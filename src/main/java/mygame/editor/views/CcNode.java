package mygame.editor.views;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import mygame.editor.customShapes.Drawable;

import java.util.ArrayList;
import java.util.List;

public class CcNode implements Drawable {
    public int id;
    public int layer;
    public double x;
    public double y;
    public double width;
    public double height;
    public double scaleX = 1;
    public double scaleY = 1;
    public double angle;
    public Affine transform;
    public String name;
    protected BoundingBox bBox;
    public boolean active;

    private List<CcNode> children = new ArrayList<>();

    public List<CcNode> getChildren() {
        return children;
    }

    public void addChild(CcNode node) {
        children.add(node);
    }

    @Override
    public void draw(GraphicsContext context, long time) {
        context.save();
        context.translate(x, -y);
        context.rotate(angle);
        context.scale(scaleX, scaleY);
        rasterize(context);
        children.forEach(n -> n.draw(context, time));
        context.restore();

    }

    public void rasterize(GraphicsContext context) {
        transform = context.getTransform();

    }

    public boolean contains(double x, double y) {
        return false;
    }

    public void setActive(boolean isActive) {

    }


    public void setAnchor(Anchor anchor) {
        switch (anchor) {
            case MIDDLE:
                bBox = new BoundingBox(-width / 2, -height / 2, width, height);
                break;
            case BOTTOM_RIGHT:
                bBox = new BoundingBox(-width, -height, width, height);
                break;
            case BOTTOM_LEFT:
                bBox = new BoundingBox(0, -height, width, height);
                break;

            case TOP_LEFT:
                bBox = new BoundingBox(0, 0, width, height);
                break;

            case TOP_RIGHT:
                bBox = new BoundingBox(-width, 0, width, height);
                break;

        }
    }

    public enum Anchor {
        MIDDLE,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
}




