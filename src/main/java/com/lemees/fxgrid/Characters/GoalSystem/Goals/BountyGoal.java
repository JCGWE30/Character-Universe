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

public class BountyGoal extends Goal {
    private Predicate<CustomCharacter> characterFilter;
    private int maxRange;
    private boolean retarget;
    private CustomCharacter target;
    private Queue<Coordinate> path;
    public BountyGoal(Predicate<CustomCharacter> characterFilter,int maxRange,boolean retarget){
        this.characterFilter=characterFilter;
        this.maxRange=maxRange;
        this.retarget = retarget;
    }
    public BountyGoal(Predicate<CustomCharacter> characterFilter){
        this.characterFilter=characterFilter;
    }

    @Override
    public boolean canStart() {
        return getNearestCharacter().isPresent();
    }

    @Override
    public boolean shouldStop() {
        return target==null|| getNearestCharacter().isEmpty() ||!target.isAlive();
    }

    @Override
    public void tick() {
        if(retarget){
            getNearestCharacter().ifPresent((c)->target=c);
        }
        if(Math.abs(host.getDistance(target))>maxRange&&maxRange!=-1){
            target=null;
            return;
        }
        if(target==null||target.isAlive()) return;
    }

    @Override
    public void start() {
        target = getNearestCharacter().orElse(null);
    }

    @Override
    public void stop() {

    }

    private Optional<CustomCharacter> getNearestCharacter(){
        return CharacterSystem.getLivingCharacters().stream()
                .filter(characterFilter)
                .filter(c->c!=host)
                .filter(this::distanceFilter)
                .sorted(Comparator.comparingDouble(c->host.getPosition().getDistance(c.getPosition())))
                .findFirst();
    }

    private boolean distanceFilter(CustomCharacter c){
        if(maxRange==-1) return true;
        return host.getDistance(c)<maxRange;
    }
}
