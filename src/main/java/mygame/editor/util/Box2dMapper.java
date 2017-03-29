package mygame.editor.util;

import javafx.scene.shape.Circle;
import mygame.editor.model.*;

/**
 * Created by oleh on 3/27/17.
 */
public class Box2dMapper {

    public static Point toBox2d(AbstractModel model){

        double layoutY = model.getLayoutY();
        double layoutX = model.getLayoutX();
       // getPosition()
        return null;
    }


    private Point getPosition (AbstractModel model){
        switch (model.getType()){
            case LINE: return getLinePosition((LineModel)model);
            case CIRCLE: return getCirclePosition((CircleModel)model);
            case POLYGON: return getPolygonPosition((PolygonModel)model);


        }
        return null;
    }

    private Point getPolygonPosition(PolygonModel model) {
        return null;
    }

    private Point getCirclePosition(CircleModel model) {
        return null;
    }

    private Point getLinePosition(LineModel model) {
        return null;
    }

}
