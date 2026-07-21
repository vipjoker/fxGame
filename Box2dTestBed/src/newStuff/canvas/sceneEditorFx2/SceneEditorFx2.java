package newStuff.canvas.sceneEditorFx2;


import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SceneEditorFx2 extends Application {

    // ===== Entry point =====
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneEditorPane root = new SceneEditorPane();
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("2D Scene Editor (JavaFX)");
        stage.setScene(scene);
        stage.show();
    }

    // ===== Tools =====
    enum Tool {
        SELECT_MOVE, PAN, ADD_RECT, ADD_CIRCLE, ADD_LINE, ADD_POLYGON, ADD_SPRITE
    }





}



