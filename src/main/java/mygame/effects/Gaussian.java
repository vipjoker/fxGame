package mygame.effects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mygame.editor.util.ImageUtil;

public class Gaussian  extends Application {
        private static final Image ICON_48 = ImageUtil.getImage("earth.jpg");
        private void init(Stage primaryStage) {
            VBox root = new VBox();
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, 500,500));
            ImageView sample = new ImageView(ICON_48);
            GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(8d);

            sample.setEffect(gaussianBlur);
            Glow glow = new Glow();
            glow.setLevel(2.0);
            Slider slider = new Slider();
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setMax(10);
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(4);

            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                gaussianBlur.setRadius(newValue.doubleValue());
                sample.setEffect(gaussianBlur);
            });


            Slider shadowSlider = new Slider();
            shadowSlider.setShowTickLabels(true);
            shadowSlider.setShowTickMarks(true);
            shadowSlider.setMax(10);
            shadowSlider.setMajorTickUnit(1);
           shadowSlider.setMinorTickCount(4);

           shadowSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
               glow.setLevel(newValue.doubleValue());
               sample.setEffect(glow);
           });



            root.getChildren().addAll(sample,slider,shadowSlider);

        }


        @Override public void start(Stage primaryStage) throws Exception {
            init(primaryStage);
            primaryStage.show();
        }
        public static void main(String[] args) { launch(args); }
    }

