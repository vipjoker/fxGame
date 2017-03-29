package mygame.demo.kotlin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Admin on 30.05.2016.
 */
 class DragExample : Application(){


    override fun start(primaryStage:Stage) {
        val pane = CustonPane()
        val customScene = Scene(pane,600.0,600.0)


//        customScene.widthProperty().addListener { observableValue, old, new ->
//            pane.prefWidth = new.toDouble()
//        }
//        customScene.heightProperty().addListener { observableValue, number, new ->
//            pane.prefHeight = new.toDouble()
//        }
  with(primaryStage){
            scene = customScene
            show()
        }
    }

    fun run(){
        launch()
    }



//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        CustonPane custonPane = new CustonPane();
//        CustomPane group = new CustomPane();
//        group.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
//
//        primaryStage.setScene(new Scene(group,600,600));
//
//
//        primaryStage.show();
//        addGroup(group);
//    }
//
//
//    private void addGroup(Pane group){
//
//
//        MyGroup rectangle =new MyGroup(50,50);
//        Platform.runLater(()->{
//            rectangle.setLayoutX(group.getWidth()/2);
//            rectangle.setLayoutY(group.getHeight()/2);
//        });
//        group.getChildren().addAll(rectangle);
//        group.getScene().setOnMouseClicked(event -> {
//            Point2D point2D = group.sceneToLocal(event.getX(), event.getY());
//            rectangle.setPosition(point2D.getX(),point2D.getY());
//        });
//
//
//        group.setOnScroll(event -> {
//            double deltaY = event.getDeltaY();
//            double scale = group.getScaleX() + (deltaY > 1? -.01:.01);
//            group.setScaleX(scale);
//            group.setScaleY(scale);
//        });
//    }
//
//
//    public static void main(String[] args) {
//        launch();
//    }
//
//
//
//
//
//    class MyGroup extends Group{
//
//
//        double x;
//        double y;
//        Text text;
//        Rectangle rectangle;
//        public MyGroup (double width,double height){
//
//            rectangle = new Rectangle(width,height);
//
//            rectangle.setFill(Color.TRANSPARENT);
//            rectangle.setStroke(Color.BLACK);
//            text = new Text("");
//            text.setY(height/2);
//
//            getChildren().addAll(rectangle,text);
//
//
//
//        }
//
//        public void setPosition(double x,double y ){
//            text.setX(x - getLayoutX());
//            text.setY(y - getLayoutY());
//            text.setText(String.format("x %.2f y %.2f",x,y));
//
//           rectangle.setX(x - rectangle.getWidth()/2 -getLayoutX());
//           rectangle.setY(y - rectangle.getHeight()/2 - getLayoutY());
//        }
//
//        public double getX() {
//            return x;
//        }
//
//        public double getY() {
//            return y;
//        }
//    }

}

fun main(args:Array<String>){
  DragExample().run()

}
