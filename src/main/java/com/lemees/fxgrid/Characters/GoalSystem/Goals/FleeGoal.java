package com.lemees.fxgrid.Characters.GoalSystem.Goals;

import com.lemees.fxgrid.Characters.GoalSystem.Goal;
import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.Systems.CharacterSystem;
import com.lemees.fxgrid.Systems.CoordinateSystem;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * FleeGoal will cause a character to run from their attacker if their health is low
 */
public class FleeGoal extends Goal {
    private int healthThreshold = 20;
    private int fleeTiles = 2;
    private int range = 20;

    /**
     * @param healthThreshold if a characters health is below this number they will start fleeing
     * @param fleeTiles how many tiles a character can move at a time while fleeing
     * @param range how many tiles the character will run for before calming down
     */
    public FleeGoal(int healthThreshold, int fleeTiles, int range){
        this.fleeTiles=fleeTiles;
        this.healthThreshold=healthThreshold;
        this.range=range;
    }

    public FleeGoal(){}

    @Override
    public boolean canStart() {
        return host.getHealth()<healthThreshold&&tooClose();
    }

    @Override
    public boolean shouldStop() {
        return host.getHealth()>healthThreshold&&tooClose();
    }

    private boolean tooClose(){
        if(!host.getLastAttackedBy().isAlive()) return false;
        return host.getDistance(host.getLastAttackedBy())<10;
    }

    @Override
    public void tick() {
        for(int i = 0;i<fleeTiles;i++) {
            PriorityQueue<Coordinate> neighbors = new PriorityQueue<>(Comparator.comparingDouble(c -> ((Coordinate) c).getDistance(host.getLastAttackedBy().getPosition())).reversed());
            neighbors.addAll(Arrays.stream(CoordinateSystem.getNeighbors(host.getPosition())).filter(t -> !CharacterSystem.tileOccupied(t)).toList());
            host.move(neighbors.poll());
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
