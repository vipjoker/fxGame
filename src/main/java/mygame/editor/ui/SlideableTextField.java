package mygame.editor.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
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
    private double precission;

    public SlideableTextField(String name,double precission) {
        super();
        this.precission = precission;
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

        setupSlider();
    }


    public void bind(DoubleProperty doubleProperty){
        textFiled.textProperty().bindBidirectional(doubleProperty, new DecimalFormat());
        this.property = doubleProperty;
    }

    public void unbind(){
        textFiled.textProperty().unbindBidirectional(property);
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
