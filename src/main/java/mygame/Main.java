package mygame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mygame.action.GameAction;
import mygame.level.FirstLevel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application implements Updatable{
    public static Set<KeyCode> buttons ;

    private List<Renderable> views = new ArrayList<>();
    private Renderer mRenderer;
    private GraphicsContext graphicsContext2D;
    @Override
    public void start(Stage primaryStage) throws Exception{
        buttons = new HashSet<>();
        primaryStage.setTitle("Game");
        Group group = new Group();
        Scene scene = new Scene(group);
        Canvas canvas = new Canvas(1280,720);
        graphicsContext2D = canvas.getGraphicsContext2D();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        group.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        //new FirstLevel(canvas);
        mRenderer = new Renderer(this);
        views.add(new Cube(100,100,100,100));

    }

    @Override
    public void mainLoop(long time) {
        if(buttons.contains(KeyCode.SPACE))mRenderer.pause();

        graphicsContext2D.clearRect(0,0,1280,720);
         for(Renderable r : views)r.draw(graphicsContext2D);
    }

    private void onKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.SPACE && mRenderer.isPaused()){
            synchronized (mRenderer){
                mRenderer.pause();
               mRenderer.notify();
            }
        }
        buttons.add(keyEvent.getCode());
    }

    private void onKeyReleased(KeyEvent keyEvent){
        buttons.remove(keyEvent.getCode());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
