package mygame.demo;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloWorld extends Application{

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Canvas Example");
        Group group = new Group();
        Scene scene = new Scene(group,800,800);
        primaryStage.setScene(scene);
        Text text = new Text();
        text.setX(200);
        text.setY(200);
        text.setText("Hello");
        text.setStyle("-fx-font-size: 30px; -fx-border-color: cadetblue; -fx-stroke-width: 1px;-fx-background-color: coral");
        group.getChildren().add(text);
        primaryStage.show();

        startThreads(text);
    }

    private void startThreads(Text text){
        new Thread(new Cinema(text)).start();
    }


}
