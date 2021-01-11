TRUNCATE TABLE campsite.resource_locks;

INSERT INTO campsite.resource_locks(id, locked, lock_timestamp) VALUES('RESERVATION_LOCK', false, now())
