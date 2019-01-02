package mygame.editor.controlers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import mygame.editor.App;
import mygame.editor.manager.FileManager;
import mygame.editor.model.Node;
import mygame.editor.parser.LocalParser;
import mygame.editor.views.NodeView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
        final String json = FileManager.getInstance().loadScene();
        List<Node> nodesFromString = LocalParser.createNodesFromString(json);
        for (Node ccNode : nodesFromString) {
            App.instance.repository.save(ccNode);
        }
    }

    public void onSave(ActionEvent actionEvent) {
        String json = LocalParser.createJsonFromNodes(App.instance.repository.getNodes());
        FileManager.getInstance().saveScene(json);
    }

    public void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onCreate(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();

        File f = chooser.showSaveDialog(App.instance.window);
        try {
            f.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
