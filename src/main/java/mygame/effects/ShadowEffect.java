package mygame.effects;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * Created by Admin on 12.06.2016.
 */
public class ShadowEffect  extends Application{

    public static void main(String[] args) {
        launch(args);
    }




    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Image shadow");
        Group root = new Group();
        Image image = new Image (this.getClass().getResourceAsStream("/earth.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setEffect(new DropShadow());
        imageView.setTranslateX(200);
        imageView.setTranslateY(200);
        Rotate rotate = new Rotate(30,40,40,40);

        root.getChildren().add(imageView);
        Scene scene = new Scene(root, 800, 800);
        scene.setFill(Color.CYAN);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
