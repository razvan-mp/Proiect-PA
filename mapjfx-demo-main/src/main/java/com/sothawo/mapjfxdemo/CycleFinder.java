package com.sothawo.mapjfxdemo;

import com.sothawo.mapjfx.MapCircle;

import java.util.ArrayList;
import java.util.List;

public class CycleFinder {
    static List<List<Integer>> graph = new ArrayList<>();
    static List<List<Integer>> cycles = new ArrayList<>();
    Graph inputGraph;

    CycleFinder(Graph graph) {
        buildGraph(graph);
        this.inputGraph = graph;
    }

    static boolean equals(List<Integer> firstCycle, List<Integer> secondCycle) {
        boolean returnValue = (firstCycle.get(0).equals(secondCycle.get(0))) && (firstCycle.size() == secondCycle.size());

        for (int index = 0; returnValue && (index < firstCycle.size()); index++)
            if (!firstCycle.get(index).equals(secondCycle.get(index))) {
                returnValue = false;
                break;
            }
        return returnValue;
    }

    static boolean isNew(List<Integer> path) {
        for (List<Integer> cycle : cycles)
            if (equals(cycle, path))
                return false;

        return true;
    }

    static int smallestNode(List<Integer> path) {
        int min = path.get(0);

        for (Integer node : path)
            if (node < min)
                min = node;
        return min;
    }

    static boolean visited(Integer nodeToCheck, List<Integer> path) {
        for (Integer node : path)
            if (node.equals(nodeToCheck))
                return true;
        return false;
    }

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

    static List<Integer> invertPath(List<Integer> path) {
        List<Integer> inverted = new ArrayList<>();

        for (int index = 0; index < path.size(); index++)
            inverted.add(path.get(path.size() - index - 1));

        return normalizePath(inverted);
    }

    static void findNewCycles(List<Integer> path) {
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

    void buildGraph(Graph inputGraph) {
        for (int index = 0; index < inputGraph.getEdgesList().size(); index++) {
            graph.add(new ArrayList<>());
            graph.get(index).add(inputGraph.getVertexList().indexOf(inputGraph.getEdgesList().get(index).getSrc()));
            graph.get(index).add(inputGraph.getVertexList().indexOf(inputGraph.getEdgesList().get(index).getDest()));
        }
    }

    public List<List<Integer>> getAllCyclesOfLength(double length) {
        for (List<Integer> pair : graph)
            for (Integer node : pair) {
                List<Integer> fromPair = new ArrayList<>();
                fromPair.add(node);
                findNewCycles(fromPair);
            }

        List<List<Integer>> allCycles = new ArrayList<>();

        int index = 0;
        for (List<Integer> cycle : cycles)
            if (cycleLengthIsWithin(cycle, length)) {
                allCycles.add(new ArrayList<>());
                for (Integer node : cycle)
                    allCycles.get(index).add(node);
                index++;
            }

        return allCycles;
    }

    private boolean cycleLengthIsWithin(List<Integer> cycle, double length) {
        double cycleLength = 0;

        double distance;
        MapCircle firstNode, secondNode;
        for (int index = 0; index < cycle.size() - 1; index++) {
            firstNode = inputGraph.getVertexList().get(cycle.get(index));
            secondNode = inputGraph.getVertexList().get(cycle.get(index + 1));
            distance = GraphHelper.getDistance(firstNode, secondNode);
            if (distance > length)
                return false;
            cycleLength += distance;
        }

        firstNode = inputGraph.getVertexList().get(0);
        secondNode = inputGraph.getVertexList().get(cycle.get(cycle.size() - 1));
        distance = GraphHelper.getDistance(firstNode, secondNode);
        if (distance > length)
            return false;
        cycleLength += distance;


        if (length <= 1000 && ((cycleLength - 250 < length) || (cycleLength + 250 < length))) {
            System.out.println("Cycle length: " + cycleLength);
            return true;
        } else return length >= 1000 && ((cycleLength - 500 < length) || (cycleLength + 500 < length));
    }
}
