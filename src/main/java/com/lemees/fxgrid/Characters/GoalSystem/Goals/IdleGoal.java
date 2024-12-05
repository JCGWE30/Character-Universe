package com.lemees.fxgrid.Characters.GoalSystem.Goals;

import com.lemees.fxgrid.Characters.GoalSystem.Goal;
import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.Systems.CharacterSystem;
import com.lemees.fxgrid.Systems.CoordinateSystem;

import java.util.Arrays;
import java.util.Random;

/**
 * IdleGoal causes the character to move around randomly around 1 spot
 */
public class IdleGoal extends Goal {
    private Coordinate startingTile;
    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean shouldStop() {
        return false;
    }

    @Override
    public void tick() {
        if(host.getPosition()==startingTile){
            Coordinate[] neighbors = CoordinateSystem.getNeighbors(host.getPosition());
            neighbors = Arrays.stream(neighbors).filter(c->!CharacterSystem.tileOccupied(c)).toArray(Coordinate[]::new);
            host.move(neighbors[new Random().nextInt(neighbors.length)]);
        }else{
            host.move(startingTile);
        }
    }

    @Override
    public void start() {
        startingTile=host.getPosition();
    }

    @Override
    public void stop() {

    }
}
