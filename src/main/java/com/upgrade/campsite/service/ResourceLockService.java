package com.upgrade.campsite.service;

import com.upgrade.campsite.exception.Constant;
import com.upgrade.campsite.entity.ResourceLock;
import com.upgrade.campsite.repository.ResourceLockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PessimisticLockException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class ResourceLockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLock.class);

    @Value( "${lock.resource.reservation.name}")
    private String reservationLockName;

    @Value( "${lock.resource.reservation.ttl}")
    private long reservationLockTtl;

    @Autowired
    ResourceLockRepository resourceLockRepository;

    /* This method provides an exclusive lock which prevents dirty reads and writes
    even in distributed system with multiple instances. The lock has a default ttl. */
    @Transactional
    public ResourceLock acquireLock() {

        ResourceLock lock = resourceLockRepository
                .findById(reservationLockName)
                .orElseThrow(() -> new NoSuchElementException(Constant.RESOURCE_LOCK_NOT_FOUND_MSG));

        // TODO: Implement retry.

        if (lock.isLocked() && ttlHasNotExpired(lock)) {
            throw new PessimisticLockException();
        }
        lock.setLocked(true);
        lock.setLockTimestamp(LocalDateTime.now());
        return resourceLockRepository.save(lock);
    }

    private boolean ttlHasNotExpired(ResourceLock lock) {
        return lock.getLockTimestamp().isAfter(LocalDateTime.now().minusMinutes(reservationLockTtl));
    }

    /* This method releases a given lock received as parameter.
    * If there was an error releasing a lock it returns false and it should alert. */
    public boolean release(ResourceLock lock) {

        try {
            if (!Objects.isNull(lock)) {
                lock.setLocked(false);
                resourceLockRepository.save(lock);
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            // TODO: Alert error
            return false;
        }

        return true;
    }
}
