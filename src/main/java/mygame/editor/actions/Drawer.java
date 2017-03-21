package mygame.editor.actions;


import com.badlogic.gdx.math.Vector2;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mygame.Constants;
import mygame.editor.DragHelper;

/**
 * Created by oleh on 3/18/17.
 */
public abstract class Drawer {
    protected Group parent;

    protected Drawer(Group parent) {
        this.parent = parent;
    }


    public abstract void mouseMoved(Point2D position);

    public abstract void mousePressed(Point2D position);

    public abstract void mouseReleased(Point2D position);

    public abstract void finishDrawing();

    protected Circle getHandler(Point2D point) {
        Circle circle = new Circle(point.getX(), point.getY(), 5);
        circle.setStroke(Constants.WHITE);
        circle.setFill(Constants.LIGHT_GREY);
        parent.getChildren().add(circle);
        setScaleListeners(circle);

        new DragHelper().setDrag(circle);
        return circle;
    }

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