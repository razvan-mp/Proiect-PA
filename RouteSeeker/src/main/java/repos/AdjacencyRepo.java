package repos;

import singleton.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class AdjacencyRepo {
    private final EntityManagerFactory entityManagerFactory = PersistenceManager.getInstance().getEntityManagerFactory();

    public List<Integer> getAdjacencyById(int id){
        System.out.println("orice");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery("select a.idNode1 from AdjacencyEntity a where a.idNode2=:id").setParameter("id", id);
        List<Integer> adjacency = (List<Integer>)query.getResultList();

        query = entityManager.createQuery("select a.idNode2 from AdjacencyEntity a where a.idNode1=:id").setParameter("id", id);
        adjacency.addAll((List<Integer>)query.getResultList());

        for(var adj : adjacency)
        {
            System.out.println(adj);
        }

        return null;
    }
}
