package mygame.editor.views;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mygame.editor.util.Constants;

public class CcSprite extends CcNode {
    protected Image image;

    public CcSprite(Image image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        bBox = new BoundingBox(0, -height, width, height);

    }

    public CcSprite(Image image, double width, double height) {
        this.image = image;
        this.width = width;
        this.height = height;
        bBox = new BoundingBox(0, -height, width, height);
    }


    @Override
    public void rasterize(GraphicsContext context) {
        transform = context.getTransform();
        context.drawImage(image, bBox.getMinX(), bBox.getMinY(), bBox.getWidth(), bBox.getHeight());

        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.strokeRect(bBox.getMinX() -5, bBox.getMinY() -5, bBox.getWidth() +10, bBox.getHeight()+10);
            context.setFill(Color.RED);

            context.fillOval(-2.5, -2.5, 5, 5);
        }
    }

    @Override
    public boolean contains(double x, double y) {
        try {
            Point2D point2D = this.transform.inverseTransform(x, y);

            return bBox.contains(point2D);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }
}



