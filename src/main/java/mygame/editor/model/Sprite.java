package mygame.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Sprite extends Node {
    private final StringProperty image;
    public Sprite(String image){
        this.image = new SimpleStringProperty(image);
    }

    public StringProperty getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image.set(image);
    }
}
