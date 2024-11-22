package com.lemees.fxgrid.Systems;

import com.lemees.fxgrid.CharacterDropdownProvider;
import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.VictoryCondition;
import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.HelloApplication;
import com.lemees.fxgrid.HelloController;

import java.util.*;

public class CharacterSystem {
    private static CharacterSystem instance;

    List<CustomCharacter> characters;
    HashMap<Class<? extends CustomCharacter>, VictoryCondition> condition = new HashMap<>();

    public static void initialize(){
        instance = new CharacterSystem();
        instance.characters = Collections.synchronizedList(new ArrayList<>());
    }

    public static void spawnCharacter(CustomCharacter c){
        Class<? extends CustomCharacter> cz = c.getClass();

        if(!instance.condition.containsKey(cz)){
            instance.condition.put(cz,c);
        }

        c.dropdown = CharacterDropdownProvider.getNewDropdown(c);

        instance.characters.add(c);

        Coordinate[] coords = CoordinateSystem.getCoordinates();

        coords = Arrays.stream(coords)
                .filter(cc->!tileOccupied(cc))
                .toArray(Coordinate[]::new);

        Random random = new Random();
        Coordinate coord = coords[random.nextInt(coords.length)];
        c.move(coord);
    }

    public static boolean tileOccupied(Coordinate c){
        return !getLivingCharacters().stream()
                .filter(ch -> ch.getPosition().equals(c))
                .toList().isEmpty();
    }

    public static List<CustomCharacter> getLivingCharacters(){
        return instance.characters.stream()
                .filter(CustomCharacter::isAlive)
                .toList();
    }

    public static List<CustomCharacter> getCharacters(){
        return new ArrayList<>(instance.characters);
    }

    public static CustomCharacter getCharacterOnTile(Coordinate c){
        return getLivingCharacters().stream()
                .filter(ch->ch.getPosition().equals(c))
                .findFirst().orElse(null);
    }

    public static void killCharacter(CustomCharacter customCharacter) {
        HelloController.addEventString(customCharacter.getName()+" HAS DIED!");
    }

    public static void checkForVictory(HelloController controller){
        for(Map.Entry<Class<? extends CustomCharacter>,VictoryCondition> e:instance.condition.entrySet()){
            if(e.getValue().isVictorious(getLivingCharacters())){
                HelloApplication.gameSpeed.set(0);
                HelloController.addEventString(e.getKey().getSimpleName()+" Has Won!");
                controller.updateEvents();
                return;
            }
        }
    }
}
