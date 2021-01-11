package com.upgrade.campsite.service;

import com.upgrade.campsite.exception.Constant;
import com.upgrade.campsite.entity.ResourceLock;
import com.upgrade.campsite.repository.ResourceLockRepository;
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

    /* This method releases a given lock received as parameter. */
    public void release(ResourceLock lock) {

        if (!Objects.isNull(lock)) {
            lock.setLocked(false);
            resourceLockRepository.save(lock);
        }
    }
}
