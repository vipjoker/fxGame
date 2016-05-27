package mygame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by oleh on 27.05.16.
 */
public class ButtonsExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Button example");


        Group group = new Group();
        Scene scene = new Scene(group);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(500,500);
        group.getChildren().add(canvas);

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        new AnimationTimer(){
            @Override
            public void handle(long now) {

                graphicsContext2D.setFill(Color.GREEN);
                graphicsContext2D.rect(10,10,200,200);
                graphicsContext2D.setFill(Color.AQUA);
                graphicsContext2D.rect(30,10,200,200);

            }
        }.start();

        primaryStage.show();

    }
}
