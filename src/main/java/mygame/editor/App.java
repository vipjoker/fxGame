package mygame.editor;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import mygame.editor.interfaces.KeyListener;
import mygame.editor.model.box2d.B2Root;
import mygame.editor.util.ResourceUtil;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class App extends Application {

    public  Window window;
    public  Scene scene;
    public  Stage stage;
    public static App instance;
    public  final List<KeyListener> keyListeners = new ArrayList<>();
    public static Set<KeyCode> buttons  = new HashSet<>();


    @Override
    public void start(Stage primaryStage) throws Exception {

        instance = this;
        Parent load = FXMLLoader.load(this.getClass().getResource("/editor.fxml"));


        scene = new Scene(load, 1200, 800);

        primaryStage.setTitle("Editor");
        window = scene.getWindow();
        stage = primaryStage;


        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
        load.requestFocus();


        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void onKeyPressed(KeyEvent keyEvent) {
        for (KeyListener keyListener : keyListeners) {
            keyListener.onKeyPressed(keyEvent);
        }
        buttons.add(keyEvent.getCode());
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        buttons.remove(keyEvent.getCode());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void addKeyListener(KeyListener listener){
        keyListeners.add(listener);
    }

    public void removeKeyListener(KeyListener listener){
        keyListeners.remove(listener);
    }

}

