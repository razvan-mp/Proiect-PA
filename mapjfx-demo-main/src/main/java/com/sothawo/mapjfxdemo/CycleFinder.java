package com.sothawo.mapjfxdemo;

import java.util.ArrayList;
import java.util.List;

public class CycleFinder {
    static int[][] graph = new int[1_000][2];

    static List<int[]> cycles = new ArrayList<>();

    CycleFinder(Graph graph) {
        buildGraph(graph);
    }

    static void findNewCycles(int[] path) {
        int n = path[0];
        int x;
        int[] sub = new int[path.length + 1];

        for (int[] ints : graph)
            for (int y = 0; y <= 1; y++)
                if (ints[y] == n) {
                    x = ints[(y + 1) % 2];
                    if (!visited(x, path)) {
                        sub[0] = x;
                        System.arraycopy(path, 0, sub, 1, path.length);
                        findNewCycles(sub);
                    } else if ((path.length > 2) && (x == path[path.length - 1])) {
                        int[] p = normalize(path);
                        int[] inv = invert(p);
                        if (isNew(p) && isNew(inv)) {
                            cycles.add(p);
                        }
                    }
                }
    }

    //  check of both arrays have same lengths and contents
    static Boolean equals(int[] a, int[] b) {
        boolean ret = (a[0] == b[0]) && (a.length == b.length);

        for (int i = 1; ret && (i < a.length); i++) {
            if (a[i] != b[i]) {
                ret = false;
                break;
            }
        }

        return ret;
    }

    static int[] invert(int[] path) {
        int[] p = new int[path.length];

        for (int i = 0; i < path.length; i++) {
            p[i] = path[path.length - 1 - i];
        }

        return normalize(p);
    }

    static int[] normalize(int[] path) {
        int[] p = new int[path.length];
        int x = smallest(path);
        int n;

        System.arraycopy(path, 0, p, 0, path.length);

        while (p[0] != x) {
            n = p[0];
            System.arraycopy(p, 1, p, 0, p.length - 1);
            p[p.length - 1] = n;
        }

        return p;
    }

    static Boolean isNew(int[] path) {
        boolean ret = true;

        for (int[] p : cycles) {
            if (equals(p, path)) {
                ret = false;
                break;
            }
        }

        return ret;
    }

    static void print(String s) {
        System.out.println(s);
    }

    static int smallest(int[] path) {
        int min = path[0];

        for (int p : path) {
            if (p < min) {
                min = p;
            }
        }

        return min;
    }

    static Boolean visited(int n, int[] path) {
        boolean ret = false;
        for (int p : path) {
            if (p == n) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    void buildGraph(Graph graf) {
        System.out.println("ceva");
        for (int index = 0; index < graf.getEdgesList().size(); index++) {
            graph[index][0] = graf.getVertexList().indexOf(graf.getEdgesList().get(index).getSrc());
            graph[index][1] = graf.getVertexList().indexOf(graf.getEdgesList().get(index).getDest());
        }

        for (int i = 0; i < graf.getEdgesList().size(); i++) {
            System.out.println(graph[i][0] + " " + graph[i][1]);
        }
    }

    public List<List<Integer>> getAllCyclesOfLength(int length) {
        for (int[] ints : graph)
            for (int anInt : ints) {
                findNewCycles(new int[]{anInt});
            }

        List<List<Integer>> allCycles = new ArrayList<>();

        int index = 0;
        for (int[] cy : cycles) {
            if (cy.length == length) {
                allCycles.add(new ArrayList<>());
                for (var nod : cy)
                    allCycles.get(index).add(nod);
                index += 1;
            }
        }
        return allCycles;
    }
}
