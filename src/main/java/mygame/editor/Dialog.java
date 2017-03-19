package mygame.editor;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
}