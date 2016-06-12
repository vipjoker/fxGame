package mygame.effects;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.*;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

/**
 * Created by Admin on 12.06.2016.
 */
public class CoordinateSystemDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Java fx coordinate system demo");
        Group root = new Group();
        Sphere sphere = new Sphere(50);
        Material material = new PhongMaterial(Color.RED);
        sphere.setMaterial(material);
        sphere.setTranslateX(100);
        sphere.setTranslateY(100);
        sphere.setTranslateZ(100);

        Cylinder cylinder = new Cylinder(10,50);
        cylinder.setTranslateX(300);
        cylinder.setTranslateY(300);

        root.getChildren().addAll(sphere,cylinder);
        Scene scene = new Scene(root,600,600,true , SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera();

        scene.setCamera(camera);

        LinearGradient gradient = new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,
                new Stop[]{
                   new Stop(0, Color.web("#e0e0e0")),
                   new Stop(1, Color.web("#a0a0a0"))
                });
        scene.setFill(gradient);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
