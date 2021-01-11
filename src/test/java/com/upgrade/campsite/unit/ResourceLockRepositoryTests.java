package com.upgrade.campsite.unit;

import com.upgrade.campsite.entity.ResourceLock;
import com.upgrade.campsite.repository.ResourceLockRepository;
import com.upgrade.campsite.service.ResourceLockService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.PessimisticLockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.MockitoAnnotations.openMocks;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceLockRepositoryTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLockRepositoryTests.class);

    @TestConfiguration
    static class ResourceLockRepositoryTestsTestContextConfiguration {

        @Bean
        public ResourceLockService resourceLockService() {
            return new ResourceLockService();
        }
    }

    @Autowired
    private ResourceLockRepository resourceLockRepository;

    @Autowired
    private ResourceLockService resourceLockService;

    @BeforeEach
    public void init() {

        openMocks(this);

        ReflectionTestUtils.setField(resourceLockService, "reservationLockName", "RESERVATION_LOCK");
        ReflectionTestUtils.setField(resourceLockService, "reservationLockTtl", 1);
        ReflectionTestUtils.setField(resourceLockService, "resourceLockRepository", resourceLockRepository);

    }

    @Test
    @Order(1)
    synchronized void acquireLockAndReleaseLockIsOkTest() {

        try {

            List<ResourceLock> locks = (List<ResourceLock>) resourceLockRepository.findAll();

            Assertions.assertEquals(1, locks.size());

            ResourceLock acquiredLock = resourceLockService.acquireLock();

            Assertions.assertNotNull(acquiredLock);
            Assertions.assertTrue(acquiredLock.isLocked());

            resourceLockService.release(acquiredLock);

            locks = (List<ResourceLock>) resourceLockRepository.findAll();
            Assertions.assertEquals(1, locks.size());
            Assertions.assertFalse(locks.get(0).isLocked());


        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            fail();
        }
    }

    @Test
    @Order(2)
    synchronized void acquireLockedResourceFailsAndReturnsLockException() {

        List<ResourceLock> locks = (List<ResourceLock>) resourceLockRepository.findAll();

        Assertions.assertEquals(1, locks.size());

        ResourceLock acquiredLock = resourceLockService.acquireLock();

        Assertions.assertNotNull(acquiredLock);
        Assertions.assertTrue(acquiredLock.isLocked());

        try {
            resourceLockService.acquireLock();
        } catch (PessimisticLockException pEx) {
            LOGGER.error(pEx.getMessage(), pEx);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            fail();
        } finally {
            Assertions.assertEquals(1, locks.size());
        }
    }

    @Test
    @Order(3)
    synchronized void acquireResourceLockWithConcurrency() throws InterruptedException {

        final int THREADS = 4;
        final int CONCURRENT_LOCK_ATTEMPTS = 12;

        ExecutorService threadPoolExecutorService = Executors.newFixedThreadPool(THREADS);
        List<Callable<ResourceLock>> tasks = new ArrayList<>();

        for (int i = 1; i <= CONCURRENT_LOCK_ATTEMPTS ; i++) {
            Callable<ResourceLock> c = () -> {
                LOGGER.info("Trying to acquire lock...");
                return resourceLockService.acquireLock();
            };
            tasks.add(c);
        }

        List<Future<ResourceLock>> results = threadPoolExecutorService.invokeAll(tasks);

        Assertions.assertEquals(CONCURRENT_LOCK_ATTEMPTS, results.size());

        int acquiredLocks = 0;
        int failedAcquireLockAttempts = 0;

        for (Future<ResourceLock> result : results) {
            try {
                if(!Objects.isNull(result.get())) {
                    acquiredLocks++;
                }
            } catch (ExecutionException ex) {
                if (ex.getCause() instanceof PessimisticLockException) {
                    LOGGER.info("Resource was locked.");
                    failedAcquireLockAttempts++;
                }
            }
        }

        Assertions.assertEquals(1, acquiredLocks);
        Assertions.assertEquals(11, failedAcquireLockAttempts);

    }


}
