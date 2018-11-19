package mygame.editor;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import physicsPort.Action;

import java.util.ArrayList;
import java.util.List;


public class FileListener extends Application {


    private TreeView<TreeHolder> treeView;
    private TreeHolder rootHolder;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(400);
        primaryStage.setHeight(400);

        treeView = new TreeView<>();
        treeView.setPrefWidth(200);
        VBox hBox = new VBox();


        hBox.setPrefWidth(200);
        primaryStage.setScene(new Scene(new HBox(treeView, hBox)));


        rootHolder = new TreeHolder("Grand parent");


        TreeHolder parent1 = new TreeHolder("Parent 1");
        TreeHolder parent2 = new TreeHolder("Parent 2");
        TreeHolder parent3 = new TreeHolder("Parent 3");
        TreeHolder child = new TreeHolder("Child");
        TreeHolder grandChild1 = new TreeHolder("Grandchild 1");
        TreeHolder grandChild2 = new TreeHolder("Grandchild 2");
        TreeHolder grandChild3 = new TreeHolder("Grandchild 3");
        rootHolder.addChild(parent1);
        rootHolder.addChild(parent2);
        rootHolder.addChild(parent3);
        parent2.addChild(child);
        child.addChild(grandChild1);
        child.addChild(grandChild2);
        child.addChild(grandChild3);


        treeView.setCellFactory((v) -> new TreeItemTreeHolder());


        treeView.setOnMouseDragged(this::onMouseDragged);
        treeView.setOnMouseReleased(this::onMouseReleased);
        treeView.setOnMousePressed(this::onMousePressed);
        treeView.setRoot(updateTreeView(rootHolder));
        primaryStage.show();
    }
    TreeHolder lastCell;
    TreeHolder firstCell;
    private void onMousePressed(MouseEvent event) {
        final TreeItem<TreeHolder> root = treeView.getRoot();
        traverse(root,treeItem->{

            final Rectangle cell = (Rectangle) treeItem.getGraphic();

            if (cell!= null &&  cell.getParent() != null) {


                final Point2D point2D = cell.getParent().parentToLocal(event.getX(), event.getY());
                final Point2D pointInRect = cell.parentToLocal(point2D);
                if (cell.contains(pointInRect)) {

                    cell.setFill(Color.SKYBLUE);
                    firstCell = treeItem.getValue();
                }
                System.out.println(point2D);
            }else{
                if(cell == null){
                    System.out.println("Cell is null");
                }else if(cell.getParent() == null){
                    System.out.println("Parent is null");
                }
            }
        });
    }

    private void onMouseDragged(MouseEvent event){
        final TreeItem<TreeHolder> root = treeView.getRoot();
        traverse(root,treeItem->{

            final Rectangle cell = (Rectangle) treeItem.getGraphic();

            if (cell!= null &&  cell.getParent() != null) {


                final Point2D point2D = cell.getParent().parentToLocal(event.getX(), event.getY());
                final Point2D pointInRect = cell.parentToLocal(point2D);
                if (cell.contains(pointInRect)) {

                    cell.setFill(Color.RED);
                } else {
                    cell.setFill (Color.GREEN);
                }
                System.out.println(point2D);
            }else{
                if(cell == null){
                    System.out.println("Cell is null");
                }else if(cell.getParent() == null){
                    System.out.println("Parent is null");
                }
            }
        });

    }

    private void onMouseReleased(MouseEvent event){
        TreeItem<TreeHolder> root = treeView.getRoot();

        traverse(root,treeItem->{
            final Rectangle cell = (Rectangle) treeItem.getGraphic();
            if (cell!= null &&  cell.getParent() != null) {


                final Point2D point2D = cell.getParent().parentToLocal(event.getX(), event.getY());
                final Point2D pointInRect = cell.parentToLocal(point2D);
                cell.setFill (Color.GREEN);
                if (cell.contains(pointInRect)) {
                    cell.setFill(Color.YELLOW);
                    lastCell = treeItem.getValue();
                }


            }else{
                if(cell == null){
                    System.out.println("Cell is null");
                }else if(cell.getParent() == null){
                    System.out.println("Parent is null");
                }
            }
        });

        if(firstCell != null && lastCell != null && firstCell != rootHolder){
            firstCell.removeFromParent();
            lastCell.addChild(firstCell);
            treeView.setRoot(updateTreeView(rootHolder));

        }

        firstCell = null;
        lastCell = null;


    }



    private void traverse(TreeItem<TreeHolder> treeItem, Action<TreeItem<TreeHolder>> action){

        action.call(treeItem);
        for(TreeItem<TreeHolder> holderTreeItem :treeItem.getChildren()){
            traverse(holderTreeItem,action);
        }
    }


    private TreeItem<TreeHolder> updateTreeView(TreeHolder holder) {
        TreeItem<TreeHolder> root = new TreeItem<>(holder);
        for (TreeHolder item : holder.getChildren()) {
            root.getChildren().add(updateTreeView(item));
        }

        Rectangle rectangle = new Rectangle(0, 0, 40, 40);
        rectangle.setFill(Color.PINK);
        root.setGraphic(rectangle);

        return root;
    }


    public static void main(String[] args) {
        launch(args);
    }


    static class TreeItemTreeHolder extends TextFieldTreeCell<TreeHolder> {
        @Override
        public void updateItem(TreeHolder item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getValue());
            }
        }
    }



}
