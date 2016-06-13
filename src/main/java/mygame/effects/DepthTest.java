package mygame.effects;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * Created by Admin on 12.06.2016.
 */
public class DepthTest extends Application {


    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        if(!Platform.isSupported(ConditionalFeature.SCENE3D)){
            throw new RuntimeException("ERROR");
        }

        primaryStage.setTitle("Depth buffer example");

        Rectangle red = new Rectangle(300,300, Color.RED);
        red.setTranslateX(-50);
        red.setTranslateY(-50);
        red.setTranslateZ(1);

        Rectangle green = new Rectangle(300,300,Color.GREEN);
        green.setTranslateX(100);
        green.setTranslateY(0);
        green.setTranslateZ(200);


        Rectangle blue = new Rectangle(300,300, Color.BLUE);
        blue.setTranslateX(0);
        blue.setTranslateY(100);
        blue.setTranslateZ(100);


        Group rotationGroup = new Group(red,green,blue);
        rotationGroup.setTranslateX(125);
        rotationGroup.setTranslateY(125);
        rotationGroup.setRotationAxis(Rotate.Y_AXIS);

        Slider s = new Slider(0,360,0);
        s.setBlockIncrement(1);
        s.setTranslateX(255);
        s.setTranslateY(575);
        rotationGroup.rotateProperty().bind(s.valueProperty());
        Group root = new Group(rotationGroup,s);
        Scene scene = new Scene(root,600,600,true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());
//        scene.setCamera(new ParallelCamera());
        primaryStage.setScene(scene);
        primaryStage.show();



    }
}
