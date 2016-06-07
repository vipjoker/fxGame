package mygame.effects;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mygame.util.ImageUtil;

/**
 * Created by Admin on 07.06.2016.
 */
public class SepiaEffect extends Application {
        private static final Image BOAT = ImageUtil.getImage("earth.jpg");
        private void init(Stage primaryStage) {
            Group root = new Group();
            primaryStage.setScene(new Scene(root));
            ImageView sample = new ImageView(BOAT);
            final SepiaTone sepiaTone = new SepiaTone();
            sepiaTone.setLevel(0.5d);
            sample.setEffect(sepiaTone);
            root.getChildren().add(sample);
        }

        @Override public void start(Stage primaryStage) throws Exception {
            init(primaryStage);
            primaryStage.show();
        }
        public static void main(String[] args) { launch(args); }
    }

