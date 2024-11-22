package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.RandomWalkCharacter;
import com.lemees.fxgrid.Characters.RegisterCharacter;

@RegisterCharacter(50)
public class SpoonsGuy extends RandomWalkCharacter {
    private static int counter = 0;
    public SpoonsGuy() {
        super("Spoons Guy "+counter++, "spoons.png");
    }

    @Override
    protected boolean shouldAttack(CustomCharacter cust) {
        return !((cust instanceof KneeSurgery) || (cust instanceof SpoonsGuy));
    }
}
