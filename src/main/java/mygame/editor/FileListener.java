package mygame.editor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import mygame.editor.model.TreeFileHolder;

import java.util.ArrayList;
import java.util.List;


public class FileListener extends Application {


    private TreeView<TreeHolder> treeView;
    List<Rectangle> rects = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(400);
        primaryStage.setHeight(400);

        treeView = new TreeView<>();
        treeView.setPrefWidth(200);
        VBox hBox = new VBox();


        hBox.setPrefWidth(200);
        primaryStage.setScene(new Scene(new HBox(treeView, hBox)));


        TreeHolder grandParent = new TreeHolder("Grand parent");


        TreeHolder parent1 = new TreeHolder("Parent 1");
        TreeHolder parent2 = new TreeHolder("Parent 2");
        TreeHolder parent3 = new TreeHolder("Parent 3");
        TreeHolder child = new TreeHolder("Child");
        TreeHolder grandChild1 = new TreeHolder("Grandchild 1");
        TreeHolder grandChild2 = new TreeHolder("Grandchild 2");
        TreeHolder grandChild3 = new TreeHolder("Grandchild 3");
        grandParent.items.add(parent1);
        grandParent.items.add(parent2);
        grandParent.items.add(parent3);
        parent2.items.add(child);
        child.items.add(grandChild1);
        child.items.add(grandChild2);
        child.items.add(grandChild3);


        treeView.setCellFactory((v) -> new TreeItemTreeHolder());


        treeView.setOnMouseDragged(event -> {



            for (Rectangle cell : rects) {
                if (cell.getParent() != null) {


                    final Point2D point2D = cell.getParent().parentToLocal(event.getX(), event.getY());
                    final Point2D pointInRect = cell.parentToLocal(point2D);
                    if (cell.contains(pointInRect)) {

                        cell.setStroke(Color.RED);
                    } else {
                        cell.setStroke(Color.TRANSPARENT);
                    }

                    System.out.println(point2D);

                }


            }
            System.out.println("************************************************");
        });

        updateTreeView(grandParent);
        primaryStage.show();
    }


    private void updateTreeView(TreeHolder holder) {
        TreeItem<TreeHolder> root = new TreeItem<>(holder);
        treeView.setRoot(root);
        Rectangle rectangle = new Rectangle(0, 0, 40, 40);
        rectangle.setFill(Color.PINK);
        root.setGraphic(rectangle);
        rects.add(rectangle);
        // root.getChildren().add(item);

    }


    public static void main(String[] args) {
        launch(args);
    }


    static class TreeItemTreeHolder extends TextFieldTreeCell<TreeHolder> {
        @Override
        public void updateItem(TreeHolder item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {

                setText(item.value);
            }
        }
    }


    static class TreeHolder {


        public TreeHolder(String value) {
            this.value = value;
        }

        public void addChild() {

        }

        private String value;
        private List<TreeHolder> items = new ArrayList<>();
    }
}
