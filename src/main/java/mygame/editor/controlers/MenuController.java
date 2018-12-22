package mygame.editor.controlers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import mygame.editor.App;
import mygame.editor.manager.FileManager;
import mygame.editor.parser.LocalParserKt;
import mygame.editor.views.CcNode;

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
        List<CcNode> nodesFromString = LocalParserKt.createNodesFromString(json);
        for (CcNode ccNode : nodesFromString) {
            App.instance.repository.save(ccNode);
        }
    }

    public void onSave(ActionEvent actionEvent) {
        String json = LocalParserKt.createJsonFromNodes(App.instance.repository.getNodes());
        FileManager.getInstance().saveScene(json);
    }

    public void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
