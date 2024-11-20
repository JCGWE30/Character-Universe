package com.lemees.fxgrid;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HelloController {
    private FXMLLoader hoverPane;
    private GridPane eventGrid;
    private Coordinate lastObservedCoordinate;
    public static int mapSize;

    @FXML
    private Pane encompassPane;
    @FXML
    private AnchorPane eventAnchor;
    @FXML
    private ScrollPane creatureScroll;
    @FXML
    private ScrollPane eventScroll;

    private double cellWidth;
    private double cellHeight;
    private Map<Coordinate, Node> cellValues = new ConcurrentHashMap<>();

    public void setGridSize(int size) {

        mapSize = size;

        hoverPane = CharacterInfoProvider.createCharacterInfo();
        encompassPane.getChildren().add(hoverPane.getRoot());

        cellWidth = eventAnchor.getPrefWidth() / size;
        cellHeight = eventAnchor.getPrefHeight() / size;

        eventGrid = new GridPane();
        eventGrid.setGridLinesVisible(true);

        eventGrid.getColumnConstraints().clear();
        eventGrid.getRowConstraints().clear();

        for (int i = 0; i < size; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(100 / size);
            eventGrid.getColumnConstraints().add(colConstraint);
        }
        for (int j = 0; j < size; j++) {
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(100 / size);
            eventGrid.getRowConstraints().add(rowConstraint);
        }

        eventGrid.setPrefSize(450, 525);

        eventAnchor.getChildren().add(eventGrid);
    }

    public void spawnRectangle(int x, int y){
        Coordinate coords = new Coordinate(x, y);

        if (cellValues.containsKey(coords)) return;

        Rectangle rect = new Rectangle();

        rect.setWidth(cellWidth);
        rect.setHeight(cellHeight);

        rect.setLayoutX(cellWidth*x);
        rect.setLayoutY(cellHeight*y);

        eventAnchor.getChildren().add(rect);

        cellValues.put(coords, rect);
    }

    public void spawnTile(Coordinate c){
        spawnTile(c.getX(),c.getY());
    }

    public void spawnTile(int x, int y) {

        Image image = new Image(getClass().getResource("/images/testImage.png").toExternalForm());

        Coordinate coords = new Coordinate(x, y);

        if (cellValues.containsKey(coords)) return;

        ImageView view = new ImageView(image);

        view.setFitWidth(cellWidth);
        view.setFitHeight(cellHeight);

        view.setLayoutX(cellWidth*x);
        view.setLayoutY(cellHeight*y);

        eventAnchor.getChildren().add(view);

        cellValues.put(coords, view);
    }

    public void deleteTile(int x, int y) {
        Coordinate coords = new Coordinate(x, y);

        if (!cellValues.containsKey(coords)) return;

        Node rec = cellValues.remove(coords);
        eventAnchor.getChildren().remove(rec);
    }

    public void deleteTile(Coordinate c) {
        deleteTile(c.getX(), c.getY());
    }

    public void deleteAllTiles() {
        for (Coordinate coords : cellValues.keySet()) {
            deleteTile(coords);
        }
    }

    private Optional<Coordinate> getHoveredCoordinate(MouseEvent e) {
        try {
            Node node = e.getPickResult().getIntersectedNode();

            if(!cellValues.values().contains(node)) return Optional.empty();

            return cellValues.entrySet().stream()
                    .filter(t -> t.getValue() == node)
                    .map(t->t.getKey())
                    .findFirst();

        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public void moveMouse(MouseEvent e) {
        AnchorPane pane = hoverPane.getRoot();
        getHoveredCoordinate(e).ifPresentOrElse(
                (c) -> {
                    pane.setVisible(true);
                    if(c==lastObservedCoordinate) return;
                    lastObservedCoordinate = c;
                    CharacterInfoProvider controler = hoverPane.getController();
                    controler.setupInfo();

                    int xOffset = e.getSceneX() > 500 ? -110 : 10;
                    int yOffset = e.getSceneY() > 500 ? -210 : 10;

                    pane.setLayoutX(e.getSceneX()+xOffset);
                    pane.setLayoutY(e.getSceneY()+yOffset);
                },
                () -> {
                    pane.setVisible(false);
                }
        );
    }
}