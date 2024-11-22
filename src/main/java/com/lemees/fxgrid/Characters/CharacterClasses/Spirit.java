package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.BountyCharacter;
import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.RandomWalkCharacter;
import com.lemees.fxgrid.Characters.RegisterCharacter;

import java.util.List;

@RegisterCharacter(0)
public class Spirit extends BountyCharacter {
    private static int counter = 0;
    public Spirit() {
        super("Spirit "+counter++, "testImage.png");
    }

    @Override
    protected boolean canTarget(CustomCharacter c) {
        return !((c instanceof Onryo) || (c instanceof Spirit));
    }

    @Override
    public boolean isVictorious(List<CustomCharacter> cs) {
        return cs.stream()
                .filter(c->!this.getClass().isInstance(c))
                .count()==0;
    }
}
