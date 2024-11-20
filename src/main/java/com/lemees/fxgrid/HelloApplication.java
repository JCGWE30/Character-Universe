package com.lemees.fxgrid;

import com.lemees.fxgrid.Systems.CoordinateSystem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = loader.load();

        // Get the controller
        HelloController controller = loader.getController();

        int gridSize = 25;

        // Call the method on the controller
        controller.setGridSize(gridSize);

        //x,y
        HashMap<Integer,Integer> takenSpots = new HashMap<>();

        Random random = new Random();

        Stack<Coordinate> pathCoords = CoordinateSystem.getPath(new Coordinate(0,0),new Coordinate(10,11));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(200),e->{
                    controller.deleteAllTiles();
                    controller.spawnTile(pathCoords.pop());
//                    for (int i = 0; i < 100; i++) {
//                        Map.Entry<Integer, Integer> selectedSpot = null;
//                        while (takenSpots.entrySet().contains(selectedSpot) || selectedSpot == null) {
//                            selectedSpot = Map.entry(random.nextInt(gridSize), random.nextInt(gridSize));
//                        }
//                        controller.spawnTile(selectedSpot.getKey(), selectedSpot.getValue());
//                        takenSpots.put(selectedSpot.getKey(), selectedSpot.getValue());
//                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Set up the scene and stage
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Chacter Universe");

        scene.setOnMouseMoved(controller::moveMouse);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void sleep(long l){
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}