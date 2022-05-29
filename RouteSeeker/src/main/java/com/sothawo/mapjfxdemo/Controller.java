/*
 Copyright 2015-2020 Peter-Josef Meisch (pj.meisch@sothawo.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.sothawo.mapjfxdemo;

import com.sothawo.mapjfx.*;
import entities.NodesEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import repos.NodesRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Controller {
    private static Graph graphExpo;
    private static Graph graphDacia;
    private static List<List<Integer>> cyclesExpo;
    private static List<List<Integer>> cyclesDacia;
    private static final Coordinate coordExpo = new Coordinate(47.18458144737681, 27.565773142875663);
    private static final Coordinate coordRondDacia = new Coordinate(47.16852245494382, 27.553073638333757);
    private static final Coordinate coordNordIasi = new Coordinate(47.230760, 27.583033);
    private static final Coordinate coordSudIasi = new Coordinate(47.126273, 27.596461);
    private static final Coordinate coordVestIasi = new Coordinate(47.175779, 27.499281);
    private static final Coordinate coordEstIasi = new Coordinate(47.168813, 27.670672);
    private static final Extent extindereIasi = Extent.forCoordinates(coordNordIasi, coordSudIasi, coordVestIasi, coordEstIasi);
    private static final List<CoordinateLine> drawnCycles = new ArrayList<>();
    private static final int ZOOM_DEFAULT = 16;
    private static int location;
    private static List<Color> colorList = new ArrayList<>();
    final List<CoordinateLine> pathListExpo = new ArrayList<>();
    final List<CoordinateLine> pathListDacia = new ArrayList<>();
    final List<Coordinate> predefinedPointsListExpo = new ArrayList<>();
    final List<Coordinate> predefinedPointsListDacia = new ArrayList<>();
    final List<MapCircle> predefinedDaciaMapCircleList = new ArrayList<>();
    final List<MapCircle> predefinedExpoMapCircleList = new ArrayList<>();

    @FXML
    private TextField selectDistance;

    @FXML
    private Button buttonExpo;

    @FXML
    private Button buttonRondDacia;

    @FXML
    private MapView mapView;

    @FXML
    private HBox topControls;

    @FXML
    private Slider sliderZoom;

    @FXML
    private Accordion leftControls;

    @FXML
    private TitledPane optionsLocations;

    @FXML
    private CheckBox checkConstrainIasi;

    public Controller() {
        addColors();
        addAllPointsList();
        addMapCircles();
    }

    private void addColors() {
        colorList = List.of(new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.BLUE, Color.INDIGO, Color.VIOLET, Color.HOTPINK, Color.DARKTURQUOISE, Color.SALMON});
    }

    private void addAllPointsList() {
        NodesRepo nodesRepo = new NodesRepo();
        Integer maxId = nodesRepo.getMaxId(1);

        for (int index = 1; index <= maxId; index++) {
            NodesEntity nodesEntity = nodesRepo.findById(index);
            this.predefinedPointsListExpo.add(new Coordinate(nodesEntity.getLatitude(), nodesEntity.getLongitude()));
        }

        Integer newMaxId = nodesRepo.getMaxId(2);

        for (int index = maxId + 1; index <= newMaxId; index++) {
            NodesEntity nodesEntity = nodesRepo.findById(index);
            this.predefinedPointsListDacia.add(new Coordinate(nodesEntity.getLatitude(), nodesEntity.getLongitude()));
        }
    }

    private void addMapCircles() {
        for (var point : predefinedPointsListExpo)
            predefinedExpoMapCircleList.add(new MapCircle(point, 10).setVisible(true).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE));

        for (var point : predefinedPointsListDacia)
            predefinedDaciaMapCircleList.add(new MapCircle(point, 10).setVisible(true).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE));
    }

    public void initMapAndControls(Projection projection) {
        mapView.setCustomMapviewCssURL(Objects.requireNonNull(getClass().getResource("/custom_mapview.css")));
        leftControls.setExpandedPane(optionsLocations);

        setControlsDisable(true);

        selectDistance.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                if (location == 1)
                    deleteGraph(1);
                else
                    deleteGraph(2);
            } else {
                try {
                    deleteGraph(1);
                    deleteGraph(2);
                    System.out.println("Path length changed to:" + Double.parseDouble(newValue));
                    if (cyclesDacia != null && cyclesExpo != null)
                        filterCycles(Double.parseDouble(newValue));
                } catch (NumberFormatException e) {
                    selectDistance.setText(oldValue);
                }
            }
        });
        selectDistance.setText("500");

        buttonExpo.setOnAction(event -> {
            location = 1;
            mapView.setCenter(coordExpo);
            deleteGraph(2);
            drawAllCycles(1);
        });

        buttonRondDacia.setOnAction(event -> {
            location = 2;
            mapView.setCenter(coordRondDacia);
            deleteGraph(1);
            drawAllCycles(2);
        });

        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty());

        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });

        MapType mapType = MapType.OSM;
        mapView.setMapType(mapType);

        checkConstrainIasi.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                mapView.constrainExtent(extindereIasi);
            } else {
                mapView.clearConstrainExtent();
            }
        }));

        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());
    }

    private void filterCycles(double length) {
        Integer counter = 0;
        if (location == 1) {
            createCycles(length, counter, pathListExpo, cyclesExpo);
        } else {
            createCycles(length, counter, pathListDacia, cyclesDacia);
        }
    }

    private void createCycles(double length, Integer counter, List<CoordinateLine> pathList, List<List<Integer>> cycleList) {
        for (var path : pathList)
            path.setVisible(false);

        if (drawnCycles != null) {
            for (CoordinateLine drawnPath : drawnCycles)
                drawnPath.setVisible(false);
        }

        for (var cycle : cycleList)
            if (cycleLengthIsWithin(length, cycle)) {
                drawCycle(cycle, counter);
                counter++;
            }
    }

    private void drawCycle(List<Integer> cycle, Integer count) {
        CoordinateLine pathToDraw;
        List<MapCircle> mapCircleList;
        if (location == 1)
            mapCircleList = predefinedExpoMapCircleList;
        else
            mapCircleList = predefinedDaciaMapCircleList;

        for (int nodeIndex = 0; nodeIndex < cycle.size() - 1; nodeIndex++) {
            pathToDraw = new CoordinateLine(mapCircleList.get(cycle.get(nodeIndex)).getCenter(),
                    mapCircleList.get(cycle.get(nodeIndex + 1)).getCenter()).setVisible(true).setColor(colorList.get(count));
            drawnCycles.add(pathToDraw);
            mapView.addCoordinateLine(pathToDraw);
        }
        pathToDraw = new CoordinateLine(mapCircleList.get(cycle.get(0)).getCenter(),
                mapCircleList.get(cycle.get(cycle.size() - 1)).getCenter()).setVisible(true).setColor(colorList.get(count));
        drawnCycles.add(pathToDraw);
        mapView.addCoordinateLine(pathToDraw);
    }

    private boolean cycleLengthIsWithin(double length, List<Integer> cycle) {
        int cycleLength = 0;
        List<MapCircle> mapCircles;

        if (location == 1)
            mapCircles = predefinedExpoMapCircleList;
        else
            mapCircles = predefinedDaciaMapCircleList;

        for (int index = 0; index < cycle.size() - 1; index++) {
            cycleLength += GraphHelper.getDistance(mapCircles.get(cycle.get(index)), mapCircles.get(cycle.get(index + 1)));
        }

        cycleLength += GraphHelper.getDistance(mapCircles.get(cycle.get(cycle.size() - 1)), mapCircles.get(cycle.get(0)));

        return !(cycleLength > length + 250) && !(cycleLength < length - 250);
    }

    private void deleteGraph(Integer graphId) {
        if (graphId == 1) {
            for (var path : pathListExpo)
                path.setVisible(false);
        } else {
            for (var path : pathListDacia)
                path.setVisible(false);
        }
    }

    private void setControlsDisable(boolean flag) {
        topControls.setDisable(flag);
        leftControls.setDisable(flag);
    }

    private void drawAllEdges(List<Edge> edges, Integer graphId) {
        if (graphId == 1) {
            for (var edge : edges) {
                pathListExpo.add(new CoordinateLine(
                        edge.getSrc().getCenter(),
                        edge.getDest().getCenter())
                        .setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
            }
            for (var path : pathListExpo)
                mapView.addCoordinateLine(path);
        } else {
            for (var edge : edges) {
                pathListDacia.add(new CoordinateLine(
                        edge.getSrc().getCenter(),
                        edge.getDest().getCenter())
                        .setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
            }
            for (var path : pathListDacia)
                mapView.addCoordinateLine(path);
        }
    }

    private void drawAllCycles(int graphId) {
        if (graphId == 1) {
            addCyclesToMap(graphExpo, cyclesExpo, pathListExpo);
        } else {
            addCyclesToMap(graphDacia, cyclesDacia, pathListDacia);
        }
    }

    private void addCyclesToMap(Graph graph, List<List<Integer>> graphCycles, List<CoordinateLine> pathListDacia) {
        for (List<Integer> graphCycle : graphCycles) {
            for (int vertexIndex = 0; vertexIndex < graphCycle.size() - 1; vertexIndex++) {
                pathListDacia.add(new CoordinateLine(
                        graph.getVertexList().get(graphCycle.get(vertexIndex)).getCenter(),
                        graph.getVertexList().get(graphCycle.get(vertexIndex + 1)).getCenter()
                ).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
            }
            pathListDacia.add(new CoordinateLine(
                    graph.getVertexList().get(graphCycle.get(0)).getCenter(),
                    graph.getVertexList().get(graphCycle.get(graphCycle.size() - 1)).getCenter()
            ).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
        }

        for (var path : pathListDacia) {
            mapView.addCoordinateLine(path);
        }
    }

    private void drawGraph(Integer graphId) {
        if (graphId == 1) {
            Graph graphExpo = new Graph(predefinedExpoMapCircleList, 1);
            drawAllEdges(graphExpo.getEdgesList(), 1);
        } else {
            Graph graphDacia = new Graph(predefinedDaciaMapCircleList, 2);
            drawAllEdges(graphDacia.getEdgesList(), 2);
        }
    }

    private void afterMapIsInitialized() {
        mapView.setZoom(ZOOM_DEFAULT);
        mapView.setCenter(coordExpo);

        for (var mapCircle : predefinedExpoMapCircleList)
            mapView.addMapCircle(mapCircle);

        for (var mapCircle : predefinedDaciaMapCircleList)
            mapView.addMapCircle(mapCircle);

        graphExpo = new Graph(predefinedExpoMapCircleList, 1);
        CycleFinder cycleFinder = new CycleFinder(graphExpo);
        cyclesExpo = cycleFinder.getAllCycles();

        graphDacia = new Graph(predefinedDaciaMapCircleList, 2);
        CycleFinder cycleFinder2 = new CycleFinder(graphDacia);
        cyclesDacia = cycleFinder2.getAllCycles();

        setControlsDisable(false);
    }
}
