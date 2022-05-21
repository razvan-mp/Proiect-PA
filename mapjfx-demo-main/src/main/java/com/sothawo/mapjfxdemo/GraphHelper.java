package com.sothawo.mapjfxdemo;

import com.sothawo.mapjfx.MapCircle;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.alg.cycle.SzwarcfiterLauerSimpleCycles;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

        return (c * r);
    }

    public static void getCycles(List<MapCircle> mapCircles) {
        SimpleDirectedWeightedGraph<MapCircle, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (var mapCircle : mapCircles)
            graph.addVertex(mapCircle);

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(0), mapCircles.get(1)), getDistance(mapCircles.get(0), mapCircles.get(1)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(1), mapCircles.get(0)), getDistance(mapCircles.get(1), mapCircles.get(0)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(0), mapCircles.get(4)), getDistance(mapCircles.get(0), mapCircles.get(4)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(4), mapCircles.get(0)), getDistance(mapCircles.get(4), mapCircles.get(0)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(0), mapCircles.get(3)), getDistance(mapCircles.get(0), mapCircles.get(3)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(3), mapCircles.get(0)), getDistance(mapCircles.get(3), mapCircles.get(0)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(1), mapCircles.get(6)), getDistance(mapCircles.get(1), mapCircles.get(6)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(6), mapCircles.get(1)), getDistance(mapCircles.get(6), mapCircles.get(1)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(2), mapCircles.get(6)), getDistance(mapCircles.get(2), mapCircles.get(6)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(6), mapCircles.get(2)), getDistance(mapCircles.get(6), mapCircles.get(2)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(2), mapCircles.get(15)), getDistance(mapCircles.get(2), mapCircles.get(15)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(15), mapCircles.get(2)), getDistance(mapCircles.get(15), mapCircles.get(2)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(3), mapCircles.get(4)), getDistance(mapCircles.get(3), mapCircles.get(4)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(4), mapCircles.get(3)), getDistance(mapCircles.get(4), mapCircles.get(3)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(3), mapCircles.get(7)), getDistance(mapCircles.get(3), mapCircles.get(7)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(7), mapCircles.get(3)), getDistance(mapCircles.get(7), mapCircles.get(3)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(4), mapCircles.get(5)), getDistance(mapCircles.get(4), mapCircles.get(5)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(5), mapCircles.get(4)), getDistance(mapCircles.get(5), mapCircles.get(4)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(4), mapCircles.get(9)), getDistance(mapCircles.get(4), mapCircles.get(9)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(9), mapCircles.get(4)), getDistance(mapCircles.get(9), mapCircles.get(4)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(4), mapCircles.get(7)), getDistance(mapCircles.get(4), mapCircles.get(7)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(7), mapCircles.get(4)), getDistance(mapCircles.get(7), mapCircles.get(4)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(5), mapCircles.get(6)), getDistance(mapCircles.get(5), mapCircles.get(6)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(6), mapCircles.get(5)), getDistance(mapCircles.get(6), mapCircles.get(5)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(5), mapCircles.get(11)), getDistance(mapCircles.get(5), mapCircles.get(11)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(11), mapCircles.get(5)), getDistance(mapCircles.get(11), mapCircles.get(5)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(6), mapCircles.get(12)), getDistance(mapCircles.get(6), mapCircles.get(12)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(12), mapCircles.get(6)), getDistance(mapCircles.get(12), mapCircles.get(6)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(7), mapCircles.get(8)), getDistance(mapCircles.get(7), mapCircles.get(8)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(8), mapCircles.get(7)), getDistance(mapCircles.get(8), mapCircles.get(7)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(8), mapCircles.get(9)), getDistance(mapCircles.get(8), mapCircles.get(9)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(9), mapCircles.get(8)), getDistance(mapCircles.get(9), mapCircles.get(8)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(9), mapCircles.get(13)), getDistance(mapCircles.get(9), mapCircles.get(13)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(13), mapCircles.get(9)), getDistance(mapCircles.get(13), mapCircles.get(9)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(10), mapCircles.get(11)), getDistance(mapCircles.get(10), mapCircles.get(11)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(11), mapCircles.get(10)), getDistance(mapCircles.get(11), mapCircles.get(10)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(10), mapCircles.get(12)), getDistance(mapCircles.get(10), mapCircles.get(12)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(12), mapCircles.get(10)), getDistance(mapCircles.get(12), mapCircles.get(10)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(12), mapCircles.get(14)), getDistance(mapCircles.get(12), mapCircles.get(14)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(14), mapCircles.get(12)), getDistance(mapCircles.get(14), mapCircles.get(12)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(13), mapCircles.get(14)), getDistance(mapCircles.get(13), mapCircles.get(14)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(14), mapCircles.get(13)), getDistance(mapCircles.get(14), mapCircles.get(13)));

        graph.setEdgeWeight(graph.addEdge(mapCircles.get(14), mapCircles.get(15)), getDistance(mapCircles.get(14), mapCircles.get(15)));
        graph.setEdgeWeight(graph.addEdge(mapCircles.get(15), mapCircles.get(14)), getDistance(mapCircles.get(15), mapCircles.get(14)));

        CycleDetector<MapCircle, DefaultWeightedEdge> cycleDetector = new CycleDetector<>(graph);
        var ceva = cycleDetector.findCyclesContainingVertex(mapCircles.get(7));

        var altceva = ceva.iterator();
    }
}
