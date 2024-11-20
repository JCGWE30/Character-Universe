package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.RegisterCharacter;

@RegisterCharacter
public class BasicCharacter extends CustomCharacter {
    protected BasicCharacter() {
        super("Basic Character", "testImage.png");
    }
}
