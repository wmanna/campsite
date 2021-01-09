package com.upgrade.campsite.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resource_locks")
public class ResourceLock {

    @Id
    private String id;

    private boolean locked;
    @Column(name = "lock_timestamp")
    private LocalDateTime lockTimestamp;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setLockTimestamp(LocalDateTime lockTimestamp) {
        this.lockTimestamp = lockTimestamp;
    }

    public LocalDateTime getLockTimestamp() {
        return lockTimestamp;
    }
}
