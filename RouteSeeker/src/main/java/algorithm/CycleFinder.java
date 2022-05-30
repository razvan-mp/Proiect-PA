package algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding cycles in a given custom graph.
 */
public class CycleFinder {
    List<List<Integer>> graph = new ArrayList<>();
    List<List<Integer>> cycles = new ArrayList<>();
    Graph inputGraph;

    /**
     * Calls the <code>buildGraph()</code> method for initializing the graph.
     * @param graph Graph given as input.
     */
    public CycleFinder(Graph graph) {
        buildGraph(graph);
        this.inputGraph = graph;
    }

    /**
     * Checks if a cycle is equal to another in order to keep repeating cycles out of the final list.
     * @param firstCycle first cycle to be compared
     * @param secondCycle second cycle to be compared
     * @return <code>true</code> if the cycles are equal, <code>false</code> otherwise
     */
    static boolean equals(List<Integer> firstCycle, List<Integer> secondCycle) {
        boolean returnValue = (firstCycle.get(0).equals(secondCycle.get(0))) && (firstCycle.size() == secondCycle.size());

        for (int index = 0; returnValue && (index < firstCycle.size()); index++)
            if (!firstCycle.get(index).equals(secondCycle.get(index))) {
                returnValue = false;
                break;
            }
        return returnValue;
    }

    /**
     * Finds the smallest node in a graph (refering to its actual node value)
     * @param path path to extract the smallest node from
     * @return the smallest node in the path
     */
    static int smallestNode(List<Integer> path) {
        int min = path.get(0);

        for (Integer node : path)
            if (node < min)
                min = node;
        return min;
    }

    /**
     * Checks if a given node has been visited in the current finding sequence
     * @param nodeToCheck node to be checked for being visited
     * @param path path that would include the node
     * @return <code>true</code> if the node has been visited, <code>false</code> otherwise
     */
    static boolean visited(Integer nodeToCheck, List<Integer> path) {
        for (Integer node : path)
            if (node.equals(nodeToCheck))
                return true;
        return false;
    }

    /**
     * Normalizes a given cycle (path) to ensure adjacency is respected
     * @param path given path to normalize
     * @return normalized path, returned as a <code>List of Integer</code>
     */
    static List<Integer> normalizePath(List<Integer> path) {
        List<Integer> normalized = new ArrayList<>(path);
        int smallestNode = smallestNode(path);
        int node;

        while (normalized.get(0) != smallestNode) {
            node = normalized.get(0);
            normalized.remove(0);
            normalized.add(node);
        }

        return normalized;
    }

    /**
     * Inverts a cycle such that the generated cycle will have respect to the route defined in the map
     * @param path path to be inverted
     * @return inverted path, in the form of a <code>List of Integer</code>
     */
    static List<Integer> invertPath(List<Integer> path) {
        List<Integer> inverted = new ArrayList<>();

        for (int index = 0; index < path.size(); index++)
            inverted.add(path.get(path.size() - index - 1));

        return normalizePath(inverted);
    }

    /**
     * Checks if a cycle is new or has been previously generated
     * @param path given path to check for repetition
     * @return <code>true</code> if the cycle has been previously generated, <code>false</code> otherwise
     */
    boolean isNew(List<Integer> path) {
        for (List<Integer> cycle : cycles)
            if (equals(cycle, path))
                return false;

        return true;
    }

    /**
     * Recursive algorithm that finds a cycle from a given start node, and continues by using partial cycles
     * @param path initial node (after recursion it becomes a partial cycle)
     */
    void findNewCycles(List<Integer> path) {
        int node = path.get(0);
        int nodeToVisit;
        List<Integer> subPath = new ArrayList<>();

        for (int index = 0; index <= path.size(); index++)
            subPath.add(-1);

        for (List<Integer> pair : graph)
            for (int index = 0; index <= 1; index++)
                if (pair.get(index) == node) {
                    nodeToVisit = pair.get((index + 1) % 2);
                    if (!visited(nodeToVisit, path)) {
                        List<Integer> newSubPath = new ArrayList<>();
                        newSubPath.add(nodeToVisit);
                        newSubPath.addAll(path);
                        for (int pathIndex = path.size() + 1; pathIndex < subPath.size(); pathIndex++)
                            newSubPath.add(subPath.get(pathIndex));

                        findNewCycles(newSubPath);
                    } else if ((path.size() > 2) && (nodeToVisit == path.get(path.size() - 1))) {
                        List<Integer> cycleMiddle = normalizePath(path);
                        List<Integer> cycle = invertPath(cycleMiddle);
                        if (isNew(cycleMiddle) && isNew(cycle)) {
                            cycles.add(cycleMiddle);
                            return;
                        }
                    }
                }
    }

    /**
     * Initializes the graph by adding the proper adjacencies
     * @param inputGraph input graph used to generate the graph attribute
     */
    public void buildGraph(Graph inputGraph) {
        for (int index = 0; index < inputGraph.getEdgesList().size(); index++) {
            graph.add(new ArrayList<>());
            graph.get(index).add(inputGraph.getVertexList().indexOf(inputGraph.getEdgesList().get(index).getSrc()));
            graph.get(index).add(inputGraph.getVertexList().indexOf(inputGraph.getEdgesList().get(index).getDest()));
        }
    }

    /**
     * Calls <code>findNewCycles()</code> for every two adjacent nodes in the graph
     * @return <code>a List of Lists of Integers</code> that contains all the cycles found in the graph
     */
    public List<List<Integer>> getAllCycles() {
        for (List<Integer> pair : graph)
            for (Integer node : pair) {
                List<Integer> fromPair = new ArrayList<>();
                fromPair.add(node);
                findNewCycles(fromPair);
            }

        List<List<Integer>> allCycles = new ArrayList<>();

        int index = 0;
        for (List<Integer> cycle : cycles) {
            allCycles.add(new ArrayList<>());
            for (Integer node : cycle)
                allCycles.get(index).add(node);
            index++;
        }

        return allCycles;
    }
}
