package mygame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mygame.action.GameAction;
import mygame.level.FirstLevel;

import java.util.HashSet;
import java.util.Set;

public class Main extends Application {
    public Set<KeyCode> buttons ;
    private static Main INSTANCE;

    public static Main getInstance(){
        return INSTANCE;
    }

    private GameAction mAction;
    @Override
    public void start(Stage primaryStage) throws Exception{
        INSTANCE = this;
        buttons = new HashSet<>();
        primaryStage.setTitle("Game");
        Group group = new Group();
        Scene scene = new Scene(group);
        Canvas canvas = new Canvas(1280,720);

        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
        scene.setOnKeyTyped(this::onKeyTyped);

        group.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        new FirstLevel(canvas);
    }

    private void onKeyPressed(KeyEvent keyEvent){
        buttons.add(keyEvent.getCode());
    }

    private void onKeyReleased(KeyEvent keyEvent){
        buttons.remove(keyEvent.getCode());
    }

    private void onKeyTyped(KeyEvent keyEvent){
        buttons.remove(keyEvent.getCode());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
