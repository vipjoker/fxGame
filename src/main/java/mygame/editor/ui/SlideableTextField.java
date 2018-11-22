package mygame.editor.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.text.DecimalFormat;

public class SlideableTextField extends AnchorPane {

    private final Text text;
    private final TextField textFiled;
    private double lastX;
    private double lastValue;
    private DoubleProperty property;
    private StringProperty stringProperty;
    private double precission = 10;

    public SlideableTextField(String name, DoubleProperty doubleProperty) {
        super();

        setPrefHeight(40);

        this.property = doubleProperty;
        text = new Text(name);
        text.setLayoutY(25);
        text.setFill(Color.WHITE);

        textFiled = new TextField(doubleProperty.asString().get());
        textFiled.setLayoutY(7);
        textFiled.setPrefWidth(125);
        textFiled.textProperty().bindBidirectional(doubleProperty, new DecimalFormat());
        getChildren().add(text);
        getChildren().add(textFiled);


        AnchorPane.setRightAnchor(textFiled, 10.0);
        AnchorPane.setLeftAnchor(text, 10.0);

        setupSlider();
    }


    public SlideableTextField(String name, StringProperty stringProperty) {
        super();

        setPrefHeight(40);

        this.stringProperty = stringProperty;
        text = new Text(name);
        text.setLayoutY(25);
        text.setFill(Color.WHITE);

        textFiled = new TextField(stringProperty.get());
        textFiled.setLayoutY(7);
        textFiled.setPrefWidth(125);
        textFiled.textProperty().bindBidirectional(stringProperty);
        getChildren().add(text);
        getChildren().add(textFiled);


        AnchorPane.setRightAnchor(textFiled, 10.0);
        AnchorPane.setLeftAnchor(text, 10.0);
    }

    private void setupSlider() {

        text.setCursor(Cursor.H_RESIZE);
        text.setOnMousePressed(event -> {
            lastX = event.getX();
            lastValue = property.doubleValue();
        });
        text.setOnMouseDragged(event -> {
            final double newX = event.getX();
            double deltaX = newX - lastX ;
            double value = deltaX + lastValue;
            lastValue = value;
            double reminder = value % precission;

            property.set(value - reminder);
            lastX = newX;
        });

    }
}
