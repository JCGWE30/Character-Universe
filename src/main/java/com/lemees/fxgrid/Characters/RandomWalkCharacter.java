package com.lemees.fxgrid.Characters;

import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.Systems.CharacterSystem;
import com.lemees.fxgrid.Systems.CoordinateSystem;

import java.util.Arrays;
import java.util.Random;

public abstract class RandomWalkCharacter extends CustomCharacter{
    public RandomWalkCharacter(String name, String imagePath) {
        super(name, imagePath);
    }

    @Override
    public void turnTick() {
        Coordinate[] coord = CoordinateSystem.getNighbors(currentPosition);
        for(Coordinate neighbor : coord){
            CustomCharacter cust = CharacterSystem.getCharacterOnTile(neighbor);
            if(cust!=null&&shouldAttack(cust)){
                if(isAlive())
                    attack(cust);
                return;
            }
        }
        coord = Arrays.stream(coord)
                .filter(this::obstacleFilter)
                .toArray(Coordinate[]::new);
        if(coord.length==0)
            return;
        move(coord[new Random().nextInt(coord.length)]);
    }

    protected boolean shouldAttack(CustomCharacter cust){
        return true;
    }

    private boolean obstacleFilter(Coordinate c){
        return !CharacterSystem.tileOccupied(c);
    }
}
