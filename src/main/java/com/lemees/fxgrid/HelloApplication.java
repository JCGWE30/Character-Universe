package com.lemees.fxgrid;

import com.lemees.fxgrid.Characters.CharacterClasses.Onryo;
import com.lemees.fxgrid.Characters.CharacterClasses.Spirit;
import com.lemees.fxgrid.Characters.CharacterLoader;
import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Systems.CharacterSystem;
import com.lemees.fxgrid.Systems.CoordinateSystem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloApplication extends Application {
    public static IntegerProperty gameSpeed;
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

        CharacterLoader.loadCharacters();
        CharacterSystem.initialize();

        for(Map.Entry<Class<? extends CustomCharacter>,Integer> clazz : CharacterLoader.characterList.entrySet()){
            try {
                for(int i = 0;i<clazz.getValue();i++){
                    CharacterSystem.spawnCharacter(clazz.getKey().getDeclaredConstructor().newInstance());
                }
            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
                }
        }

        for(CustomCharacter c:CharacterSystem.getLivingCharacters()){
            controller.spawnCharacter(c);
        }

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1),e->{
                    controller.deleteAllTiles();

                    for(CustomCharacter c:CharacterSystem.getLivingCharacters()){
                        c.turnTick();
                        controller.spawnCharacter(c);
                    }

                    controller.updateEvents();
                    controller.updateDropdowns();
                    CharacterSystem.checkForVictory(controller);
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        gameSpeed=new SimpleIntegerProperty(1);

        gameSpeed.addListener((observableValue, number, t1) -> {
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.0/t1.intValue()),e->{

                controller.deleteAllTiles();

                for(CustomCharacter c:CharacterSystem.getLivingCharacters()){
                    c.turnTick();
                }
                for(CustomCharacter c:CharacterSystem.getLivingCharacters()){
                    controller.spawnCharacter(c);
                }

                controller.updateEvents();
                controller.updateDropdowns();
                CharacterSystem.checkForVictory(controller);
            }));
            timeline.playFromStart();
        });


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