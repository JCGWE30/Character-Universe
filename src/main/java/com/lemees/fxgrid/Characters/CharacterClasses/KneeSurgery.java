package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.BountyCharacter;
import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.RandomWalkCharacter;
import com.lemees.fxgrid.Characters.RegisterCharacter;

@RegisterCharacter(50)
public class KneeSurgery extends BountyCharacter {
    private static int counter = 0;

    public KneeSurgery() {
        super("Knee Surgery "+counter++, "surg.jpg");
    }

    @Override
    protected boolean canTarget(CustomCharacter cust) {
        return !((cust instanceof KneeSurgery) || (cust instanceof SpoonsGuy));
    }
}
