package com.lemees.fxgrid.Characters.GoalSystem.Goals;

import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.GoalSystem.Goal;
import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.Systems.CharacterSystem;
import com.lemees.fxgrid.Systems.CoordinateSystem;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * BountyGoal will select a random target and chase them until they are dead, or they leave range
 */
public class BountyGoal extends Goal {
    private Predicate<CustomCharacter> characterFilter;
    private int maxRange = -1;
    private boolean retarget = false;
    private CustomCharacter target;
    private Queue<Coordinate> path;

    /**
     * Creates a bounty goal with custom targeting settings
     * @param characterFilter filters what characters the goal can target (will never target its self)
     * @param maxRange the range at which a target can be selected, and chases
     * @param retarget if another potential target is closer than the selected target, should the goal retarget
     */
    public BountyGoal(Predicate<CustomCharacter> characterFilter, int maxRange, boolean retarget) {
        this.characterFilter = characterFilter;
        this.maxRange = maxRange;
        this.retarget = retarget;
    }

    /**
     * Creates a bounty goal with the default targeting settings
     * @param characterFilter filters what characters the goal can target (will never target its self)
     */
    public BountyGoal(Predicate<CustomCharacter> characterFilter) {
        this.characterFilter = characterFilter;
    }

    @Override
    public boolean canStart() {
        Optional<CustomCharacter> nearest = getNearestCharacter();
        return nearest.isPresent();
    }

    @Override
    public boolean shouldStop() {
        return target == null || getNearestCharacter().isEmpty() || !target.isAlive();
    }

    @Override
    public void tick() {
        if (retarget) {
            getNearestCharacter().ifPresent((c) -> target = c);
        }
        if (target==null || Math.abs(host.getDistance(target)) > maxRange && maxRange != -1) {
            target = null;
            return;
        }

        if (target == null || !target.isAlive()) return;

        if(host.getDistance(target)<2){
            host.attack(target);
            return;
        }

        try {
            path = CoordinateSystem.getPath(host.getPosition(), target.getPosition(), this::hostExclude);
            path.poll();
            host.move(path.poll());
        }catch(Exception e){
            CharacterSystem.killCharacter(host);
        }
    }

    private boolean hostExclude(Coordinate c){
        if(CharacterSystem.tileOccupied(c))
            return CharacterSystem.getCharacterOnTile(c).equals(target);
        return true;
    }

    @Override
    public void start() {
        target = getNearestCharacter().orElse(null);
    }

    @Override
    public void stop() {

    }

    private Optional<CustomCharacter> getNearestCharacter() {
        Stream<CustomCharacter> stream = CharacterSystem.getLivingCharacters().stream()
                .filter(c -> c != host)
                .filter(this::distanceFilter);

        if (characterFilter != null)
            stream.filter(characterFilter);

        return stream
                .sorted(Comparator.comparingDouble(c -> host.getPosition().getDistance(c.getPosition())))
                .findFirst();
    }

    private boolean distanceFilter(CustomCharacter c) {
        if (maxRange == -1) return true;
        return host.getDistance(c) < maxRange;
    }
}
