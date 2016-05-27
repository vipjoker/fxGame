package mygame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HelloWorld extends Application{

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Canvas Example");

        Group root = new Group();

        Scene theScene = new Scene(root);

        primaryStage.setScene(theScene);

        Canvas canvas = new Canvas(400,200);

        root.getChildren().add(canvas);

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

        graphicsContext2D.setFill(Color.RED);
        graphicsContext2D.setStroke(Color.BLACK);

        graphicsContext2D.setLineWidth(2);

        Font font  = Font.font("Times New Roman", FontWeight.BOLD,48);
        graphicsContext2D.setFont(font);
        graphicsContext2D.fillText("Hello World",60,50);
        graphicsContext2D.strokeText("Hello World", 60,50);
        primaryStage.show();
    }
}
