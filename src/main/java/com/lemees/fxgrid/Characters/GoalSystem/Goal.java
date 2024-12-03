package com.lemees.fxgrid.Characters.GoalSystem;

import com.lemees.fxgrid.Characters.CustomCharacter;

public abstract class Goal {
    private int priority;
    protected CustomCharacter host;

    public abstract boolean canStart();

    public abstract boolean shouldStop();

    public abstract void tick();

    public abstract void start();

    public abstract void stop();

    public final int weightOf(){
        if(!canStart())
            return priority+100000;
        return priority;
    }

    public final void initGoal(int priority,CustomCharacter host){
        this.priority=priority;
        this.host=host;
    }
}
