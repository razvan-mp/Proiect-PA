package singleton;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Persistence manager that uses an <code>entityManagerFactory</code> to persist data from the tables
 */
public class PersistenceManager {
    public static final boolean DEBUG = false;
    private static final PersistenceManager singleton = new PersistenceManager();

    protected EntityManagerFactory entityManagerFactory;

    private PersistenceManager() {
    }

    public static PersistenceManager getInstance() {
        return singleton;
    }

    protected void createEntityManagerFactory() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");

        if (DEBUG)
            System.out.println("Persistence started at " + new java.util.Date());
    }

    public EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null)
            createEntityManagerFactory();
        return entityManagerFactory;
    }
}