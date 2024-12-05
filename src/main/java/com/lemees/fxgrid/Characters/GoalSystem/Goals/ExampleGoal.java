package com.lemees.fxgrid.Characters.GoalSystem.Goals;

import com.lemees.fxgrid.Characters.GoalSystem.Goal;
import com.lemees.fxgrid.GridController;

//Extends off of goal
public class ExampleGoal extends Goal {
    @Override
    public boolean canStart() {
        return Math.random()<0.1; //Give this goal a 10% chance to start
    }

    @Override
    public boolean shouldStop() {
        return Math.random()<0.01; //Give this goal a 1% chance to stop;
    }

    @Override
    public void tick() {
        GridController.addEventString("Hey whats up guys"); //Prints this message to console every tick
    }

    @Override
    public void start() {
        GridController.addEventString("Yay im starting"); //Says this when the goal starts up
    }

    @Override
    public void stop() {
        GridController.addEventString("Aw im ending"); // Says this when the goal shuts down
    }
}
