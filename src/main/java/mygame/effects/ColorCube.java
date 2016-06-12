package mygame.effects;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * Created by Admin on 12.06.2016.
 */
public class ColorCube extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cube java.sample");

        Rectangle rectangle1 = new Rectangle(200,200, Color.GREEN);
        rectangle1.setTranslateZ(-100);

        Rectangle rectangle2 = new Rectangle(200,200, Color.RED);
        rectangle2.setTranslateZ(100);

        Rectangle rectangle3 = new Rectangle(200,200, Color.BLUE);
        rectangle3.setTranslateX(100);
        rectangle3.setRotationAxis(Rotate.Y_AXIS);
        rectangle3.setRotate(90);

        Rectangle rectangle4 = new Rectangle(200,200, Color.YELLOW);
        rectangle4.setTranslateX(-100);
        rectangle4.setRotationAxis(Rotate.Y_AXIS);
        rectangle4.setRotate(90);


        Rectangle rectangle5 = new Rectangle(200,200, Color.GRAY);
        Rectangle rectangle6 = new Rectangle(200,200, Color.AQUA);
        Box box = new Box(50,50,20);
        Group group = new Group(rectangle1,rectangle2,rectangle3,rectangle4,rectangle5,rectangle6 ,box);
        group.setTranslateX(100);
        group.setTranslateY(100);
        group.setRotationAxis(Rotate.Y_AXIS);
        Slider slider = new Slider(0,360,0);
        slider.setBlockIncrement(1);
        slider.setTranslateY(400);
        slider.setTranslateX(100);
        group.rotateProperty().bind(slider.valueProperty());
        Group root = new Group(group, slider);

        Scene scene = new Scene(root,500,500,true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
