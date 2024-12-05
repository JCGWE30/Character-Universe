package com.lemees.fxgrid.Characters.CharacterClasses;

import com.lemees.fxgrid.Characters.CustomCharacter;
import com.lemees.fxgrid.Characters.GoalSystem.Goals.FleeGoal;
import com.lemees.fxgrid.Characters.GoalSystem.Goals.IdleGoal;
import com.lemees.fxgrid.Characters.RegisterCharacter;

import java.util.List;

//Registers this character to spawn 2 of them on simulation start
@RegisterCharacter(2)
//Extends off CustomCharacter
public class ExampleCharacter extends CustomCharacter {
    public ExampleCharacter() {
        super("Example Name", //Name showed when hovering over a character, or viewing leaderboards. Can be different for each character instance
                "spoons.png"); //The image name to act as the icon. Relative to resources/images
        this.health=200; //Start us with 200 health
    }

    @Override
    public void updateCustomFields() {
        /*
        Characters have 3 custom info fields that can be used as desired. They can be changed below
        This method is run every time a character is hovered over, so they should be updated here
         */
        super.customInfoFields[0] = "Hey im";
        super.customInfoFields[1] = "Really";
        super.customInfoFields[2] = "Cool";
    }

    @Override
    public void initGoals() {
        //Sets it up so that this charater will stand idle, until they drop below 30 health
        //Then they will flee for 25 tiles
        addGoal(new FleeGoal(30,25,2));
        addGoal(new IdleGoal());
    }

    @Override
    public boolean isVictorious(List<CustomCharacter> cs) {
        //This character can never win, intended to check the list of characters and see if based on whos left alive
        //If this character should win
        return false;
    }
}
