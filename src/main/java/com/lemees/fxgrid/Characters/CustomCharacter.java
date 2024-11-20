package com.lemees.fxgrid.Characters;

import com.lemees.fxgrid.Coordinate;

public class CustomCharacter {
    private String name;
    private String imagePath;
    private Coordinate currentPosition;

    protected double health;

    protected CustomCharacter(String name,String imagePath){
        this.currentPosition = new Coordinate(0,0);
        this.name=name;
        this.imagePath=imagePath;
    }

    final public void move(Coordinate newCoordinate){

    }

    public final Coordinate getPosition(){
        return currentPosition;
    }

    protected void turnTick(){

    }

    protected void attack(CustomCharacter character){
        character.defend(this,10);
    }

    protected void defend(CustomCharacter character,double damage){

    }
}
