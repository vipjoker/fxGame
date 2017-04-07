package mygame.editor.actions;


import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mygame.Constants;
import mygame.editor.ui.CustonPane;
import mygame.editor.view.AbstractView;
import mygame.editor.model.Point;

import java.util.List;


public abstract class Action {
    protected CustonPane parent;
    protected final List<AbstractView> models;
    protected Action(CustonPane parent, List<AbstractView> models) {
        this.parent = parent;
        this.models = models;
    }

    public void init(){}

    public abstract void mouseMoved(Point position);

    public abstract void mousePressed(Point position);

    public abstract void mouseReleased(Point position);

    public abstract void finishDrawing();



    protected void setScaleListeners(Node node) {


        node.setOnMouseEntered(event1 -> {
            Node n = (Node) event1.getSource();


            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500));
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);

            ParallelTransition transition = new ParallelTransition(n, scaleTransition);


            transition.play();

        });
        node.setOnMouseExited(event1 -> {
            Node n = (Node) event1.getSource();
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500));
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);

            ParallelTransition transition = new ParallelTransition(n, scaleTransition);


            transition.play();
        });

    }





    protected void setColorListeners(Node node) {
        node.setOnMouseEntered(event1 -> {
            Node rect = (Node) event1.getSource();


            StrokeTransition strokeTransition = new StrokeTransition(Duration.millis(500));
            strokeTransition.setToValue(Constants.GREEN);
            FillTransition fillTransition = new FillTransition(Duration.millis(500));
            fillTransition.setToValue(Constants.GREEN.deriveColor(1, 1, 1, .25));
            ParallelTransition transition = new ParallelTransition(rect, strokeTransition, fillTransition);


            transition.play();

        });
        node.setOnMouseExited(event1 -> {
            Node rect = (Node) event1.getSource();
            StrokeTransition fill = new StrokeTransition(Duration.millis(500));
            fill.setToValue(Constants.RED);
            FillTransition fillTransition = new FillTransition(Duration.millis(500));
            fillTransition.setToValue(Color.RED.deriveColor(1, 1, 1, .25));
            ParallelTransition transition = new ParallelTransition(rect, fillTransition, fill);


            transition.play();
        });
    }
}