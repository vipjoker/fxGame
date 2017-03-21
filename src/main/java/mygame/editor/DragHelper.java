package mygame.editor;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;

/**
 * Created by oleh on 3/21/17.
 */
public class DragHelper {
    private Point2D start;
    private Node node;
    public void setDrag(Node node){
        this.node = node;
        node.setOnMouseReleased(event -> {
            start = null;
        });

        node.setOnMousePressed(event -> {

            start = new Point2D(event.getX(),event.getY());


        });

        node.setOnMouseDragged(event -> {
            Node source = (Node)event.getSource();

            Point2D point ;
            if(source.getParent() == null) point = source.localToParent(event.getX(), event.getY());
            else point = source.localToParent(event.getX(),event.getY());



            if(start != null) {

                source.setLayoutX(point.getX() - start.getX());
                source.setLayoutY(point.getY() - start.getY());
            }
            event.consume();
        });
    }

    public void removeDrag(){
        node.setOnMouseDragged(null);
        node.setOnMousePressed(null);
        node.setOnMouseReleased(null);
    }




}
