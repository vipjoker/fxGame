package mygame.editor;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Admin on 11.06.2016.
 */
public class Controller implements Initializable{
    public Pane pnGrid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 500;
        double height = 500;


        for(int i = 0 ; i < width ; i+=50){
            Line line = new Line(0,i,width,i);
            Line line1 = new Line(i,0,i,width);


            Rectangle rectangle = new Rectangle(i+5,i+5,40,40);
            pnGrid.getChildren().addAll(line,line1,rectangle);

        }
    }

    public void onClick(ActionEvent event){

    }
}
