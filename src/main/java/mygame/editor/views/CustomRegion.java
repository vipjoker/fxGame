package mygame.editor.views;

import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by oleh on 3/22/17.
 */
public class CustomRegion extends Pane {

    public CustomRegion(){
        super();
//        setBackground(new Background(new BackgroundFill(Color.AQUA,null,null)));
    }
    public void  addChild(Node... nodes){
        for(Node n :nodes)
        getChildren().add(n);
    }
    public void clearChildren(){
        getChildren().clear();
    }
}
