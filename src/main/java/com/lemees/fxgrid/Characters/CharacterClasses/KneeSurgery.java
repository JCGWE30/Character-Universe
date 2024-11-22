package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.BountyCharacter;
import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.RandomWalkCharacter;
import com.lemees.fxgrid.Characters.RegisterCharacter;

import java.util.List;

@RegisterCharacter(20)
public class KneeSurgery extends BountyCharacter {
    private static int counter = 0;

    public KneeSurgery() {
        super("Knee Surgery "+counter++, "surg.jpg");
    }

    @Override
    protected boolean canTarget(CustomCharacter cust) {
        return !((cust instanceof KneeSurgery) || (cust instanceof SpoonsGuy));
    }

    @Override
    public boolean isVictorious(List<CustomCharacter> cs) {
        List<CustomCharacter> cc = cs.stream()
                .filter(c->!this.getClass().isInstance(c))
                .toList();
        return cc.size()==0;
    }
}
