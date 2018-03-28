package mygame.editor;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mygame.Constants;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * Created by Admin on 16.06.2016.
 */
public class Dialog {

    public static void showDialog(Path path){
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        Button button = new Button("OK");
        button.setOnAction(event -> stage.close());
        VBox pane = new VBox(10,new Label(path.toFile().getName()),button);
        try {
            Image image = new Image(path.toUri().toString());

            ImageView imageView = new ImageView(image);
            ScrollPane scrollPane = new ScrollPane(imageView);

            pane.getChildren().add(scrollPane);

        } catch (Exception e) {
            e.printStackTrace();
        }


        pane.setAlignment(Pos.CENTER);

        stage.setScene(new Scene( pane,300,300));
        stage.show();
    }



}
