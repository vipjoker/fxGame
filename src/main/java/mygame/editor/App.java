package mygame.editor;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import mygame.editor.interfaces.KeyListener;
import mygame.editor.model.Command;
import mygame.editor.model.Node;
import mygame.editor.repository.NodeModel;
import mygame.editor.views.NodeView;

import java.util.*;


public class App extends Application {

    public  Window window;
    public  Scene scene;
    public  Stage stage;
    public static App instance;
    private final Map<Class, Object> controllers = new HashMap<>();
    public  final List<KeyListener> keyListeners = new ArrayList<>();
    public static Set<KeyCode> buttons  = new HashSet<>();
    public final SimpleObjectProperty<Command> observableAction = new SimpleObjectProperty<>();
    public final NodeModel repository = new NodeModel();
    public final ObservableList<Node> selected = FXCollections.observableArrayList();

    public void registerController(Object object) {
        controllers.put(object.getClass(),object);
    }

    public <T> T getController(Class clazz){
        final Object o = controllers.get(clazz);
        return (T)o;
    }

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

