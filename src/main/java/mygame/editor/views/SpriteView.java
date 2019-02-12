package mygame.editor.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mygame.editor.manager.FileManager;
import mygame.editor.util.Constants;

import java.io.File;

public class SpriteView extends NodeView {
    protected Image image;
    private String imagePath;

    public SpriteView(Image image) {
        this.image = image;

        width.set(image.getWidth());
        height.set(image.getHeight());

    }

    public SpriteView(String path, double width, double height) {
        this(path);
        this.width.set(width);
        this.height.set(height);
    }

    public SpriteView(String path){
        this.imagePath = path;
        final File f = new File(FileManager.getInstance().getWorkingFolder(), path);
        this.image = new Image(f.toURI().toString());
    }


    @Override
    public void rasterize(GraphicsContext context) {
        transform = context.getTransform();

        double h = -height.doubleValue();
        double w = width.doubleValue();
        context.drawImage(image, - w * anchorX.doubleValue(), h - (h * anchorY.doubleValue()), width.doubleValue(), height.doubleValue());

        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.strokeRect( - 5  , - 5  - height.getValue(), width.getValue() +10,   height.doubleValue()  + 10);
            context.setFill(Color.RED);
            context.fillOval(-2.5, -2.5, 5, 5);
        }
    }


    @Override
    public boolean contains(Point2D point) {
        final Point2D point2D = convertToLocalSpace(point);
        final double x = point2D.getX();
        final double y = point2D.getY();

        boolean xMatch = x >= 0 && x <= this.width.doubleValue();
        boolean yMatch = y >= 0 && y <= this.height.doubleValue();
        return xMatch && yMatch;

    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public String getImagePath() {
        return imagePath;
    }
}



