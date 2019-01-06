package mygame.editor.controlers;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mygame.editor.App;
import mygame.editor.model.Command;
import mygame.editor.model.Node;
import mygame.editor.model.TreeNodeHolder;
import mygame.editor.util.Callback;
import mygame.editor.views.NodeView;

import java.net.URL;
import java.util.ResourceBundle;

public class HierarchyController implements Initializable {
    public TreeView<Node> nodeTreeview;


    private TreeItem<Node> nodesItem;
    private Node rootNode;
    private Node lastCell;
    private Node firstCell;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTreeView();
        App.instance.observableAction.addListener(this::onActionChange);
        App.instance.repository.listenForNodes(this::onNodesChanged);
        nodeTreeview.setOnMousePressed(this::onMousePressed);
        nodeTreeview.setOnMouseDragged(this::onMouseDragged);
        nodeTreeview.setOnMouseReleased(this::onMouseReleased);
        setupContextMenu();
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem delete_item = new MenuItem("Delete item");
        delete_item.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                TreeItem<Node> selectedItem = nodeTreeview.getSelectionModel().getSelectedItem();

                if(selectedItem != null){
                    App.instance.repository.delete(selectedItem.getValue());
                    selectedItem.getValue().removeSelf();

                }
            }
        });

        contextMenu.getItems().add(delete_item);

        nodeTreeview.setOnContextMenuRequested(event -> {
            contextMenu.show(nodeTreeview,event.getScreenX(),event.getScreenY());
        });

    }

    private void onNodesChanged(ListChangeListener.Change<? extends Node> change) {
        nodesItem.getChildren().clear();
        for (Node node : change.getList()) {
           traversTreeItems(node,nodesItem);
//
        }
    }


    private void traversTreeItems(Node node, TreeItem<Node> parent){
        TreeItem<Node> treeItem = createTreeItem(node);
        parent.getChildren().add(treeItem);
        for(Node child : node.getChildren()) {
            traversTreeItems(child,treeItem);
        }
    }

    private TreeItem<Node> createTreeItem(Node node){
        TreeItem<Node> treeItem = new TreeItem<>(node);
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        rectangle.setFill(Color.PINK);
        treeItem.setGraphic(rectangle);
        return treeItem;
    }

    private void traverse(Node ccNode, Callback<Node> callback){
        callback.call(ccNode);
        for (Node node : ccNode.getChildren()) {
            traverse(node, callback);
        }
    }


    private void initTreeView() {

        nodeTreeview.setCellFactory(e -> new TreeNodeHolder());

        rootNode = new Node();
        rootNode.setName("Nodes");


        nodesItem = new TreeItem<>(rootNode);
        nodeTreeview.setRoot(nodesItem);

    }

    //************************************************************************


    private void onMousePressed(MouseEvent event) {





        final TreeItem<Node> selectedItem = nodeTreeview.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            final Node value = selectedItem.getValue();
            if(value != rootNode){
                App.instance.selected.clear();
                App.instance.selected.add(value);
            }
        }
        final TreeItem<Node> root = nodeTreeview.getRoot();
        traverse(root, treeItem -> {

            final Rectangle cell = (Rectangle) treeItem.getGraphic();

            if (cell != null && cell.getParent() != null) {


                final Point2D point2D = cell.getParent().parentToLocal(event.getX(), event.getY());
                final Point2D pointInRect = cell.parentToLocal(point2D);
                if (cell.contains(pointInRect)) {

                    cell.setFill(Color.SKYBLUE);
                    firstCell = treeItem.getValue();
                }
                System.out.println(point2D);
            } else {
                if (cell == null) {
                    System.out.println("Cell is null");
                } else if (cell.getParent() == null) {
                    System.out.println("Parent is null");
                }
            }
        });
    }

    private void onMouseDragged(MouseEvent event) {
        final TreeItem<Node> root = nodeTreeview.getRoot();
        traverse(root, treeItem -> {

            final Rectangle cell = (Rectangle) treeItem.getGraphic();

            if (cell != null && cell.getParent() != null) {


                final Point2D point2D = cell.getParent().parentToLocal(event.getX(), event.getY());
                final Point2D pointInRect = cell.parentToLocal(point2D);
                if (cell.contains(pointInRect)) {

                    cell.setFill(Color.RED);
                } else {
                    cell.setFill(Color.GREEN);
                }
                System.out.println(point2D);
            } else {
                if (cell == null) {
                    System.out.println("Cell is null");
                } else if (cell.getParent() == null) {
                    System.out.println("Parent is null");
                }
            }
        });

    }

    private void onMouseReleased(MouseEvent event) {
        TreeItem<Node> root = nodeTreeview.getRoot();
        traverse(root, treeItem -> {
            final Rectangle cell = (Rectangle) treeItem.getGraphic();
            if (cell != null && cell.getParent() != null) {


                final Point2D point2D = cell.getParent().parentToLocal(event.getX(), event.getY());
                final Point2D pointInRect = cell.parentToLocal(point2D);
                cell.setFill(Color.GREEN);
                if (cell.contains(pointInRect)) {
                    cell.setFill(Color.YELLOW);
                    lastCell = treeItem.getValue();
                }


            } else {
                if (cell == null) {
                    System.out.println("Cell is null");
                } else if (cell.getParent() == null) {
                    System.out.println("Parent is null");
                }
            }
        });

        if (firstCell != null && lastCell != null && firstCell != rootNode && firstCell != lastCell) {
            firstCell.removeSelf();
           // App.instance.repository.delete(firstCell);

            lastCell.addChild(firstCell);
            for (Node ccNode : rootNode.getChildren()) {
                ccNode.removeSelf();
            }

            for (Node ccNode : App.instance.repository.getNodes()) {
//                rootNode.addChild(ccNode);

            }
            nodeTreeview.setRoot(updateTreeView(rootNode));

        }

        firstCell = null;
        lastCell = null;


    }


    private void traverse(TreeItem<Node> treeItem, Callback<TreeItem<Node>> callback) {
        callback.call(treeItem);
        for (TreeItem<Node> holderTreeItem : treeItem.getChildren()) {
            traverse(holderTreeItem, callback);
        }
    }


    private TreeItem<Node> updateTreeView(Node holder) {
        TreeItem<Node> root = new TreeItem<>(holder);
        for (Node item : holder.getChildren()) {
            root.getChildren().add(updateTreeView(item));
        }

        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        rectangle.setFill(Color.PINK);
        root.setGraphic(rectangle);

        return root;
    }

//************************************************************************

    public static class Holder {
        public final String name;
        public final Object object;
        public final Class clazz;

        public Holder(String name, Object object, Class clazz) {
            this.name = name;
            this.object = object;
            this.clazz = clazz;
        }
    }

    private void onActionChange(ObservableValue<? extends Command> observableValue, Command oldValue, Command newValue) {
        System.out.println("Hierarchy " + newValue);
    }


}
