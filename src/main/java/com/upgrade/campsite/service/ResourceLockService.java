package com.upgrade.campsite.service;

import com.upgrade.campsite.entity.ResourceLock;
import com.upgrade.campsite.repository.ResourceLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResourceLockService {

    @Autowired
    ResourceLockRepository resourceLockRepository;

    @Transactional
    public ResourceLock acquireLock() {

        Optional<ResourceLock> lock = resourceLockRepository.findById("RESERVATION_LOCK");

        lock.ifPresent( value -> {
            if (value.isLocked()) {
                throw new PessimisticLockException();
            }
            value.setLocked(true);
            value.setLockTimestamp(LocalDateTime.now());
            resourceLockRepository.save(value);
        });

        return lock.orElseThrow(PersistenceException::new);

    }

    public void release(ResourceLock lock) {

        lock.setLocked(false);
        resourceLockRepository.save(lock);

    }
}
