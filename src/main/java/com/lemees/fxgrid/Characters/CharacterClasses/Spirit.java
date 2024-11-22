package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.BountyCharacter;
import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.RandomWalkCharacter;
import com.lemees.fxgrid.Characters.RegisterCharacter;

@RegisterCharacter(50)
public class Spirit extends BountyCharacter {
    private static int counter = 0;
    public Spirit() {
        super("Spirit "+counter++, "testImage.png");
    }

    @Override
    protected boolean canTarget(CustomCharacter c) {
        return !((c instanceof Onryo) || (c instanceof Spirit));
    }
}
