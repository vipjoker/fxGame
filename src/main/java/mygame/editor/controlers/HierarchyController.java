package mygame.editor.controlers;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
    private List<B2Body> bodies ;
    private List<B2Body> joint;



    private Map<String, Object> map = new HashMap<>();
    private TreeItem<Holder> bodiesItem;
    private TreeItem<Holder> jointsItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateNodeTreeView();
        App.instance.observableAction.addListener(this::onActionChange);
        App.instance.repository.listenForBodies(this::onBodiesChanged);
        App.instance.repository.listenForJoints(this::onJointsChanged);
    }



    private void onBodiesChanged(ListChangeListener.Change<? extends B2Body> change) {
        bodiesItem.getChildren().clear();
        for (B2Body body : change.getList()) {
            Holder holder = new Holder(body.getName(),body,B2Body.class);
            TreeItem<Holder> treeItem = new TreeItem<>(holder);
            bodiesItem.getChildren().add(treeItem);
        }
    }

    private void onJointsChanged(ListChangeListener.Change<? extends B2Joint> change) {
        System.out.println(change);
    }


    private void updateNodeTreeView() {

        nodeTreeview.setCellFactory(e->new TreeItemHolder());


        Holder holder = new Holder("Root",null,null);
        Holder bodies = new Holder("Bodies",null,null);
        Holder joints = new Holder("Joints",null,null);
        TreeItem<Holder> root = new TreeItem<>(holder);
        nodeTreeview.setRoot(root);

        bodiesItem = new TreeItem<>(bodies);
        jointsItem = new TreeItem<>(joints);
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
    private void onActionChange(ObservableValue<? extends Command> observableValue, Command oldValue, Command newValue) {
        System.out.println("Hierarchy " + newValue);
    }

}
