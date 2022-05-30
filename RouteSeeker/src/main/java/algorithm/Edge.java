package algorithm;

import com.sothawo.mapjfx.MapCircle;

/**
 * Edge class used for building the graph. Contains <code>MapCircle</code> attributes that represent the source and the
 * destination nodes for every <code>Edge</code>
 */
public class Edge implements Comparable<Edge> {
    private final MapCircle src;
    private final MapCircle dest;
    private final double weight;

    /**
     * Creates a new edge to be used in the graph
     * @param src source node
     * @param dest destination node
     * @param weight distance between the two nodes (on the map)
     */
    public Edge(MapCircle src, MapCircle dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    /**
     * Compares two <code>Edge</code> objects to each other
     * @param compareEdge the object to be compared.
     * @return <code>true</code> if current edge has a greater weight, <code>false</code> otherwise
     */
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

