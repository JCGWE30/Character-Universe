package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.RandomWalkCharacter;
import com.lemees.fxgrid.Characters.RegisterCharacter;

import java.util.Random;

@RegisterCharacter(50)
public class Onryo extends RandomWalkCharacter {
    private static int counter = 0;
    public Onryo() {
        super("Onryo "+counter++, "onryo.png");
    }

    @Override
    protected boolean shouldAttack(CustomCharacter cust) {
        return !((cust instanceof Onryo) || (cust instanceof Spirit));
    }
}
