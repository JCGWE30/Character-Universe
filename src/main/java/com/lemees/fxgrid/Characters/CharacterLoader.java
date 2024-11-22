package com.lemees.fxgrid.Characters;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.text.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CharacterLoader {
    public static HashMap<Class<? extends CustomCharacter>,Integer> characterList;

    public static void loadCharacters(){
        characterList = new HashMap<>();
        Reflections reflections = new Reflections("com.lemees.fxgrid.Characters.CharacterClasses");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RegisterCharacter.class);
        for(Class<?> cs:classes){
            int value = cs.getAnnotation(RegisterCharacter.class).value();
            @SuppressWarnings("unchecked")
            Class<? extends CustomCharacter> character = (Class<? extends CustomCharacter>) cs;
            characterList.put(character,value);
        }
    }
}
