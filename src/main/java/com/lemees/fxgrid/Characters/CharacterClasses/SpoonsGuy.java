package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.RandomWalkCharacter;
import com.lemees.fxgrid.Characters.RegisterCharacter;

import java.util.List;

@RegisterCharacter(0)
public class SpoonsGuy extends RandomWalkCharacter {
    private static int counter = 0;
    public SpoonsGuy() {
        super("Spoons Guy "+counter++, "spoons.png");
    }

    @Override
    protected boolean shouldAttack(CustomCharacter cust) {
        return !((cust instanceof KneeSurgery) || (cust instanceof SpoonsGuy));
    }

    @Override
    public boolean isVictorious(List<CustomCharacter> cs) {
        return cs.stream()
                .filter(c->!this.getClass().isInstance(c))
                .count()==0;
    }
}
