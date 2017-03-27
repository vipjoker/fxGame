package mygame.editor;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import mygame.Constants;

/**
 * Created by oleh on 3/25/17.
 */
public class Axis extends Group {

    private Line hor,ver;
    private Text text;
    private final int HALF_LENGTH = 10;
    private final int STROKE_WIDTH = 1;

    public Axis(){
        hor =new Line();
        hor.setStroke(Constants.LIGHT_GREY);
        hor.setStrokeWidth(STROKE_WIDTH);
        ver = new Line();
        ver.setStroke(Constants.LIGHT_GREY);
        ver.setStrokeWidth(STROKE_WIDTH);
        text = new Text();
        text.setFill(Constants.LIGHT_GREY);
       getChildren().addAll(hor,ver,text);



    }

    public void update(double x ,double y,double width,double height){
        hor.setStartX(0);

        hor.setEndX(width);
        hor.setStartY(y);
        hor.setEndY(y);

        ver.setStartX(x);
        ver.setEndX(x);
        ver.setStartY(0);
        ver.setEndY(height);
        text.setX(x + HALF_LENGTH);
        text.setY(y - 2);

        text.setText("0");
    }



}
