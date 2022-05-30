package repos;

import entities.NodesEntity;
import singleton.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Repository class for the <code>nodes</code> table
 */
public class NodesRepo {
    private final EntityManagerFactory entityManagerFactory = PersistenceManager.getInstance().getEntityManagerFactory();

    public NodesEntity findById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return (NodesEntity) entityManager.createQuery("select c from NodesEntity c where c.id =:id").setParameter("id", id).getResultList().get(0);
    }


    public Integer getMaxId(int graphId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return (Integer) entityManager.createQuery("select max(c.id) from NodesEntity c where c.idGraph =:graphId").setParameter("graphId", graphId).getResultList().get(0);
    }
}
