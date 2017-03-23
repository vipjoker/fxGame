package mygame.effects;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageOperator extends Application {

    SimpleDoubleProperty gridSize = new SimpleDoubleProperty(3.0);
    SimpleDoubleProperty hueFactor = new SimpleDoubleProperty(12.0);
    SimpleDoubleProperty hueOffset = new SimpleDoubleProperty(240.0);

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
        final WritableImage img = new WritableImage(300, 300);
        gridSize.addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                renderImage(img, gridSize.doubleValue(), hueFactor.doubleValue(), hueOffset.doubleValue());
            }
        });
        hueFactor.addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                renderImage(img, gridSize.doubleValue(), hueFactor.doubleValue(), hueOffset.doubleValue());
            }
        });
        hueOffset.addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                renderImage(img, gridSize.doubleValue(), hueFactor.doubleValue(), hueOffset.doubleValue());
            }
        });
        renderImage(img, 3.0, 12.0, 240.0);

        ImageView view = new ImageView(img);
        root.getChildren().add(view);
    }

    private static void renderImage(WritableImage img, double gridSize, double hueFactor, double hueOffset) {
        PixelWriter pw = img.getPixelWriter();
        double w = img.getWidth();
        double h = img.getHeight();
        double xRatio = 0.0;
        double yRatio = 0.0;
        double hue = 0.0;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                xRatio = x / w;
                yRatio = y / h;
                hue = Math.sin(yRatio * (gridSize * Math.PI)) * Math.sin(xRatio * (gridSize * Math.PI)) * Math.tan(hueFactor / 20.0) * 360.0 + hueOffset;
                Color c = Color.rgb(1, 1, 1,1.0);
                pw.setColor(x, y, c);
            }
        }
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

