package com.sothawo.mapjfxdemo;

import java.util.*;

public class CycleFinder {
//    static List<List<Integer>> adjacency;

//    public CycleFinder(Graph graph) {
//        adjacency = GraphHelper.getAdjacency(graph);
//    }

    static int[][] graph =
            {
                    {1, 2}, {1, 5}, {1, 4}, {2, 7},
                    {3, 7}, {3, 16}, {4, 5}, {4, 8},
                    {5, 6}, {5, 10}, {5, 8}, {6, 7},
                    {6, 12}, {7, 13}, {8, 9}, {9, 10},
                    {10, 14}, {11, 12}, {11, 13}, {13, 15},
                    {14, 15}, {15, 16}
            };

    static List<List<Integer>> cycles = new ArrayList<List<Integer>>();
//      static List<List<Integer>> cycles = new ArrayList<>();
    /**
     * @param args
     */
    public static void main(String[] args) {

        for (int i = 0; i < graph.length; i++)
            for (int j = 0; j < graph[i].length; j++)
            {
                List<Integer> vertexPath = new ArrayList<>();
                vertexPath.add(graph[i][j]);

                findNewCycles(vertexPath);
            }


        for (List<Integer> cy : cycles)
        {
            String s = "" + cy.get(0);

            for (int i = 1; i < cy.size(); i++)
            {
                s += "," + cy.get(i);
            }

            o(s);
        }

    }

    static void findNewCycles(List<Integer> path)
    {
        System.out.println("inc find " + path.size());
        int n = path.get(0);
        int x;
        List<Integer> sub = new ArrayList<>(path.size()+ 1);

        for (int i = 0; i < graph.length; i++)
            for (int y = 0; y <= 1; y++)
                if (graph[i][y] == n)
                //  edge refers to our current node
                {
                    x = graph[i][(y + 1) % 2];
                    if (!visited(x, path))
                    //  neighbor node not on path yet
                    {
                        if(sub.size() == 0)
                            sub.add(x);
                        else
                            sub.set(0, x);
                        System.out.println(path + " " + sub);

                        sub.addAll(1, path);
//                        System.arraycopy(path, 0, sub, 1, path.size());
                        findNewCycles(sub);
                    }
                    else if ((path.size() > 2) && (x == path.get(path.size() - 1)))
                    //  cycle found
                    {
                        List<Integer> p = normalize(path);
                        List<Integer> inv = invert(p);
                        if (isNew(p) && isNew(inv))
                        {
                            cycles.add(p);
                        }
                    }
                }
        System.out.println("fin find " + path.size());
    }

    //  check of both arrays have same lengths and contents
    static boolean equals(List<Integer> a, List<Integer> b)
    {
        boolean ret = (Objects.equals(a.get(0), b.get(0)) && (a.size() == b.size()));

        for (int index = 1; ret && (index < a.size()); index++)
        {
            if (a.get(index) != b.get(index))
            {
                ret = false;
            }
        }

        return ret;
    }

    //  create a path array with reversed order
    static List<Integer> invert(List<Integer> path)
    {
        List<Integer> p = new ArrayList<Integer>(path.size());
        System.out.println("inc invert " + path.size());

        for (int i = 0; i < path.size(); i++)
        {
            p.set(i, path.get(path.size() - i - 1));
        }
        System.out.println("sf invert " + path.size());
        return normalize(p);
    }

    //  rotate cycle path such that it begins with the smallest node
    static List<Integer> normalize(List<Integer> path)
    {
        System.out.println("inc normalize " + path.size());
        List<Integer> p = new ArrayList<>(path.size());
        int x = smallest(path);
        int n;

//        System.out.println("----------------------------\n" + path + " " + p + "\n-----------------------------");

        p.addAll(0, path);
//        System.arraycopy(path, 0, p, 0, path.size());

        while (p.size() !=0 && p.get(0) != x)
        {
            n = p.get(0);
            if(p.size() == 0)
                p.set(0, null);
            else
                p.remove(0);
//            System.arraycopy(p, 1, p, 0, p.size() - 1);
            if(p.size() > 0)
                p.set(p.size() - 1, n);
        }
        System.out.println("fin normalize " + path.size());
        return p;
    }

    //  compare path against known cycles
    //  return true, iff path is not a known cycle
    static Boolean isNew(List<Integer> path)
    {
        System.out.println("inc isnew " + path.size());
        boolean ret = true;

        for(List<Integer> p : cycles)
        {
            if (equals(p, path))
            {
                ret = false;
                break;
            }
        }
        System.out.println("fin isnew " + path.size());
        return ret;
    }

    static void o(String s)
    {
        System.out.println(s);
    }

    //  return the int of the array which is the smallest
    static int smallest(List<Integer> vertexPath)
    {
        System.out.println("inc smallest " + vertexPath.size());
        int min = vertexPath.get(0);

        for (int vertex : vertexPath)
        {
            if (vertex < min)
            {
                min = vertex;
            }
        }
        System.out.println("sf smallest " + vertexPath.size());
        return min;
    }

    //  check if vertex n is contained in path
    static Boolean visited(int n, List<Integer> vertexPath)
    {
        System.out.println("inc visited " + vertexPath.size());
        Boolean ret = false;

        for (int vertex : vertexPath)
        {
            if (vertex == n)
            {
                ret = true;
                break;
            }
        }
        System.out.println("sf visited " + vertexPath.size());
        return ret;
    }
}
