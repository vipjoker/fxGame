package mygame.editor;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import mygame.Constants;



public class Curosor extends Group {


    private Line hor,ver;
    private final int HALF_LENGTH = 10;
    private final int STROKE_WIDTH = 1;

    public Curosor(){
        hor =new Line();
        hor.setStroke(Constants.GREEN);
        hor.setStrokeWidth(STROKE_WIDTH);
        ver = new Line();
        ver.setStroke(Color.WHEAT);
        ver.setStrokeWidth(STROKE_WIDTH);
       getChildren().addAll(hor,ver);



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

    }




}
