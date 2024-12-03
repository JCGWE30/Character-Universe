package com.lemees.fxgrid;

import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Systems.CharacterSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GridController {
    private static GridController instance;

    private FXMLLoader hoverPane;
    private GridPane eventGrid;
    private Coordinate lastObservedCoordinate;
    private List<String> eventLog = new ArrayList<>();

    public static int mapSize;

    @FXML
    private Pane encompassPane;
    @FXML
    private AnchorPane eventAnchor;
    @FXML
    private VBox creatureScroll;
    @FXML
    private VBox eventScroll;
    @FXML
    private ScrollPane eventScrollPane;

    @FXML
    private Button pauseButton;
    @FXML
    private Button slowButton;
    @FXML
    private Button fastButton;
    @FXML
    private Label speedDisplay;

    private double cellWidth;
    private double cellHeight;

    private int actualSpeed = 1;
    private boolean isPaused = false;

    private Map<Coordinate, Node> cellValues = new ConcurrentHashMap<>();

    private void updateSpeed(int speed,boolean paused){
        if(paused){
            if(isPaused){
                pauseButton.setText("Pause");
                GridApplication.gameSpeed.set(actualSpeed);
            }else{
                pauseButton.setText("Play");
                GridApplication.gameSpeed.set(0);
            }
            isPaused=!isPaused;
            return;
        }
        actualSpeed+=speed;
        actualSpeed = Math.max(1,Math.min(actualSpeed,5));
        GridApplication.gameSpeed.set(actualSpeed);
        speedDisplay.setText("x"+actualSpeed);
    }

    public void setGridSize(int size) {
        pauseButton.setOnAction(_ -> {
            updateSpeed(0,true);
        });
        slowButton.setOnAction(_ -> {
            updateSpeed(-1,false);
        });
        fastButton.setOnAction(_ -> {
            updateSpeed(1,false);
        });

        instance = this;

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

    public void spawnCharacter(CustomCharacter character){
        Image image = new Image(getClass().getResource(character.getImage()).toExternalForm());

        Coordinate coords = character.getPosition();

        if (cellValues.containsKey(coords)) return;

        ImageView view = new ImageView(image);

        view.setFitWidth(cellWidth);
        view.setFitHeight(cellHeight);

        view.setLayoutX(cellWidth*coords.getX());
        view.setLayoutY(cellHeight*coords.getY());

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

    public static void addEventString(String event){
        instance.eventLog.add(event);
    }

    public void updateEvents(){
        eventScroll.getChildren().clear();
        for(String st:eventLog){
            Label label = new Label();
            label.setText(st);
            eventScroll.getChildren().add(label);
        }
        eventScrollPane.setVvalue(1.0);
    }

    public void updateDropdowns(){
        creatureScroll.getChildren().clear();

        List<FXMLLoader> panes = CharacterSystem.getCharacters().stream()
                .sorted(Comparator.comparingInt((c)-> c.isAlive() ? 1000 + c.getKills() : c.getKills()))
                .map(c->c.dropdown)
                .toList().reversed();

        for(FXMLLoader pane:panes){
            AnchorPane root = pane.getRoot();
            CharacterDropdownProvider prov = pane.getController();
            prov.updateStats();
            creatureScroll.getChildren().add(root);
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

                    CustomCharacter character = CharacterSystem.getCharacterOnTile(c);

                    controler.setupInfo(character);

                    int xOffset = e.getSceneX() > 400 ? -110 : 10;
                    int yOffset = e.getSceneY() > 400 ? -210 : 10;

                    pane.setLayoutX(e.getSceneX()+xOffset);
                    pane.setLayoutY(e.getSceneY()+yOffset);
                },
                () -> {
                    pane.setVisible(false);
                }
        );
    }
}