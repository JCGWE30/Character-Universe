package com.lemees.fxgrid.Characters;

import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.HelloController;
import com.lemees.fxgrid.Systems.CharacterSystem;
import com.lemees.fxgrid.Systems.CoordinateSystem;

import java.util.*;

public class BountyCharacter extends CustomCharacter{
    private Queue<Coordinate> path;
    private CustomCharacter target;

    public BountyCharacter(String name, String imagePath) {
        super(name, imagePath);
    }

    @Override
    public void turnTick() {
        if(target==null||!target.isAlive()){
            List<CustomCharacter> targets = new ArrayList<>(CharacterSystem.getLivingCharacters().stream()
                    .filter(this::canTarget)
                    .toList());

            targets.sort(Comparator.comparingDouble(c->currentPosition.getDistance(c.currentPosition)));

            if(targets.isEmpty()) return;

            target = targets.getFirst();
            path = CoordinateSystem.getPath(currentPosition,target.getPosition(),this::obstacleFilter);
        }
        path = CoordinateSystem.getPath(currentPosition,target.getPosition(),this::obstacleFilter);
        for(Coordinate c:CoordinateSystem.getNighbors(currentPosition)){
            if(!CharacterSystem.tileOccupied(c)) continue;

            if(CharacterSystem.getCharacterOnTile(c).equals(target)){
                attack(target);
                return;
            }
        }
        if(path==null) return;
        path.poll();
        move(path.poll());
    }

    protected boolean canTarget(CustomCharacter c){
        return c!=this;
    }

    protected boolean obstacleFilter(Coordinate c){
        CustomCharacter character = CharacterSystem.getCharacterOnTile(c);
        return !CharacterSystem.tileOccupied(c) || character==target;
    }
}
