package com.lemees.fxgrid.Characters;

import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.GridController;
import com.lemees.fxgrid.Systems.CharacterSystem;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;


public abstract class CustomCharacter implements VictoryCondition {
    public FXMLLoader dropdown;

    private String name;
    private String imagePath;
    private Image image;

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

    public final String getName(){
        return name;
    }

    public final boolean isAlive(){
        return health > 0;
    }

    public final int getKills(){
        return kills;
    }

    public abstract void turnTick();

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
