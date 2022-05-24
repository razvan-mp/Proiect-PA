package com.sothawo.mapjfxdemo;

import com.sothawo.mapjfx.MapCircle;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class GraphHelper {
    public static double getDistance(MapCircle firstPoint, MapCircle secondPoint) {
        double lon1 = firstPoint.getCenter().getLongitude();
        double lon2 = secondPoint.getCenter().getLongitude();
        double lat1 = firstPoint.getCenter().getLatitude();
        double lat2 = secondPoint.getCenter().getLatitude();

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371;

        return (c * r * 1000);
    }

    public static void getCycles(List<MapCircle> mapCircles) {
        SimpleDirectedWeightedGraph<MapCircle, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (var mapCircle : mapCircles)
            graph.addVertex(mapCircle);

        CycleDetector<MapCircle, DefaultWeightedEdge> cycleDetector = new CycleDetector<>(graph);
        var ceva = cycleDetector.findCyclesContainingVertex(mapCircles.get(7));

        var altceva = ceva.iterator();
    }

    public static List<List<Integer>> getAdjacency(Graph graph) {
        List<List<Integer>> adjacency = new ArrayList<>();
        List<Edge> edgeList = graph.getEdgesList();
        List<MapCircle> vertexList = graph.getVertexList();

        for (int index1 = 0; index1 < graph.getVertexList().size(); index1++) {
            adjacency.add(new ArrayList<>());

            for (int index2 = 0; index2 < graph.getEdgesList().size(); index2++) {
                if(edgeList.get(index2).getSrc().equals(vertexList.get(index1)))
                    adjacency.get(index1).add(vertexList.indexOf(edgeList.get(index2).getDest()));
                else if(edgeList.get(index2).getDest().equals(vertexList.get(index1)))
                    adjacency.get(index1).add(vertexList.indexOf(edgeList.get(index2).getSrc()));
            }
        }
        return adjacency;
    }

}
