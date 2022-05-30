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
package map;

import algorithm.CycleFinder;
import algorithm.Graph;
import algorithm.GraphHelper;
import com.sothawo.mapjfx.*;
import entities.NodesEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import repos.NodesRepo;
import utilities.Info;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static utilities.Info.COORD_ROND_DACIA;
import static utilities.Info.EXTINDERE_IASI;

/**
 * Controller class for the <code>MVC</code> model, uses <code>JavaFX</code>
 */
public class Controller {
    private static final List<CoordinateLine> drawnCycles = new ArrayList<>();
    private static final int ZOOM_DEFAULT = 16;
    private static Graph graphExpo;
    private static Graph graphDacia;
    private static List<List<Integer>> cyclesExpo;
    private static List<List<Integer>> cyclesDacia;
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

    /**
     * Initializes the color and <code>MapCircle</code> lists
     */
    public Controller() {
        addColors();
        addAllPointsList();
        addMapCircles();
    }

    /**
     * Adds colors to the <code>colorList</code> list to be used for drawing cycles
     */
    private void addColors() {
        colorList = List.of(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.BLUE, Color.INDIGO, Color.VIOLET, Color.HOTPINK, Color.DARKTURQUOISE, Color.SALMON,
                Color.GRAY, Color.BLACK, Color.DARKCYAN, Color.DARKSEAGREEN);
    }

    /**
     * Adds all points from the DB to the predefined point lists
     */
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

    /**
     * Adds the <code>MapCircle</code>s to the predefined lists, in order to be drawn on the map
     */
    private void addMapCircles() {
        for (var point : predefinedPointsListExpo)
            predefinedExpoMapCircleList.add(new MapCircle(point, 10).setVisible(true).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE));

        for (var point : predefinedPointsListDacia)
            predefinedDaciaMapCircleList.add(new MapCircle(point, 10).setVisible(true).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE));
    }

    /**
     * Initializes the map and its controls (buttons and input field) in order to create the app window
     * @param projection
     */
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
            mapView.setCenter(Info.COORD_EXPO);
            deleteGraph(2);
            drawAllCycles(1);
        });

        buttonRondDacia.setOnAction(event -> {
            location = 2;
            mapView.setCenter(COORD_ROND_DACIA);
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
                mapView.constrainExtent(EXTINDERE_IASI);
            } else {
                mapView.clearConstrainExtent();
            }
        }));

        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());
    }

    /**
     * Utility method to call the <code>createCycles</code> method with the appropriate parameters
     * @param length the length of the cycle to be drawn on the map
     */
    private void filterCycles(double length) {
        Integer counter = 0;
        if (location == 1) {
            createCycles(length, counter, pathListExpo, cyclesExpo);
        } else {
            createCycles(length, counter, pathListDacia, cyclesDacia);
        }
    }

    /**
     * Creates the cycles and calls the <code>drawCycle</code> method with the appropriate parameters
     * @param length length of the cycle to be drawn
     * @param counter cycle ID
     * @param pathList list of <code>JavaFX</code> <code>CoordinateLine</code>s to be drawn
     * @param cycleList list of cycles to be filtered and drawn
     */
    private void createCycles(double length, Integer counter, List<CoordinateLine> pathList, List<List<Integer>> cycleList) {
        for (var path : pathList)
            path.setVisible(false);

        for (CoordinateLine drawnPath : drawnCycles)
            drawnPath.setVisible(false);

        for (var cycle : cycleList)
            if (cycleLengthIsWithin(length, cycle)) {
                drawCycle(cycle, counter);
                counter++;
            }
    }

    /**
     * Adds the <code>CoordinateLine</code>s with respect to the generated cycles to the <code>mapView</code>
     * @param cycle cycle to be added to the <code>mapView</code>
     * @param count cycle ID
     */
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

    /**
     * Checks if the current cycle is within the specified length
     * @param length path length constraint
     * @param cycle cycle to be verified
     * @return <code>true</code> if the cycle length is within the constraint, <code>false</code> otherwise
     */
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

    /**
     * Hides a graph from the <code>mapView</code>
     * @param graphId ID of the graph to be hid
     */
    private void deleteGraph(Integer graphId) {
        if (graphId == 1) {
            for (var path : pathListExpo)
                path.setVisible(false);
        } else {
            for (var path : pathListDacia)
                path.setVisible(false);
        }
    }

    /**
     * Enables or disables the map controls based on the <code>flag</code> parameter
     * @param flag <code>true</code> if the controls should be enabled, <code>false</code> otherwise
     */
    private void setControlsDisable(boolean flag) {
        topControls.setDisable(flag);
        leftControls.setDisable(flag);
    }

    /**
     * Draws all the cycles from the graph with <code>graphId</code> when the respective button is clicked
     * @param graphId graph's ID
     */
    private void drawAllCycles(int graphId) {
        if (graphId == 1) {
            addCyclesToMap(graphExpo, cyclesExpo, pathListExpo);
        } else {
            addCyclesToMap(graphDacia, cyclesDacia, pathListDacia);
        }
    }

    /**
     * Sets all the cycles from the graph to be visible on the <code>mapView</code>
     * @param graph graph to be drawn
     * @param graphCycles cycles to be drawn
     * @param pathList list of paths that constitute the predefined lines
     */
    private void addCyclesToMap(Graph graph, List<List<Integer>> graphCycles, List<CoordinateLine> pathList) {
        for (List<Integer> graphCycle : graphCycles) {
            for (int vertexIndex = 0; vertexIndex < graphCycle.size() - 1; vertexIndex++) {
                pathList.add(new CoordinateLine(
                        graph.getVertexList().get(graphCycle.get(vertexIndex)).getCenter(),
                        graph.getVertexList().get(graphCycle.get(vertexIndex + 1)).getCenter()
                ).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
            }
            pathList.add(new CoordinateLine(
                    graph.getVertexList().get(graphCycle.get(0)).getCenter(),
                    graph.getVertexList().get(graphCycle.get(graphCycle.size() - 1)).getCenter()
            ).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
        }

        for (var path : pathList) {
            mapView.addCoordinateLine(path);
        }
    }

    /**
     * Creates and initializes the predefined lists after the <code>mapView</code> is initialized
     */
    private void afterMapIsInitialized() {
        mapView.setZoom(ZOOM_DEFAULT);
        mapView.setCenter(Info.COORD_EXPO);

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
