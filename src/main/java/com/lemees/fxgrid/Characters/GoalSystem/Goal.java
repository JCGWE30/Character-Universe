package com.lemees.fxgrid.Characters.GoalSystem;

import com.lemees.fxgrid.Characters.CustomCharacter;

import java.util.Objects;

public abstract class Goal {
    private int priority;
    protected CustomCharacter host;

    /**
     * Checks if the goal is able to begin. If the goal has a higher priority then the active goal it will override it
     * @return if the goal should begin
     */
    public abstract boolean canStart();

    /**
     * Checks if the goal should stop
     * @return if the goal should stop
     */
    public abstract boolean shouldStop();

    /**
     * Runs the every tick its active
     */
    public abstract void tick();

    /**
     * Runs the first tick the goal activates, before {@link Goal#tick()}
     */
    public abstract void start();

    /**
     * Runs the last tick the goal is after, after {@link Goal#tick()}
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return priority == goal.priority && Objects.equals(host, goal.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority, host);
    }
}
