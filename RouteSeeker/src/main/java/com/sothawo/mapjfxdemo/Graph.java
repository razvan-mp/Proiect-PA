package com.sothawo.mapjfxdemo;

import com.sothawo.mapjfx.MapCircle;
import repos.AdjacencyRepo;
import repos.NodesRepo;

import java.util.*;
import java.lang.*;

import static com.sothawo.mapjfxdemo.GraphHelper.getDistance;

class Graph {
    private final List<Edge> edgesList = new ArrayList<>();
    private final List<MapCircle> vertexList;

    public Graph(List<MapCircle> vertexList, Integer graphId) {
        this.vertexList = vertexList;
        initialiseAdjacency(graphId);
    }

    private void initialiseAdjacency(Integer graphId) {
        NodesRepo nodesRepo = new NodesRepo();
        AdjacencyRepo adjacencyRepo = new AdjacencyRepo();
        List<Integer> adjacency;

        int maxId = nodesRepo.getMaxId(graphId);
        int toSubtract = 0;

        switch(graphId) {
            case 1 -> toSubtract = 1;
            case 2 -> toSubtract = nodesRepo.getMaxId(1) + 1;
            case 3 -> toSubtract = nodesRepo.getMaxId(2) + 1;
        }

        for (int index = toSubtract - 1; index < maxId; index++) {
            adjacency = adjacencyRepo.getAdjacencyById(index + 1);
            for (Integer node : adjacency)
//                System.out.println(integer);
                edgesList.add(new Edge(vertexList.get(index - toSubtract + 1), vertexList.get(node - toSubtract), getDistance(vertexList.get(index - toSubtract + 1), vertexList.get(node - toSubtract))));
        }
    }

    public List<Edge> getEdgesList() {
        return edgesList;
    }

    public List<MapCircle> getVertexList() {
        return vertexList;
    }

    public Set<MapCircle> getAdjacentVertices(MapCircle mapCircle) {
        Set<MapCircle> mapCircleSet = new HashSet<>();
        for (var edge : edgesList) {
            if (edge.getDest().equals(mapCircle) || edge.getSrc().equals(mapCircle)) {
                mapCircleSet.add(edge.getDest());
                mapCircleSet.add(edge.getSrc());
            }
        }
        return mapCircleSet;
    }
}
