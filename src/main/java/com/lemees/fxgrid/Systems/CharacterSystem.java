package com.lemees.fxgrid.Systems;

import com.lemees.fxgrid.CharacterDropdownProvider;
import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.VictoryCondition;
import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.GridApplication;
import com.lemees.fxgrid.GridController;

import java.util.*;

public class CharacterSystem {
    private static CharacterSystem instance;

    List<CustomCharacter> characters;
    HashMap<Class<? extends CustomCharacter>, VictoryCondition> condition = new HashMap<>();

    public static void initialize() {
        instance = new CharacterSystem();
        instance.characters = Collections.synchronizedList(new ArrayList<>());
    }

    public static void spawnCharacter(CustomCharacter c) {
        Class<? extends CustomCharacter> cz = c.getClass();

        if (!instance.condition.containsKey(cz)) {
            instance.condition.put(cz, c);
        }

        c.dropdown = CharacterDropdownProvider.getNewDropdown(c);

        instance.characters.add(c);

        Coordinate[] coords = CoordinateSystem.getCoordinates();

        coords = Arrays.stream(coords)
                .filter(cc -> !tileOccupied(cc))
                .toArray(Coordinate[]::new);

        Random random = new Random();
        Coordinate coord = coords[random.nextInt(coords.length)];
        c.initGoals();
        c.move(coord);
    }

    /**
     * Checks if a character is on a tile
     *
     * @param c The tile to check
     * @return if its occupied
     */
    public static boolean tileOccupied(Coordinate c) {
        return !getLivingCharacters().stream()
                .filter(ch -> ch.getPosition().equals(c))
                .toList().isEmpty();
    }

    /**
     * Gets all characters that are alive
     *
     * @return living characters
     */
    public static List<CustomCharacter> getLivingCharacters() {
        return instance.characters.stream()
                .filter(CustomCharacter::isAlive)
                .toList();
    }

    /**
     * Gets all characters, regardless if their alive
     *
     * @return all characters
     */
    public static List<CustomCharacter> getCharacters() {
        return new ArrayList<>(instance.characters);
    }

    /**
     * Gets the character on a selected tile
     *
     * @param c The tile to check
     * @return The character on a tile, null otherwise
     */
    public static CustomCharacter getCharacterOnTile(Coordinate c) {
        return getLivingCharacters().stream()
                .filter(ch -> ch.getPosition().equals(c))
                .findFirst().orElse(null);
    }

    public static void killCharacter(CustomCharacter customCharacter) {
        GridController.addEventString(customCharacter.getName() + " HAS DIED!");
    }

    public static void checkForVictory(GridController controller) {
        for (Map.Entry<Class<? extends CustomCharacter>, VictoryCondition> e : instance.condition.entrySet()) {
            if (e.getValue().isVictorious(getLivingCharacters())) {
                GridApplication.gameSpeed.set(0);
                GridController.addEventString(e.getKey().getSimpleName() + " Has Won!");
                controller.updateEvents();
                return;
            }
        }
    }
}
