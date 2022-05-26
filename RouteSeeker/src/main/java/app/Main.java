package app;

import com.sothawo.mapjfxdemo.RouteSeeker;
import repos.AdjacencyRepo;

public class Main {
    public static void main(String[] args) {
        AdjacencyRepo adjacencyEntity = new AdjacencyRepo();
        adjacencyEntity.getAdjacencyById(1);

        RouteSeeker.main(args);
    }
}
