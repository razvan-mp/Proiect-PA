package com.sothawo.mapjfxdemo;

import com.sothawo.mapjfx.MapCircle;
import com.sothawo.mapjfxdemo.Edge;

import java.util.*;
import java.lang.*;

import static com.sothawo.mapjfxdemo.GraphHelper.getDistance;

class Graph {
    private final List<Edge> edgesList = new ArrayList<>();
    private final List<MapCircle> vertexList;

    public Graph(List<MapCircle> vertexList) {
        this.vertexList = vertexList;
        initialiseAdjacency();
    }

    private void initialiseAdjacency() {
        edgesList.add(new Edge(vertexList.get(0), vertexList.get(1), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(0), vertexList.get(3), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(0), vertexList.get(4), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(1), vertexList.get(6), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(2), vertexList.get(6), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(2), vertexList.get(15), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(3), vertexList.get(4), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(3), vertexList.get(7), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(4), vertexList.get(5), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(4), vertexList.get(7), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(4), vertexList.get(9), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(5), vertexList.get(6), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(5), vertexList.get(11), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(6), vertexList.get(12), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(7), vertexList.get(8), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(8), vertexList.get(9), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(9), vertexList.get(13), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(10), vertexList.get(11), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(10), vertexList.get(12), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(12), vertexList.get(14), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(13), vertexList.get(14), getDistance(vertexList.get(0), vertexList.get(1))));
        edgesList.add(new Edge(vertexList.get(14), vertexList.get(15), getDistance(vertexList.get(0), vertexList.get(1))));
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

    //    private int find(List<Subset> subsets, int i) {
//        // find root and make root as parent of i
//        // (path compression)
//        if (subsets.get(i).parent != i)
//            subsets.get(i).parent = find(subsets, subsets.get(i).parent);
//
//        return subsets.get(i).parent;
//    }
//
//    /**
//     * Function used for the union of 2 vertex subset.
//     * @param subsets An array of subsets.
//     * @param vertex1 The first vertex.
//     * @param vertex2 The second vertex.
//     */
//    private void union(Subset[] subsets, int vertex1, int vertex2) {
//        int xRoot = find(subsets, vertex1);
//        int yRoot = find(subsets, vertex2);
//
//        if (subsets[xRoot].rank < subsets[yRoot].rank)
//            subsets[xRoot].parent = yRoot;
//
//        else if (subsets[xRoot].rank > subsets[yRoot].rank)
//            subsets[yRoot].parent = xRoot;
//        else {
//            subsets[yRoot].parent = xRoot;
//            subsets[xRoot].rank++;
//        }
//    }
//
//    /**
//     * Constructs MST using Kruskal's algorithm.
//     */
//    public Edge[] kruskalMST() {
//        Edge[] result = new Edge[vertexNr];
//
//        int indexResult = 0;
//
//        for (int index = 0; index < vertexNr; index++)
//            result[index] = new Edge();
//
//        Arrays.sort(edge);
//
//        Subset[] subsets = new Subset[vertexNr];
//        for (int index = 0; index < vertexNr; ++index)
//            subsets[index] = new Subset();
//
//        for (int v = 0; v < vertexNr; ++v) {
//            subsets[v].parent = v;
//            subsets[v].rank = 0;
//        }
//
//        int index = 0;
//
//        while (indexResult < vertexNr - 1) {
//            Edge next_edge = edge[index++];
//
//            int vertex1 = find(subsets, next_edge.getSrc());
//            int vertex2 = find(subsets, next_edge.getDest());
//            if (vertex1 != vertex2) {
//                result[indexResult++] = next_edge;
//                union(subsets, vertex1, vertex2);
//            }
//        }
//
//        System.out.println("Following are the edges in " + "the constructed MST");
//        int minimumCost = 0;
//        for (index = 0; index < indexResult; ++index) {
//            System.out.println(result[index].getSrc() + " (" + intersectionList.get(result[index].getSrc()).getName() + ')' + " -- "
//                    + result[index].getDest() + " (" + intersectionList.get(result[index].getDest()).getName() + ')'
//                    + " == " + result[index].getWeight());
//            minimumCost += result[index].getWeight();
//        }
//        System.out.println("Minimum Cost Spanning Tree " + minimumCost + '\n');
//
//        return result;
//    }
//
//    /**
//     * Calls the DFS algorithm.
//     * @param startVertex
//     * @param visited A boolean Array of visited nodes.
//     */
//    private void dfsUtil(int startVertex, boolean[] visited) {
//        visited[startVertex] = true;
//        System.out.print(intersectionList.get(startVertex) + " ");
//
//        for (int n : adj[startVertex]) {
//            if (!visited[n])
//                dfsUtil(n, visited);
//        }
//    }
//
//    /**
//     * Prepares for a DFS recursive algorithm.
//     * @param startVertex the vertex from which to start the DFS algorithm.
//     */
//    public void dfs(int startVertex) {
//        boolean[] visited = new boolean[vertexNr];
//
//        dfsUtil(startVertex, visited);
//    }
//
//    /**
//     * Finds a Hamiltonian circuit using DFS search applied on a MST.
//     * @param startVertex The starting point for the DFS algorithm.
//     */
//    public void getHamiltonianCircle(int startVertex)
//    {
//        Edge[] edgesKruskal = kruskalMST();
//        setAdjLinkedList(edgesKruskal);
//        dfs(startVertex);
//    }
//
//
//    private void setAdjLinkedList(Edge[] result) {
//        for (Edge edge : result) {
//            if (edge.getDest() != edge.getSrc()) {
//                adj[edge.getSrc()].add(edge.getDest());
//                adj[edge.getDest()].add(edge.getSrc());
//            }
//        }
//
//    }
//
//    public void setEdge(Edge[] edge) {
//        this.edge = edge;
//    }
//
//    public Edge[] getEdge() {
//        return edge;
//    }
//
    public class Subset {
        int parent, rank;
    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < edge.length; i++) {
//            sb.append("Source: ").append(edge[i].getSrc()).append(" Dest: ").append(edge[i].getDest()).append(" Weight: ").append(edge[i].getWeight()).append("\n");
//        }
//
//        return sb.toString();
//    }
}
