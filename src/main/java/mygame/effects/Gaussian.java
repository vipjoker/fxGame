package mygame.effects;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import mygame.util.ImageUtil;

public class Gaussian  extends Application {
        private static final Image ICON_48 = ImageUtil.getImage("earth.jpg");
        private void init(Stage primaryStage) {
            Group root = new Group();
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, 48,48));
            ImageView sample = new ImageView(ICON_48);
            final GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(8d);
            sample.setEffect(gaussianBlur);
            root.getChildren().add(sample);
        }

        public double getSampleWidth() { return 48; }

        public double getSampleHeight() { return 48; }

        @Override public void start(Stage primaryStage) throws Exception {
            init(primaryStage);
            primaryStage.show();
        }
        public static void main(String[] args) { launch(args); }
    }

