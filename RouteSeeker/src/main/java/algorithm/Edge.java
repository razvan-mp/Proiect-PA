package algorithm;

import com.sothawo.mapjfx.MapCircle;

public class Edge implements Comparable<Edge> {
    private final MapCircle src;
    private final MapCircle dest;
    private final double weight;

    public Edge(MapCircle src, MapCircle dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public int compareTo(Edge compareEdge) {
        return (int) (this.weight - compareEdge.weight);
    }

    public MapCircle getSrc() {
        return src;
    }

    public MapCircle getDest() {
        return dest;
    }
}

