package mygame.editor.util;

import mygame.editor.model.*;
import mygame.editor.view.AbstractView;
import mygame.editor.view.CircleModel;
import mygame.editor.view.LineModel;
import mygame.editor.view.PolygonModel;

/**
 * Created by oleh on 3/27/17.
 */
public class Box2dMapper {

    public static Point toBox2d(AbstractView model){

        double layoutY = model.getLayoutY();
        double layoutX = model.getLayoutX();
       // getPosition()
        return null;
    }


    private Point getPosition (AbstractView model){
        switch (model.getModel().getType()){
            case BODY: return getLinePosition((LineModel)model);
            case JOINT: return getCirclePosition((CircleModel)model);



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
