package mygame.editor;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


/**
 * Created by oleh on 3/20/17.
 */
public class Curosor extends Group {


    private Line hor,ver;
    private Text text;
    Circle circle;
    private final int HALF_LENGTH = 10;
    private final int STROKE_WIDTH = 1;

    public Curosor(){
        hor =new Line();
        hor.setStroke(Color.WHEAT);
        hor.setStrokeWidth(STROKE_WIDTH);
        ver = new Line();
        ver.setStroke(Color.WHEAT);
        ver.setStrokeWidth(STROKE_WIDTH);
        text = new Text();
        text.setFill(Color.WHEAT);
        circle = new Circle(5);
        circle.setStroke(Color.WHEAT);
        circle.setFill(Color.TRANSPARENT);
        getChildren().addAll(hor,ver,text,circle);



    }

    public void update(double x ,double y,String info){
        hor.setStartX(x-HALF_LENGTH);
        hor.setEndX(x+HALF_LENGTH);
        hor.setStartY(y);
        hor.setEndY(y);

        ver.setStartX(x);
        ver.setEndX(x);
        ver.setStartY(y - HALF_LENGTH);
        ver.setEndY( y + HALF_LENGTH);
        text.setX(x + HALF_LENGTH);
        text.setY(y - 2);

        text.setText(info);

        circle.setCenterY(y);
        circle.setCenterX(x);

    }




}
