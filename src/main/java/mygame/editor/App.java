package mygame.editor;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import mygame.editor.model.box2d.B2Polygon;
import mygame.editor.model.box2d.B2Root;
import mygame.editor.util.ResourceUtil;


public class App extends Application {

    public  Window window;
    public  Scene scene;
    public  Stage stage;
    public static App instance;

    @Override
    public void start(Stage primaryStage) throws Exception {

        instance = this;
        Parent load = FXMLLoader.load(this.getClass().getResource("/editor.fxml"));

        String json = ResourceUtil.loadString("/sample.json");
        B2Root b2Root = new Gson().fromJson(json, B2Root.class);

        scene = new Scene(load, 1200, 800);

        primaryStage.setTitle("Editor");
        window = scene.getWindow();
        stage = primaryStage;

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}

