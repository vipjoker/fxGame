package mygame.editor.util;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import mygame.editor.model.Point;

/**
 * Created by oleh on 18.04.18.
 */
public class DragNDrop  {

    Point startPoint = new Point(0,0);

    EventHandler<MouseEvent> startEvent = this::onStartDrag;
    EventHandler<MouseEvent> dragEvent = this::onDragMove;

    public EventHandler<MouseEvent> getOnStartListener(){

        return startEvent;
    };

    public EventHandler<MouseEvent> getOnMoveListener(){
        return dragEvent;
    };

    private void onDragMove(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Point2D p = source.getParent().screenToLocal(mouseEvent.getScreenX(),mouseEvent.getScreenY());
        source.setLayoutX(p.getX()- startPoint.getX());
        source.setLayoutY(p.getY()- startPoint.getY());




    }

    private void onStartDrag(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Point2D p = source.screenToLocal(mouseEvent.getScreenX(),mouseEvent.getScreenY());
        startPoint.set(p.getX(),p.getY());
    }

}
