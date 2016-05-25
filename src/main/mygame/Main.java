package main.mygame;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.mygame.action.GameAction;

public class Main extends Application {

    private GameAction mAction;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root;


        FXMLLoader fxmlLoader = new FXMLLoader();
        root= fxmlLoader.load(getClass().getResource("game.fxml").openStream());
        mAction = fxmlLoader.<Controller>getController();

        primaryStage.setTitle("Hello World");

        Scene scene = new Scene(root, 800, 600);
        addOnKeyListener(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addOnKeyListener(Scene scene){

    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case W:
                    mAction.jump();
                    System.out.println("up");
                    break;
                case S:
                    mAction.crouch();
                    System.out.println("down");
                    break;
                case A:
                    mAction.moveLeft();
                    System.out.println("left");
                    break;
                case D:
                    mAction.moveRight();
                    System.out.println("right");
                    break;
                case SHIFT:
                    mAction.run();
                    System.out.println("shift");
                    break;
            }
        }
    });

    }
    public static void main(String[] args) {
        launch(args);
    }
}
