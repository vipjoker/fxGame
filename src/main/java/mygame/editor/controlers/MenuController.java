package mygame.editor.controlers;

import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import mygame.editor.App;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    public MenuBar menuBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);
    }

    public void onAdd(ActionEvent actionEvent) {

    }

    public void onLoad(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Json Files", "*.json"));


        final Scene scene = menuBar.getScene();
        File selectedFile = fileChooser.showOpenDialog(scene.getWindow());

    }

    public void onSave(ActionEvent actionEvent) {

    }
}
