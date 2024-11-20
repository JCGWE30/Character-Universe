package com.lemees.fxgrid.Characters;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.text.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CharacterLoader {
    public static List<Class<? extends CustomCharacter>> characterList;

    public static void loadCharacters(){
        characterList = new ArrayList<>();
        Reflections reflections = new Reflections("com.lemees.fxgrid.Characters.CharacterClasses");
        Set<String> classes = reflections.get(Scanners.TypesAnnotated.with(RegisterCharacter.class));
        for(String cs:classes){
            try {
                @SuppressWarnings("unchecked")
                Class<? extends CustomCharacter> character = (Class<? extends CustomCharacter>) Class.forName(cs);
                characterList.add(character);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
