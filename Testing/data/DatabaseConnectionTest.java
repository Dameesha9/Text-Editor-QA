package testing.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import dal.DatabaseConnection;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatabaseConnectionTest {

    @BeforeAll
    static void setup() {
        System.out.println("Testing Singleton Pattern for DatabaseConnection");
    }

    @Test
    void testGetInstanceReturnsSameObject() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertSame(instance1, instance2, "Both instances should be the same object");
    }

    @Test
    void testSingletonAcrossMultipleCalls() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();

        assertSame(instance1, instance2, "Instance 1 and 2 should be same");
        assertSame(instance2, instance3, "Instance 2 and 3 should be same");
        assertSame(instance1, instance3, "Instance 1 and 3 should be same");
    }

    @Test
    void testThreadSafety() throws InterruptedException {
        int threadCount = 10;
        Set<DatabaseConnection> instances = new HashSet<>();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                synchronized (instances) {
                    instances.add(DatabaseConnection.getInstance());
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS),
                   "All threads should complete within timeout");

        assertEquals(1, instances.size(),
                    "Only one instance should exist across all threads");
    }

    @Test
    void testGetConnectionNotNull() {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        assertNotNull(instance.getConnection(),
                     "Database connection should be established");
    }

    @Test
    void testConnectionPersistence() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        assertSame(instance1.getConnection(), instance2.getConnection(),
                  "Connection object should be the same across instances");
    }
}
