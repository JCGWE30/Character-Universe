package com.lemees.fxgrid;

import com.lemees.fxgrid.Characters.CustomCharacter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class CharacterInfoProvider {
    @FXML
    private ImageView characterIcon;
    @FXML
    private Text characterName;
    @FXML
    private Text characterKills;
    @FXML
    private Text characterHealth;
    @FXML
    private Text customField1;
    @FXML
    private Text customField2;
    @FXML
    private Text customField3;

    public void setupInfo(CustomCharacter character){
        characterName.setText(character.getName());
        Image icon = new Image(getClass().getResource(character.getImage()).toExternalForm());
        characterIcon.setImage(icon);
        characterKills.setText("Kills: "+character.getKills());
        characterHealth.setText("Health: "+character.getHealth());
        Optional<String>[] customFields = character.getOptionalCustomFields();

        character.updateCustomFields();
        customField1.setText(customFields[0].orElse(""));
        customField2.setText(customFields[1].orElse(""));
        customField3.setText(customFields[2].orElse(""));
    }

    public static FXMLLoader createCharacterInfo(){
        try {
            FXMLLoader loader = new FXMLLoader(CharacterInfoProvider.class.getResource("character-info.fxml"));
            loader.load();
            return loader;
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
