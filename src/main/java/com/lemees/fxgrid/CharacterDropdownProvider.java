package com.lemees.fxgrid;

import com.lemees.fxgrid.Characters.CustomCharacter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

import java.io.IOException;

public class CharacterDropdownProvider {
    private CustomCharacter character;

    @FXML
    private Rectangle dropdownHue;

    @FXML
    private ImageView dropdownIcon;

    @FXML
    private Label dropdownName;

    @FXML
    private Label dropdownKills;

    public static FXMLLoader getNewDropdown(CustomCharacter c){
        try {
            FXMLLoader loader = new FXMLLoader(CharacterInfoProvider.class.getResource("character-sidebar.fxml"));
            AnchorPane pane = loader.load();
            CharacterDropdownProvider prov = loader.getController();
            prov.character = c;
            prov.updateStats();
            return loader;
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public void updateStats(){
        dropdownIcon.setImage(character.getLoadedImage());
        dropdownName.setText(character.getName());
        dropdownKills.setText(character.getKills()+" Kills");

        if(!character.isAlive()){
            dropdownHue.setOpacity(0.5);
        }
    }
}
