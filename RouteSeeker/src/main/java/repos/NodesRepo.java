package repos;

import entities.NodesEntity;
import singleton.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class NodesRepo {
    private final EntityManagerFactory entityManagerFactory = PersistenceManager.getInstance().getEntityManagerFactory();

    public NodesEntity findById(int id)
    {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return (NodesEntity) entityManager.createQuery("select c from NodesEntity c where c.id =:id").setParameter("id", id).getResultList().get(0);
    }
}
