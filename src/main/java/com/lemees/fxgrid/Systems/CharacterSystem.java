package com.lemees.fxgrid.Systems;

import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CharacterSystem {
    private static CharacterSystem instance;

    List<CustomCharacter> livingCharacters = new ArrayList<>();

    public static void initialize(){
        instance = new CharacterSystem();
    }

    public static void spawnCharacter(CustomCharacter c){
        instance.livingCharacters.add(c);

        Coordinate[] coords = Arrays.stream(CoordinateSystem.getCoordinates())
                .filter(c->!tileOccupied(c))
                .toArray(Coordinate[]::new);

        Random random = new Random();
        c.move(coords[random.nextInt(coords.length)]);
    }

    public static boolean tileOccupied(Coordinate c){
        return instance.livingCharacters.stream()
                .filter(ch -> ch.getPosition() == c)
                .toList().isEmpty();
    }
}
