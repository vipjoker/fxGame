package mygame.editor.controlers;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import mygame.editor.App;
import mygame.editor.model.Command;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.model.box2d.B2Joint;
import mygame.editor.render.TreeItemHolder;
import mygame.editor.views.CcNode;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class HierarchyController implements Initializable {
    public TreeView<Holder> nodeTreeview;



    private Map<String, Object> map = new HashMap<>();
    private TreeItem<Holder> nodesItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateNodeTreeView();
        App.instance.observableAction.addListener(this::onActionChange);
        App.instance.repository.listenForNodes(this::onNodesChanged);
        nodeTreeview.setOnMousePressed(this::onTreePressed);
    }

    private void onNodesChanged(ListChangeListener.Change<? extends CcNode> change) {
        nodesItem.getChildren().clear();
        for (CcNode node : change.getList()) {
            Holder holder = new Holder(node.getName().get(),node,CcNode.class);
            TreeItem<Holder> treeItem = new TreeItem<>(holder);
            nodesItem.getChildren().add(treeItem);
        }
    }

    private void onTreePressed(MouseEvent event) {

        TreeItem<Holder> selectedItem = nodeTreeview.getSelectionModel().getSelectedItem();

        if(selectedItem != null && CcNode.class == selectedItem.getValue().clazz){

            CcNode node = (CcNode)selectedItem.getValue().object;
            App.instance.selected.clear();
            App.instance.selected.add(node);
        }
    }







    private void updateNodeTreeView() {

        nodeTreeview.setCellFactory(e->new TreeItemHolder());


        Holder holder = new Holder("Nodes",null,null);

        nodesItem = new TreeItem<>(holder);
        nodeTreeview.setRoot(nodesItem);

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
    private void onActionChange(ObservableValue<? extends Command> observableValue, Command oldValue, Command newValue) {
        System.out.println("Hierarchy " + newValue);
    }

}
