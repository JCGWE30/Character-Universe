package com.lemees.fxgrid.Characters;

import com.lemees.fxgrid.Characters.GoalSystem.Goal;
import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.GridController;
import com.lemees.fxgrid.Systems.CharacterSystem;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public abstract class CustomCharacter implements VictoryCondition {
    public FXMLLoader dropdown;

    private String name;
    private String imagePath;
    private Image image;
    private List<Goal> goals = new ArrayList<>();
    private Goal activeGoal;

    protected Coordinate currentPosition;
    protected double health;
    protected int kills;

    public CustomCharacter(String name,String imagePath){
        this.currentPosition = new Coordinate(0,0);
        this.name=name;
        this.imagePath=imagePath;
        this.health=10;

        this.image = new Image(getClass().getResource(getImage()).toExternalForm());
    }

    final public void move(Coordinate newCoordinate){
        currentPosition=newCoordinate;
    }

    public final String getImage(){
        return "/images/"+imagePath;
    }

    public final Image getLoadedImage(){
        return image;
    }

    public final Coordinate getPosition(){
        return currentPosition;
    }

    public final double getDistance(CustomCharacter c){
        return getPosition().getDistance(c.getPosition());
    }

    public final String getName(){
        return name;
    }

    public final boolean isAlive(){
        return health > 0;
    }

    public final int getKills(){
        return kills;
    }

    public final void turnTick(){
        if(activeGoal!=null&&activeGoal.shouldStop())
            activeGoal=null;
        PriorityQueue<Goal> tempGoals = new PriorityQueue<>(Comparator.comparingInt(Goal::weightOf).reversed());
        tempGoals.addAll(goals);
        for(Goal goal:tempGoals){
            if(goal.canStart()){
                activeGoal=goal;
                break;
            }
        }
        activeGoal.tick();
    }

    protected void addGoal(Goal goal){
        goals.add(goal);
    }

    protected void attack(CustomCharacter character){
        character.defend(this,10);
        GridController.addEventString(getName()+" Attacks "+character.getName()+" for 10 damage!");
        kills++;
    }

    protected void defend(CustomCharacter character,double damage){
        this.health-=damage;
        if(health<=0){
            CharacterSystem.killCharacter(this);
        }
    }
}
