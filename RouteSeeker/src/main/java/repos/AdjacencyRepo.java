package repos;

import singleton.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * Repository class for the <code>adjacency</code> table
 */
public class AdjacencyRepo {
    private final EntityManagerFactory entityManagerFactory = PersistenceManager.getInstance().getEntityManagerFactory();

    public List<Integer> getAdjacencyById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("select a.idNode2 from AdjacencyEntity a where a.idNode1=:id").setParameter("id", id);

        return (List<Integer>) query.getResultList();
    }
}
