package mygame.demo;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import mygame.editor.DrawerPane;


public class ChartExample extends Application {


    boolean isOpen = true;
    boolean rightIsOpen = true;
    @Override
    public void start(Stage primaryStage) throws Exception {

DrawerPane pane444 = new DrawerPane();
        Pane root = new Pane();


        Pane pane = new Pane();
        pane.prefWidth(100);
        pane.prefHeight(50);
        pane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));


        root.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));


        VBox lef = new VBox();

        lef.alignmentProperty().setValue(Pos.CENTER);
        lef.spacingProperty().setValue(10);
        lef.setBackground(new Background(new BackgroundFill(Color.BLACK.deriveColor(1, 1, 1, .5f), null, null)));


        VBox right = new VBox();

        right.alignmentProperty().setValue(Pos.CENTER);
        right.spacingProperty().setValue(10);
        right.setPadding(new Insets(0,0,0,20));
        right.setBackground(new Background(new BackgroundFill(Color.BLACK.deriveColor(1, 1, 1, .5f), null, null)));
        right.setPrefWidth(200);
        right.alignmentProperty().setValue(Pos.CENTER);


        TreeItem<String> tree = new TreeItem<>("Root");
        tree.setExpanded(true);

        TreeItem<String> item = new TreeItem<>("Body");
        item.getChildren().addAll(new TreeItem<String>("Fi"));


        TreeItem<String> item2 =  new TreeItem<>("Joint");



        tree.getChildren().addAll(item,item2);


        TreeView<String> treeView = new TreeView<>(tree);




        right.getChildren().addAll(

                new Button("One"),
                new Button("Two"),
                new Button("Three")
                , treeView
        );


        AnchorPane center = new AnchorPane();


        Button leftButton = new Button("texone wfwf");
        Button rightButton = new Button("texone wfwf");

        AnchorPane.setRightAnchor(leftButton, 0.0);
        AnchorPane.setLeftAnchor(rightButton, 0.0);
        center.getChildren().addAll(
                leftButton, rightButton);


        center.setBackground(new Background(new BackgroundFill(Color.WHEAT.deriveColor(1, 1, 1, .25f), null, null)));


        center.setPrefHeight(100);


        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            double l = lef.getWidth();
            double r = right.getWidth();
            if(!isOpen) l -= 180;
            if(!rightIsOpen)r -= 180;
            center.setPrefWidth(newValue.doubleValue() - l - r);



            right.setLayoutX(root.getWidth() - right.getWidth());
        });


        lef.translateXProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue() - oldValue.doubleValue();
            center.setLayoutX(center.getLayoutX() + width);
            center.setPrefWidth((center.getWidth() - width));
        });

        right.translateXProperty().addListener(((observable, oldValue, newValue) -> {
            double width = newValue.doubleValue() - oldValue.doubleValue();
            center.setPrefWidth(center.getWidth() + width);

        }));


        lef.getChildren().addAll(
                new Button("Button one"),
                new Button("Button two"),
                new Button("Button three"),
                new Button("Button four"));
        lef.setPrefWidth(200);
        lef.prefHeightProperty().bind(root.heightProperty());
        right.prefHeightProperty().bind(root.heightProperty());
        root.getChildren().addAll(lef, center, right);


        lef.setOnMouseClicked(event -> {
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), lef);
            translateTransition.setToX(isOpen ? -180 : 0);
            translateTransition.play();
            isOpen = !isOpen;
        });

        right.setOnMouseClicked(event -> {
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500),right);
            translateTransition.setToX(rightIsOpen ? 180 : 0);
            translateTransition.play();
            rightIsOpen = !rightIsOpen;
        });

        primaryStage.setScene(new Scene(pane444, 600, 600));

        Platform.runLater(() -> {
            center.setLayoutX(lef.getWidth());
            center.setPrefWidth((root.getWidth() - lef.getWidth() - right.getWidth()));
            right.setLayoutX(root.getWidth() - right.getWidth());

        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

