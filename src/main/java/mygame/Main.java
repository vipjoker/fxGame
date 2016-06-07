package mygame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mygame.level.SimpleBackground;
import mygame.level.SimpleFPS;
import mygame.level.SimpleLevel;
import mygame.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application implements Updatable {
    public static Set<KeyCode> buttons;

    private List<Renderable> views = new ArrayList<>();
    private Renderer mRenderer;
    private GraphicsContext graphicsContext2D;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        buttons = new HashSet<>();
        primaryStage.setTitle("Game");
        Group group = new Group();
        Scene scene = new Scene(group);

        Canvas canvas = new Canvas(Constants.WIDTH, Constants.HEIGHT);
        graphicsContext2D = canvas.getGraphicsContext2D();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        group.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        // new FirstLevel(canvas);
        primaryStage.show();
        mRenderer = new Renderer(this);
        views.add(new SimpleBackground());
        views.add(new SimpleLevel());
        views.add(new SimpleFPS());

    }

    @Override
    public void mainLoop(long time) {


        graphicsContext2D.clearRect(0, 0, 640, 480);

        for (Renderable r : views) r.draw(graphicsContext2D);
    }

    private void onKeyPressed(KeyEvent keyEvent) {

        buttons.add(keyEvent.getCode());
    }

    private void onKeyReleased(KeyEvent keyEvent) {


        if (keyEvent.getCode() == KeyCode.SPACE)
            if (mRenderer.isPaused()) {
                graphicsContext2D.setGlobalAlpha(1.0);
                synchronized (mRenderer) {
                    mRenderer.unpause();
                    mRenderer.notify();
                }
            }else{
                graphicsContext2D.setGlobalAlpha(0.5);
                mRenderer.pause();
            }


        buttons.remove(keyEvent.getCode());
    }
}
