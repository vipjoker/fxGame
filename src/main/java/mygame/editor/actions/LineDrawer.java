package mygame.editor.actions;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import mygame.Constants;

/**
 * Created by oleh on 3/18/17.
 */
public class LineDrawer extends Drawer {

    Path path;
    LineTo lineTo;
    public LineDrawer(Parent parent) {
        super(parent);
    }



    @Override
    public void mouseMoved(MouseEvent event) {
        if(lineTo == null){
            lineTo = new LineTo(event.getX(),event.getY());
            path.getElements().add(lineTo);
        }else{

            lineTo.setX(event.getX());
            lineTo.setY(event.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (path == null) {
            path = new Path(new MoveTo(event.getX(), event.getY()));
            path.setStroke(Constants.RED);
            path.setStrokeWidth(2);
            ((Pane) parent).getChildren().add(path);
        }else {
            lineTo = new LineTo(event.getX(),event.getY());
            path.getElements().add(lineTo);

        }

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void finishDrawing() {

    }
}
