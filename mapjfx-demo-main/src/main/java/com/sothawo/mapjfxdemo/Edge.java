package com.sothawo.mapjfxdemo;

import com.sothawo.mapjfx.MapCircle;

public class Edge implements Comparable<Edge> {
    private MapCircle src, dest;
    private double weight;

    public Edge(MapCircle src, MapCircle dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public int compareTo(Edge compareEdge) {
        return (int)(this.weight - compareEdge.weight);
    }

    public void setSrc(MapCircle src) {
        this.src = src;
    }

    public void setDest(MapCircle dest) {
        this.dest = dest;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    public MapCircle getSrc() {
        return src;
    }

    public MapCircle getDest() {
        return dest;
    }

    public double getWeight() {
        return weight;
    }
}

