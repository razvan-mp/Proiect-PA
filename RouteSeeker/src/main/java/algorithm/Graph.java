package algorithm;

import com.sothawo.mapjfx.MapCircle;
import repos.AdjacencyRepo;
import repos.NodesRepo;

import java.util.ArrayList;
import java.util.List;

import static algorithm.GraphHelper.getDistance;

/**
 * Custom graph implementation class
 */
public class Graph {
    private final List<Edge> edgesList = new ArrayList<>();
    private final List<MapCircle> vertexList;

    /**
     * Sets the <code>vertexList</code> to a list of nodes given as input and calls the <code>initialiseAdjacency</code>
     * method
     * @param vertexList list of vertices to be added to graph
     * @param graphId graph's ID used for identifying the graph after creation
     */
    public Graph(List<MapCircle> vertexList, Integer graphId) {
        this.vertexList = vertexList;
        initialiseAdjacency(graphId);
    }

    /**
     * Creates an edge list for the graph
     * @param graphId graph's ID for choosing a list to work with
     */
    private void initialiseAdjacency(Integer graphId) {
        NodesRepo nodesRepo = new NodesRepo();
        AdjacencyRepo adjacencyRepo = new AdjacencyRepo();
        List<Integer> adjacency;

        int maxId = nodesRepo.getMaxId(graphId);
        int toSubtract = 0;

        switch (graphId) {
            case 1 -> toSubtract = 1;
            case 2 -> toSubtract = nodesRepo.getMaxId(1) + 1;
            case 3 -> toSubtract = nodesRepo.getMaxId(2) + 1;
        }

        for (int index = toSubtract - 1; index < maxId; index++) {
            adjacency = adjacencyRepo.getAdjacencyById(index + 1);
            for (Integer node : adjacency)
                edgesList.add(new Edge(vertexList.get(index - toSubtract + 1), vertexList.get(node - toSubtract), getDistance(vertexList.get(index - toSubtract + 1), vertexList.get(node - toSubtract))));
        }
    }

    public List<Edge> getEdgesList() {
        return edgesList;
    }

    public List<MapCircle> getVertexList() {
        return vertexList;
    }
}
