package mygame.editor.views;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import mygame.editor.manager.FileManager;
import mygame.editor.model.Point;
import mygame.editor.model.Rect;
import mygame.editor.util.Constants;

import java.io.File;

public class CcSprite extends CcNode {
    protected Image image;
    private String imagePath;
    private Rect bounds;

    public CcSprite(Image image) {
        this.image = image;

        width.set(image.getWidth());
        height.set(image.getHeight());

    }

    public CcSprite(String path, double width, double height) {
        this.imagePath = path;
        final File f = new File(FileManager.getInstance().getWorkingFolder(), path);

        this.image = new Image(f.toURI().toString());
        this.width.set(width);
        this.height.set(height);
        bounds = new Rect(x, y, this.width, this.height);
    }


    @Override
    public void rasterize(GraphicsContext context) {
        transform = context.getTransform();

        double imageY = -height.doubleValue();
        context.drawImage(image, 0, imageY, width.doubleValue(), height.doubleValue());

        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.strokeRect(bBox.getMinX() - 5, bBox.getMinY() - 5, bBox.getWidth() + 10, bBox.getHeight() + 10);
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



