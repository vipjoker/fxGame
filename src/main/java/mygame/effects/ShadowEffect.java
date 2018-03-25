package mygame.effects;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.function.Consumer;

import static java.lang.Math.random;

/**
 * Created by Admin on 12.06.2016.
 */
public class ShadowEffect  extends Application{

    public static void main(String[] args) {
        launch(args);
    }




    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Image shadow");
        Group root = new Group();
        Image image = new Image (this.getClass().getResourceAsStream("/earth.jpg"));
        Image image1 = new Image(this.getClass().getResourceAsStream("/sprites.png"));

        ImageView imageView2 = new ImageView(image1);
        imageView2.setViewport(new Rectangle2D(0,0,300,400));
        ImageView imageView = new ImageView(image1);
        imageView.setOpacity(0.2);

        Rectangle rectangle = new Rectangle(200,200);
        Paint paint = new Color(1,0,0,1);

        rectangle.setStroke(paint);
        rectangle.setFill(Color.TRANSPARENT);




        Circle positionHandler = getHandler();
        rectangle.xProperty().bind(positionHandler.centerXProperty());
        rectangle.yProperty().bind(positionHandler.centerYProperty());

        EventHandler<MouseEvent> dragListener= e -> {
            Circle  c = (Circle) e.getTarget();
            c.setCenterX(e.getX());
            c.setCenterY(e.getY());

        };


        positionHandler.setOnMouseDragged(dragListener);

        Circle circle = getHandler();
        rectangle.widthProperty().bind(circle.centerXProperty());
        rectangle.heightProperty().bind(circle.centerYProperty());
        circle.setOnMouseDragged(dragListener);


        root.getChildren().addAll(imageView,imageView2,rectangle,positionHandler,circle);
        Scene scene = new Scene(root, 800, 800);


        scene.setFill(Color.CYAN);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Circle getHandler (){
        Circle c = new Circle(10);
        c.setFill(Color.GREEN);
        return c;
    }
}
