package mygame.editor.views;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mygame.editor.model.Point;
import mygame.editor.util.Constants;

public class CcSprite extends CcNode {
    protected Image image;

    public CcSprite(Image image) {
        this.image = image;
        width.set(image.getWidth());
        height.set(image.getHeight());
        updateBoundingBox();

    }

    public CcSprite(Image image, double width, double height) {
        this.image = image;
        this.width.set(width);
        this.height.set(height);
        updateBoundingBox();
    }


    @Override
    public void rasterize(GraphicsContext context) {
        transform = context.getTransform();
        context.drawImage(image, 0,0,width.doubleValue(),height.doubleValue());

        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.strokeRect(bBox.getMinX() -5, bBox.getMinY() -5, bBox.getWidth() +10, bBox.getHeight()+10);
            context.setFill(Color.RED);

            context.fillOval(-2.5, -2.5, 5, 5);
        }
    }
    @Override
    public void updateBoundingBox(){
        bBox = new BoundingBox(0, -height.doubleValue(), width.doubleValue(), height.doubleValue());
    }

    @Override
    public boolean contains(Point2D point) {
        try {
            Point2D point2D = this.transform.inverseTransform(point);

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



