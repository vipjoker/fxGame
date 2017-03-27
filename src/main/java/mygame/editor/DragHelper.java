package mygame.editor;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.Point;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by oleh on 3/21/17.
 */
public class DragHelper {
    private Point2D start;
    private Node node;
    private BiConsumer<Node,Point> delegate;

    public DragHelper(){}

    public DragHelper(BiConsumer<Node,Point> delegate){
        this.delegate=delegate;
    }
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
                double newX = point.getX() - start.getX();
                double newY = point.getY() - start.getY();
                source.setLayoutX(newX);
                source.setLayoutY(newY);
                if(delegate !=null)delegate.accept(node,new Point(event.getX(),event.getY()));
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
