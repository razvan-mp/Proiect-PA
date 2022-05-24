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

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Controller {
    private static final Coordinate coordPalat = new Coordinate(47.157925, 27.586588);
    private static final Coordinate coordExpo = new Coordinate(47.186680, 27.561047);
    private static final Coordinate coordRondDacia = new Coordinate(47.166728, 27.557283);
    private static final Extent extentAllLocations = Extent.forCoordinates(coordPalat, coordExpo, coordRondDacia);
    private static final Coordinate coordNordIasi = new Coordinate(47.230760, 27.583033);
    private static final Coordinate coordSudIasi = new Coordinate(47.126273, 27.596461);
    private static final Coordinate coordVestIasi = new Coordinate(47.175779, 27.499281);
    private static final Coordinate coordEstIasi = new Coordinate(47.168813, 27.670672);
    private static final Extent extindereIasi = Extent.forCoordinates(coordNordIasi, coordSudIasi, coordVestIasi, coordEstIasi);
    private static final int ZOOM_DEFAULT = 14;
    final List<CoordinateLine> pathList = new ArrayList<>();
    final List<Coordinate> predefinedPointsList = new ArrayList<>();
    final List<MapCircle> predefinedMapCircleList = new ArrayList<>();
    private Marker markerExpo;

    private Marker markerPalat;

    private Marker markerRondDacia;

    private Marker markerClick;

    @FXML
    private CheckBox checkExpoMarker;

    @FXML
    private CheckBox checkRondDaciaMarker;

    @FXML
    private Button buttonExpo;

    @FXML
    private Button buttonRondDacia;

    @FXML
    private Button buttonZoom;

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
    private Button buttonAllLocations;

    @FXML
    private Label labelCenter;

    @FXML
    private Label labelExtent;

    @FXML
    private Label labelZoom;

    @FXML
    private Label labelEvent;

    @FXML
    private CheckBox checkClickMarker;

    private CoordinateLine polygonLine;

    @FXML
    private CheckBox checkDrawPolygon;

    @FXML
    private CheckBox checkConstrainIasi;

    public Controller() {
        addAllPointsList();
        addMarkersAndLabels();
        addMapCircles();
    }

    private void addAllPointsList() {
        try (CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/csv/expo.csv"))){
            String[] csvLine;
            while((csvLine = csvReader.readNext()) != null){
                this.predefinedPointsList.add(new Coordinate(Double.parseDouble(csvLine[0]),
                        Double.parseDouble(csvLine[1])));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        try (CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/csv/rondDacia.csv"))){
            String[] csvLine;
            while((csvLine = csvReader.readNext()) != null){
                this.predefinedPointsList.add(new Coordinate(Double.parseDouble(csvLine[0]),
                        Double.parseDouble(csvLine[1])));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMarkersAndLabels() {
        markerExpo = Marker.createProvided(Marker.Provided.BLUE).setPosition(coordExpo).setVisible(false);
        markerPalat = Marker.createProvided(Marker.Provided.GREEN).setPosition(coordPalat).setVisible(false);
        markerRondDacia = Marker.createProvided(Marker.Provided.RED).setPosition(coordRondDacia).setVisible(false);
        markerClick = Marker.createProvided(Marker.Provided.ORANGE).setVisible(false);

        MapLabel labelExpo = new MapLabel("expo", 10, -10).setVisible(false).setCssClass("green-label");
        MapLabel labelPalat = new MapLabel("palat", 10, -10).setVisible(false).setCssClass("green-label");
        MapLabel labelRondDacia = new MapLabel("rond Dacia", 10, -10).setVisible(false).setCssClass("red-label");
        MapLabel labelClick = new MapLabel("click!", 10, -10).setVisible(false).setCssClass("orange-label");

        markerExpo.attachLabel(labelExpo);
        markerPalat.attachLabel(labelPalat);
        markerRondDacia.attachLabel(labelRondDacia);
        markerClick.attachLabel(labelClick);
    }

    private void addMapCircles() {
        for (var point : predefinedPointsList)
            predefinedMapCircleList.add(new MapCircle(point, 10).setVisible(true).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE));

    }

    public void initMapAndControls(Projection projection) {
        // init MapView-Cache
        final OfflineCache offlineCache = mapView.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "/mapjfx-cache";
        try {
            Files.createDirectories(Paths.get(cacheDir));
            offlineCache.setCacheDirectory(cacheDir);
            offlineCache.setActive(true);
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

        // set the custom css file for the MapView
        mapView.setCustomMapviewCssURL(Objects.requireNonNull(getClass().getResource("/custom_mapview.css")));

        leftControls.setExpandedPane(optionsLocations);

        // set the controls to disabled, this will be changed when the MapView is intialized
        setControlsDisable(true);

        // wire up the location buttons
        buttonExpo.setOnAction(event -> mapView.setCenter(coordExpo));
        buttonRondDacia.setOnAction(event -> mapView.setCenter(coordRondDacia));

        buttonAllLocations.setOnAction(event -> mapView.setExtent(extentAllLocations));

        // wire the zoom button and connect the slider to the map's zoom
        buttonZoom.setOnAction(event -> mapView.setZoom(ZOOM_DEFAULT));
        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty());

        // bind the map's center and zoom properties to the corresponding labels and format them
        labelCenter.textProperty().bind(Bindings.format("center: %s", mapView.centerProperty()));
        labelZoom.textProperty().bind(Bindings.format("zoom: %.0f", mapView.zoomProperty()));

        // watch the MapView's initialized property to finish initialization
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });

        MapType mapType = MapType.OSM;
        mapView.setMapType(mapType);
        setupEventHandlers();

        // add the graphics to the checkboxes
        checkExpoMarker.setGraphic(
                new ImageView(new Image(markerExpo.getImageURL().toExternalForm(), 16.0, 16.0, true, true)));
        checkRondDaciaMarker.setGraphic(
                new ImageView(new Image(markerRondDacia.getImageURL().toExternalForm(), 16.0, 16.0, true, true)));
        checkClickMarker.setGraphic(
                new ImageView(new Image(markerClick.getImageURL().toExternalForm(), 16.0, 16.0, true, true)));

        // bind the checkboxes to the markers visibility
        checkExpoMarker.selectedProperty().bindBidirectional(markerExpo.visibleProperty());
        checkRondDaciaMarker.selectedProperty().bindBidirectional(markerRondDacia.visibleProperty());
        checkClickMarker.selectedProperty().bindBidirectional(markerClick.visibleProperty());

        // add the polygon check handler
        ChangeListener<Boolean> polygonListener =
                (observable, oldValue, newValue) -> {
                    if (!newValue && polygonLine != null) {
                        mapView.removeCoordinateLine(polygonLine);
                        polygonLine = null;
                    }
                };
        checkDrawPolygon.selectedProperty().addListener(polygonListener);

        // add the constrain listener
        checkConstrainIasi.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                mapView.constrainExtent(extindereIasi);
            } else {
                mapView.clearConstrainExtent();
            }
        }));

        // finally initialize the map view
        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());
    }

    private void setupEventHandlers() {
        // add an event handler for singleclicks, set the click marker to the new position when it's visible
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            final Coordinate newPosition = event.getCoordinate().normalize();
            labelEvent.setText("Event: map clicked at: " + newPosition);
            if (checkDrawPolygon.isSelected()) {
                handlePolygonClick(event);
            }
            if (markerClick.getVisible()) {
                final Coordinate oldPosition = markerClick.getPosition();
                if (oldPosition != null) {
                    animateClickMarker(oldPosition, newPosition);
                } else {
                    markerClick.setPosition(newPosition);
                    // adding can only be done after coordinate is set
                    mapView.addMarker(markerClick);
                }
            }
        });

        // add an event handler for MapViewEvent#MAP_EXTENT and set the extent in the map
        mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            event.consume();
            mapView.setExtent(event.getExtent());
        });

        // add an event handler for extent changes and display them in the status label
        mapView.addEventHandler(MapViewEvent.MAP_BOUNDING_EXTENT, event -> {
            event.consume();
            labelExtent.setText(event.getExtent().toString());
        });

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: map right clicked at: " + event.getCoordinate());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker right clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label clicked: " + event.getMapLabel().getText());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label right clicked: " + event.getMapLabel().getText());
        });

    }

    private void animateClickMarker(Coordinate oldPosition, Coordinate newPosition) {
        // animate the marker to the new position
        final Transition transition = new Transition() {
            private final Double oldPositionLongitude = oldPosition.getLongitude();
            private final Double oldPositionLatitude = oldPosition.getLatitude();
            private final double deltaLatitude = newPosition.getLatitude() - oldPositionLatitude;
            private final double deltaLongitude = newPosition.getLongitude() - oldPositionLongitude;

            {
                setCycleDuration(Duration.seconds(1.0));
                setOnFinished(evt -> markerClick.setPosition(newPosition));
            }

            @Override
            protected void interpolate(double v) {
                final double latitude = oldPosition.getLatitude() + v * deltaLatitude;
                final double longitude = oldPosition.getLongitude() + v * deltaLongitude;
                markerClick.setPosition(new Coordinate(latitude, longitude));
            }
        };
        transition.play();
    }

    private void handlePolygonClick(MapViewEvent event) {
//        final List<Coordinate> coordinates = new ArrayList<>();
//        if (polygonLine != null) {
//            polygonLine.getCoordinateStream().forEach(coordinates::add);
//            mapView.removeCoordinateLine(polygonLine);
//            polygonLine = null;
//        }
//        coordinates.add(event.getCoordinate());
//        polygonLine = new CoordinateLine(coordinates)
//            .setColor(Color.DODGERBLUE)
//            .setFillColor(Color.web("lawngreen", 0.4))
//            .setClosed(true);
//        point = new MapCircle(event.getCoordinate(), 10).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE);
//        point.setVisible(true);
//        mapView.addMapCircle(point);
//        point.setVisible(true);
//        mapView.addCoordinateLine(polygonLine);
//        polygonLine.setVisible(true);
    }

    private void setControlsDisable(boolean flag) {
        topControls.setDisable(flag);
        leftControls.setDisable(flag);
    }

    private void afterMapIsInitialized() {
        // start at the harbour with default zoom
        mapView.setZoom(ZOOM_DEFAULT);
        mapView.setCenter(coordExpo);

        // add the markers to the map - they are still invisible
        mapView.addMarker(markerExpo);
        mapView.addMarker(markerPalat);
        mapView.addMarker(markerRondDacia);

        for (var mapCircle : predefinedMapCircleList)
            mapView.addMapCircle(mapCircle);

        // now enable the controls
        setControlsDisable(false);
//
//        GraphHelper.getCycles(predefinedMapCircleList);

//        pathList.add(new CoordinateLine(
//                predefinedMapCircleList.get(0).getCenter(),
//                predefinedMapCircleList.get(1).getCenter()
//        ).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
//
//        pathList.add(new CoordinateLine(
//                predefinedMapCircleList.get(0).getCenter(),
//                predefinedMapCircleList.get(3).getCenter()
//        ).setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
//
//       for (var path : pathList)
//           mapView.addCoordinateLine(path);

        // function to print the cycles
//        printCycles(edges, mark);

        Graph graph = new Graph(predefinedMapCircleList);
//        List<Edge> edges = graph.getEdgesList();

//        CycleFinder cycleFinder = new CycleFinder(graph);
//        List<List<Integer>> graphCycles = cycleFinder.getAllCyclesOfLength(3);

        CycleFinder cycleFinder = new CycleFinder(graph);
        List<List<Integer>> graphCycles = cycleFinder.getAllCyclesOfLength(500);
        System.out.println(graphCycles);

        List<Color> colorList = new ArrayList<>();
        colorList.add(Color.DODGERBLUE);
        colorList.add(Color.BLACK);
        colorList.add(Color.GREEN);
        colorList.add(Color.YELLOW);
        colorList.add(Color.DARKTURQUOISE);
        colorList.add(Color.DODGERBLUE);
        colorList.add(Color.BLACK);
        colorList.add(Color.GREEN);
        colorList.add(Color.YELLOW);
        colorList.add(Color.DARKTURQUOISE);
        colorList.add(Color.DODGERBLUE);
        colorList.add(Color.BLACK);
        colorList.add(Color.GREEN);
        colorList.add(Color.YELLOW);
        colorList.add(Color.DARKTURQUOISE);
        colorList.add(Color.DODGERBLUE);
        colorList.add(Color.BLACK);
        colorList.add(Color.GREEN);
        colorList.add(Color.YELLOW);
        colorList.add(Color.DARKTURQUOISE);
        colorList.add(Color.DODGERBLUE);
        colorList.add(Color.BLACK);
        colorList.add(Color.GREEN);
        colorList.add(Color.YELLOW);
        colorList.add(Color.DARKTURQUOISE);
        colorList.add(Color.DODGERBLUE);
        colorList.add(Color.BLACK);
        colorList.add(Color.GREEN);
        colorList.add(Color.YELLOW);
        colorList.add(Color.DARKTURQUOISE);

        for (int cycleIndex = 0; cycleIndex < graphCycles.size(); cycleIndex++) {
            for (int vertexIndex = 0; vertexIndex < graphCycles.get(cycleIndex).size() - 1; vertexIndex++) {
                pathList.add(new CoordinateLine(
                        graph.getVertexList().get(graphCycles.get(cycleIndex).get(vertexIndex)).getCenter(),
                        graph.getVertexList().get(graphCycles.get(cycleIndex).get(vertexIndex + 1)).getCenter()
                ).setColor(colorList.get(cycleIndex)).setFillColor(colorList.get(cycleIndex)).setVisible(true));
            }
            pathList.add(new CoordinateLine(
                    graph.getVertexList().get(graphCycles.get(cycleIndex).get(0)).getCenter(),
                    graph.getVertexList().get(graphCycles.get(cycleIndex).get(graphCycles.get(cycleIndex).size() - 1)).getCenter()
            ).setColor(colorList.get(cycleIndex)).setFillColor(colorList.get(cycleIndex)).setVisible(true));
        }

        for (var path : pathList) {
            mapView.addCoordinateLine(path);
        }


//        CycleFinder cycleFinder = new CycleFinder(graph);
//        cycleFinder.printCycles(graph.getEdgesList().size());

//        for(var edge : edges)
//        {
//            pathList.add(new CoordinateLine(
//                edge.getSrc().getCenter(),
//                edge.getDest().getCenter())
//                    .setColor(Color.DODGERBLUE).setFillColor(Color.DODGERBLUE).setVisible(true));
//        }
//        for (var path : pathList)
//           mapView.addCoordinateLine(path);
    }

//    private Optional<CoordinateLine> loadCoordinateLine(URL url) {
//        try (
//                Stream<String> lines = new BufferedReader(
//                        new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)).lines()
//        ) {
//            return Optional.of(new CoordinateLine(
//                    lines.map(line -> line.split(";")).filter(array -> array.length == 2)
//                            .map(values -> new Coordinate(Double.valueOf(values[0]), Double.valueOf(values[1])))
//                            .collect(Collectors.toList())));
//        } catch (IOException | NumberFormatException e) {
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }
}
