package mygame.editor.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.util.Constants;

public class CcRectangle extends CcNode {

    public CcRectangle(double x, double y, double width, double height) {
        this.x.set(x);
        this.y.set(y);
        this.width = width;
        this.height = height;

    }


    @Override
    public void rasterize(GraphicsContext context) {
        transform = context.getTransform();
        context.setFill(Constants.RED.deriveColor(1, 1, 1, 0.5));
        context.fillRect(bBox.getMinX(), bBox.getMinY(), bBox.getWidth(), bBox.getHeight());
        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.setFill(Color.RED.deriveColor(1, 1, 1, 0.5));
            context.strokeRect(bBox.getMinX(), bBox.getMinY(), bBox.getWidth(), bBox.getHeight());
            context.fillOval(-2.5, -2.5, 5, 5);
        } else {
            context.setStroke(Constants.RED);
            context.strokeRect(bBox.getMinX(), bBox.getMinY(), bBox.getWidth(), bBox.getHeight());
        }
    }

    @Override
    public boolean contains(Point2D point) {
        try {
            Point2D transform = this.transform.inverseTransform(point);

            return bBox.contains(transform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}

