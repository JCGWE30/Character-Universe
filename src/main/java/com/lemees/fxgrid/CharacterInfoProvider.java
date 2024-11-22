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

public class CharacterInfoProvider {
    private record CharacterPanels(AnchorPane pane,CharacterInfoProvider controller){}

    @FXML
    private ImageView characterIcon;
    @FXML
    private Text characterName;

    public void setupInfo(CustomCharacter character){
        characterName.setText(character.getName());
        Image icon = new Image(getClass().getResource(character.getImage()).toExternalForm());
        characterIcon.setImage(icon);
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
