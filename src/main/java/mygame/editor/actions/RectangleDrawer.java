package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import javafx.animation.ParallelTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mygame.Constants;

/**
 * Created by oleh on 3/18/17.
 */
public class RectangleDrawer extends Drawer {

    public RectangleDrawer(Parent parent) {
        super(parent);
    }


    Vector2 startPoint;
    Rectangle rectangle ;


    @Override
    public void mouseMoved(MouseEvent event) {
        System.out.println(event.toString());
        float x =(float) event.getX();
        float y = (float) event.getY();
        if(startPoint != null) {
            if (rectangle == null ) {
                rectangle = new Rectangle(startPoint.x, startPoint.y, x - startPoint.x, y - startPoint.y);
                rectangle.setStroke(Constants.RED);
                rectangle.setStrokeWidth(2);
                rectangle.setFill(Color.BLACK);

                rectangle.setOnMouseEntered(event1 -> {
                    Rectangle rect = (Rectangle) event1.getSource();


                    StrokeTransition fill = new StrokeTransition(Duration.millis(500));
                    fill.setToValue(Constants.GREEN);
                    ParallelTransition transition = new ParallelTransition(rect, fill);


                    transition.play();

                });
                rectangle.setOnMouseExited(event1 -> {
                    Rectangle rect = (Rectangle) event1.getSource();
                    StrokeTransition fill = new StrokeTransition(Duration.millis(500));
                    fill.setToValue(Constants.RED);


                    ParallelTransition transition = new ParallelTransition(rect, fill);


                    transition.play();
                });




                ((Pane)parent).getChildren().add(rectangle);
            }else{
                if(x > startPoint.x) rectangle.setWidth(x - startPoint.x);
                else {
                    rectangle.setWidth(startPoint.x - x );
                    rectangle.setX(x);
                }

                if(y > startPoint.y) rectangle.setHeight(y - startPoint.y);
                else {
                    rectangle.setHeight( startPoint.y - y);
                    rectangle.setY(y);
                }

            }
        }


    }

    @Override
    public void mousePressed(MouseEvent event) {
        float x =(float) event.getX();
        float y = (float) event.getY();
        if(startPoint == null) startPoint = new Vector2(x,y);
    }

    @Override
    public void mouseReleased(MouseEvent event) {

        float x =(float) event.getX();
        float y = (float) event.getY();

        startPoint = null;
        rectangle = null;


    }

    @Override
    public void finishDrawing() {

    }
}
