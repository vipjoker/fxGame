package mygame.editor.controlers;

import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import mygame.editor.render.TreeItemCcNode;
import mygame.editor.views.CcNode;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HierarchyController implements Initializable {
    public TreeView<CcNode> nodeTreeview;
    private Map<String, Object> map = new HashMap<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CcNode node1 = new CcNode();
        node1.name = "FIRST";
        CcNode node2 = new CcNode();
        node2.name = "Second";
        CcNode node3 = new CcNode();
        node3.name = "Third";
        CcNode node4 = new CcNode();
        node4.name = "Fourth";
        node1.addChild(node2);
        node2.addChild(node3);
        node2.addChild(node4);
        updateNodeTreeView(node1);
    }

    private void updateNodeTreeView(CcNode node) {

        nodeTreeview.setCellFactory(e->new TreeItemCcNode());
        TreeItem<CcNode> ccNodeTreeItem = new TreeItem<>(node);
        nodeTreeview.setRoot(ccNodeTreeItem);
        nodeTreeview.setOnMouseDragged(e->{
            TreeItem<CcNode> ccNodeTreeItem1 = nodeTreeview.getSelectionModel().getSelectedItems().get(0);

        });
        fillNodeTreeView(ccNodeTreeItem,node);

    }
    private void fillNodeTreeView(TreeItem<CcNode> ccNodeTreeItem ,CcNode root) {
        for (CcNode node : root.getChildren()) {
            TreeItem<CcNode> item = new TreeItem<>(node);

            ;



            ccNodeTreeItem.getChildren().add(item);
            if(!node.getChildren().isEmpty()){
                fillNodeTreeView(item,node);
            }


        }
    }

}
