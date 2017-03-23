package mygame.demo;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class HelloWorld extends Application{

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Canvas Example");
        Group group = new Group();
        Scene scene = new Scene(group,800,800);
        scene.setFill(Color.GRAY);
        primaryStage.setScene(scene);
        Text text = new Text();
        text.setX(200);
        text.setY(200);
        text.setText("Hello");
        text.setStyle("-fx-font-size: 30px; -fx-border-color: cadetblue; -fx-stroke-width: 1px;-fx-background-color: coral");
        group.getChildren().add(text);
        addRect(group);
       addCamera(scene);
        primaryStage.show();

        //startThreads(text);
    }


    private void addCamera(Scene scene){
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-2000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000);
        camera.setFieldOfView(35);
        scene.setCamera(camera);
    }




    private void addRect(Group group){
        Box testBox = new Box(50,50,50);
        testBox.setMaterial(new PhongMaterial(Color.RED));
        testBox.setDrawMode(DrawMode.LINE);
        group.getChildren().add(testBox);


    }


    private Translate translate;
    private Rotate rotateX,rotateY,rotateZ;
    private void startHandle(){





                final PerspectiveCamera camera = new PerspectiveCamera(true);
                //scene.setCamera(camera);
                //root.getChildren().add(camera);
                // BOX

                // MOVE CAMERA
                camera.getTransforms().addAll(
                        rotateY = new Rotate(-20, Rotate.Y_AXIS),
                        rotateX = new Rotate(-20, Rotate.X_AXIS),
                        rotateZ = new Rotate(0, Rotate.Z_AXIS),
                        translate = new Translate(5, -5, -15)
                );
                // SHOW STAGE

                // CHECK FOR INPUT

//                    new AnimationTimer() {
//                        @Override public void handle(long l) {
//
//                                    switch(component.getName()) {
//                                        case "x":
//                                            translate.setX(translate.getX() + component.getPollData());
//                                            break;
//                                        case "y":
//                                            translate.setY(translate.getY()+component.getPollData());
//                                            break;
//                                        case "z":
//                                            translate.setZ(translate.getZ()+component.getPollData());
//                                            break;
//                                        case "rx":
//                                            rotateX.setAngle(rotateX.getAngle()+component.getPollData());
//                                            break;
//                                        case "ry":
//                                            rotateY.setAngle(rotateY.getAngle()+component.getPollData());
//                                            break;
//                                        case "rz":
//                                            rotateZ.setAngle(rotateZ.getAngle()+component.getPollData());
//                                            break;
//                                    }
//                                }
//                            }
//                        }
//                    }.start();
                }










    private void startThreads(Text text){
        new Thread(new Cinema(text)).start();
    }


}
