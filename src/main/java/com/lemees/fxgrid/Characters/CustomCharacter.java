package com.lemees.fxgrid.Characters;

import com.lemees.fxgrid.Characters.GoalSystem.Goal;
import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.GridController;
import com.lemees.fxgrid.Systems.CharacterSystem;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

import java.util.*;


public abstract class CustomCharacter implements VictoryCondition {
    public FXMLLoader dropdown;

    private String name;
    private String imagePath;
    private Image image;
    private List<Goal> goals = new ArrayList<>();

    protected Goal activeGoal;
    protected String[] customInfoFields = new String[3];
    protected CustomCharacter lastAttackedBy;
    protected CustomCharacter lastAttacked;
    protected Coordinate currentPosition;
    protected double health;
    protected int kills;

    public CustomCharacter(String name, String imagePath) {
        this.currentPosition = new Coordinate(0, 0);
        this.name = name;
        this.imagePath = imagePath;
        this.health = 10;

        this.image = new Image(getClass().getResource(getImage()).toExternalForm());
    }

    final public void move(Coordinate newCoordinate) {
        currentPosition = newCoordinate;
    }

    public final String getImage() {
        return "/images/" + imagePath;
    }

    public final Image getLoadedImage() {
        return image;
    }

    public final Coordinate getPosition() {
        return currentPosition;
    }

    public final double getDistance(CustomCharacter c) {
        return getPosition().getDistance(c.getPosition());
    }

    public final String getName() {
        return name;
    }

    public final boolean isAlive() {
        return health > 0;
    }

    public final int getKills() {
        return kills;
    }


    public final double getHealth() {
        return health;
    }

    public final CustomCharacter getLastAttackedBy() {
        return lastAttackedBy;
    }

    public final Optional<String>[] getOptionalCustomFields() {
        return Arrays.stream(customInfoFields)
                .map(Optional::ofNullable)
                .toArray(Optional[]::new);
    }

    /**
     * Adds a goal to the character AI
     * The order goals are added determines priority.
     * The eariler a goal is added the higher priority it will have
     *
     * @param goal The goal to add
     */
    protected final void addGoal(Goal goal) {
        goal.initGoal(goals.size(), this);
        goals.add(goal);
    }

    public final void turnTick() {
        Goal previousGoal = activeGoal;
        boolean goalChanged = false;
        if (activeGoal != null && activeGoal.shouldStop())
            activeGoal = null;

        int activeWeight = activeGoal == null ? Integer.MAX_VALUE : activeGoal.weightOf();

        PriorityQueue<Goal> tempGoals = new PriorityQueue<>(Comparator.comparingInt(Goal::weightOf).reversed());
        tempGoals.addAll(goals);

        for (Goal goal : tempGoals) {
            if (goal.canStart() && goal.weightOf() < activeWeight) {
                activeGoal = goal;
                goalChanged = !activeGoal.equals(previousGoal);
                break;
            }
        }
        if (activeGoal == null) return;

        if (goalChanged) {
            activeGoal.start();
            if (previousGoal != null)
                previousGoal.stop();
        }

        activeGoal.tick();
    }

    /**
     * Runs right before the custom fields for a character are displayed.
     * Do not try to manipulate position during this call
     */
    public abstract void updateCustomFields();

    /**
     * Runs right after a character is created, and before it spawns.
     * Used to add goals to the character
     */
    public abstract void initGoals();

    /**
     * Called whenever this character should attack another
     *
     * @param character The character to attack
     */
    public void attack(CustomCharacter character) {
        if (!this.isAlive()) return;
        lastAttacked = character;
        character.defend(this, 10);
        GridController.addEventString(getName() + " Attacks " + character.getName() + " for 10 damage! Leaving them with " + ((int) character.health) + " health!");
    }

    /**
     * Called whenever a character is attacked
     *
     * @param character The attacking character
     * @param damage    The amount of damage dealt
     */
    public void defend(CustomCharacter character, double damage) {
        this.health -= damage;
        lastAttackedBy = character;
        if (health <= 0) {
            character.kills++;
            CharacterSystem.killCharacter(this);
        }
    }
}
