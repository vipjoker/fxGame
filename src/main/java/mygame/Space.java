package mygame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by oleh on 27.05.16.
 */
public class Space extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Space example");
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        Canvas canvas = new Canvas(512,512);

        root.getChildren().add(canvas);
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        final long nanotime = System.nanoTime();

        Image space = new Image("earth.jpg");

        Image rocket = new Image("rocket.png");

        new AnimationTimer(){
            int counter  =0;
            @Override
            public void handle(long now) {
                double t = (now -nanotime)/1000_000_000.0;
                graphicsContext2D.drawImage(space,0,0,512,512);

                double x = 232 + 128 * Math.cos(t);
                double y = 232 + 128 * Math.sin(t);

                graphicsContext2D.drawImage(rocket,x,y,100,100);

            }
        }.start();

        primaryStage.show();
    }
}
