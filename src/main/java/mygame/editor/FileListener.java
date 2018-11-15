package mygame.editor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.javafx.scene.control.skin.TreeViewSkin;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class FileListener extends Application {


    private TreeItem<String> root;

    @Override
    public void start(Stage primaryStage) throws Exception  {
        primaryStage.setWidth(400);
        primaryStage.setHeight(400);

        TreeView treeView = new TreeView();
        treeView.setPrefWidth(200);
        VBox hBox = new VBox();
        final Button clear = new Button("Clear");
        clear.setOnMouseClicked(event -> {
            root.getChildren().clear();
        });

        hBox.getChildren().add(clear);
        hBox.setPrefWidth(200);
        primaryStage.setScene(new Scene(new HBox(treeView,hBox)));

        final JsonObject grandparrent = createObject("Grand parent");
        final JsonObject parent = createObject("Parent");
        final JsonObject child = createObject("Child");
        final JsonObject grandChild = createObject("Grand child");

        grandparrent.getAsJsonArray("children").add(parent);
        parent.getAsJsonArray("children").add(child);



        String [] array = {"One","Two","Three","Four"};
        root = new TreeItem<>("root");

        treeView.setRoot(root);





        List<Rectangle > rects = new ArrayList<>();
        for (String s : array) {
            TreeItem<String> item = new TreeItem<>(s);
            Rectangle rectangle = new Rectangle(0,0,100,40);
            rectangle.setFill(Color.PINK);
            item.setGraphic(rectangle);
            rects.add(rectangle);
            root.getChildren().add(item);
        }



        treeView.setOnMouseDragged(event -> {
            System.out.println("Touch " + event.getX() + " " + event.getY() + " size " + rects.size());
            for (Rectangle cell : rects) {
                if(cell.getParent() != null) {

                    for (Rectangle rect : rects) {

                        final Point2D point2D = rect.getParent().parentToLocal(event.getX(),event.getY());
                        final Point2D pointInRect = rect.parentToLocal(point2D);
                        if(rect.contains(pointInRect)){

                            rect.setStroke(Color.RED);
                        }else{
                            rect.setStroke(Color.TRANSPARENT);
                        }

                        System.out.println(point2D);
                    }
                }


            }
            System.out.println("************************************************");
        });

        primaryStage.show();
    }

    private JsonObject createObject(String name) {
        JsonObject object = new JsonObject();


        object.addProperty("name",name);
        object.add("children",new JsonArray());
        return object;
    }


    public static void main(String[] args) {
        launch(args);
    }


}
