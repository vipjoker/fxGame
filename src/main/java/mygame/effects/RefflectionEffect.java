package mygame.effects;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mygame.util.ImageUtil;

public class RefflectionEffect extends Application {
    private static final Image BOAT = ImageUtil.getImage("earth.jpg");

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 100, 200));
        ImageView sample = new ImageView(BOAT);
        sample.setPreserveRatio(true);
        sample.setFitHeight(100);
        final Reflection reflection = new Reflection();
        sample.setEffect(reflection);
        root.getChildren().add(sample);
    }

    public double getSampleWidth() {
        return 100;
    }

    public double getSampleHeight() {
        return 200;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


