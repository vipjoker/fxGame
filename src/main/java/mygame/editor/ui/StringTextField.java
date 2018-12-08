package mygame.editor.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.text.DecimalFormat;

public class StringTextField extends AnchorPane {

    private final Text text;
    private final TextField textFiled;
    private StringProperty stringProperty;

    public StringTextField(String name) {
        super();

        setPrefHeight(40);


        text = new Text(name);
        text.setLayoutY(25);
        text.setFill(Color.WHITE);

        textFiled = new TextField();
        textFiled.setLayoutY(7);
        textFiled.setPrefWidth(125);

        getChildren().add(text);
        getChildren().add(textFiled);

        AnchorPane.setRightAnchor(textFiled, 10.0);
        AnchorPane.setLeftAnchor(text, 10.0);
    }


    public void bind(StringProperty stringProperty) {
        this.stringProperty = stringProperty;
        textFiled.textProperty().bindBidirectional(stringProperty);
    }

    public void unbind(){
        textFiled.textProperty().unbindBidirectional(stringProperty);
    }

}
