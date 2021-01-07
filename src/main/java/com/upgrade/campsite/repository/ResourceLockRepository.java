package com.upgrade.campsite.repository;

import com.upgrade.campsite.entity.ResourceLock;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ResourceLockRepository extends CrudRepository<ResourceLock, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ResourceLock> findById(String id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Iterable<ResourceLock> findAllById(Iterable<String> ids);

}