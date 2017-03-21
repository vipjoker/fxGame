package mygame.editor;

import com.badlogic.gdx.graphics.Color;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mygame.Constants;

/**
 * Created by Admin on 16.06.2016.
 */
public class Dialog {

    public static void showDialog(String message){
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        Button button = new Button("OK");
        button.setOnAction(event -> stage.close());
        VBox pane = new VBox(10,new Label(message),button);
        pane.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(pane,300,300));
        stage.show();
    }

    public static void showBox2dDialog(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Pane pane = new Pane();


       pane.setBackground(new Background(new BackgroundFill(Constants.DARK,null,null)));
        Scene scene = new Scene(pane, 600, 600);
        stage.setScene(scene);

       setChildren(pane,scene);
       stage.show();
    }


    private static void setChildren(Pane pane,Scene scene){

        Text info = new Text();
        Text info2 = new Text();
        info2.setFill(javafx.scene.paint.Color.RED);
        info2.setY(-10);




        Rectangle rectangle = new Rectangle(200,200,Constants.GREEN);

        rectangle.setLayoutX(100);
        rectangle.setLayoutY(100);



        DragHelper drag1 = new DragHelper();
        DragHelper drag2 = new DragHelper();
        drag1.setDrag(pane);
        drag2.setDrag(rectangle);
        Button button = new Button("remove");
        button.setOnMouseClicked(event -> {
            drag1.removeDrag();
            drag2.removeDrag();
        });

        pane.setOnScroll(event ->  {
            double scaleY = pane.getScaleY();
            double delta = event.getDeltaY() / 100;
            double scale = scaleY + delta;
            if(scale < 10 && scale> 0.1){

                pane.setScaleX(scale);
                pane.setScaleY(scale);
            }
        });

        pane.getChildren().addAll(rectangle,info,info2,button);
    }


}
