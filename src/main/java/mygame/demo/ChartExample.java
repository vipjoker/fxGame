package mygame.demo;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * Created by oleh on 3/22/17.
 */
public class ChartExample extends Application {







        @Override public void start(Stage primaryStage) throws Exception {


            NumberAxis yAxis=new NumberAxis("yyyyy",0,10,50);
            NumberAxis xAxis=new NumberAxis("xxxxx",0,10,50);

            yAxis.setSide(Side.RIGHT);
            xAxis.setSide(Side.BOTTOM);

            Circle rectangle = new Circle(100, 100, 10,Color.BLUE.deriveColor(1,1,1,.5));

            Pane group = new Pane(rectangle);
            group.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));


            Pane root = new Pane(xAxis,yAxis);
            group.setBackground(new Background(new BackgroundFill(Color.RED.deriveColor(1,1,1,.25),null,null)));

            Scene scene = new Scene(root, 500, 500);

            root.getChildren().add(group);

            xAxis.upperBoundProperty().bind(group.widthProperty());
            yAxis.upperBoundProperty().bind(group.heightProperty());



            xAxis.prefWidthProperty().bind(root.widthProperty());
            yAxis.prefHeightProperty().bind(root.heightProperty());
            primaryStage.setScene(scene);

            Slider spinner = new Slider(.2,10,1);
            group.scaleXProperty().bind(spinner.valueProperty());
            group.scaleYProperty().bind(spinner.valueProperty());
            group.scaleXProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    double x = observable.getValue().doubleValue();
                    xAxis.setLowerBound(group.screenToLocal(0, 0).getX());
                    xAxis.setUpperBound(group.screenToLocal(root.getWidth(), 0).getX());

                }
            });
            spinner.setLayoutX(20);
            spinner.setLayoutY(100);
            root.getChildren().add(spinner);




            primaryStage.show();
        }

        /**
         * Java main for when running without JavaFX launcher
         */
        public static void main(String[] args) {
            launch(args);
        }
    }

