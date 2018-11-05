package mygame.editor.controlers;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import mygame.editor.App;
import mygame.editor.render.TreeItemHolder;
import mygame.editor.views.CcNode;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HierarchyController implements Initializable {
    public TreeView<Holder> nodeTreeview;
    private Map<String, Object> map = new HashMap<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateNodeTreeView();
        App.instance.observableAction.addListener(this::onActionChange);
    }


    private void updateNodeTreeView() {

        nodeTreeview.setCellFactory(e->new TreeItemHolder());


        Holder holder = new Holder("Root",null,null);
        Holder bodies = new Holder("Bodies",null,null);
        Holder joints = new Holder("Joints",null,null);
        TreeItem<Holder> root = new TreeItem<>(holder);
        nodeTreeview.setRoot(root);

        TreeItem<Holder> bodiesItem = new TreeItem<>(bodies);
        TreeItem<Holder> jointsItem = new TreeItem<>(joints);
        root.getChildren().addAll(bodiesItem, jointsItem);

    }
    private void fillNodeTreeView(TreeItem<CcNode> ccNodeTreeItem ,CcNode root) {
        for (CcNode node : root.getChildren()) {
            TreeItem<CcNode> item = new TreeItem<>(node);

            ccNodeTreeItem.getChildren().add(item);
            if(!node.getChildren().isEmpty()){
                fillNodeTreeView(item,node);
            }


        }
    }

    public static class Holder {
        public final String name;
        public final Object object;
        public final Class clazz;
        public Holder(String name,Object object,Class clazz){
            this.name = name;
            this.object =object;
            this.clazz = clazz;
        }
    }
    private void onActionChange(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        System.out.println("Hierarchy " + newValue);
    }

}
