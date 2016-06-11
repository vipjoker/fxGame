package mygame.editor;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Admin on 11.06.2016.
 */
public class Controller implements Initializable{
    public Pane pnGrid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 1000;
        double height = 1000;


        for(int i = 0 ; i < width ; i+=50){
            Line line = new Line(0,i,width,i);
            Line line1 = new Line(i,0,i,width);
            pnGrid.getChildren().addAll(line,line1);

        }
    }

    public void onClick(ActionEvent event){

    }
}
