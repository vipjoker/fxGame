package newStuff.canvas.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractTransformableObject implements SceneObject {
    Point2D.Double anchor = new Point2D.Double();
    double rotation = 0.0;

    protected double x, y;
    protected double rotationRad; // local rotation
    protected final List<AbstractTransformableObject> children = new ArrayList<>();

    public void addChild(AbstractTransformableObject child) {
        children.add(child);
    }

    public final void render(Graphics2D g, boolean selected) {
        AffineTransform old = g.getTransform();
        try {
            g.translate(x, y);
            g.rotate(rotationRad);

            // Draw this node in local space (0,0 origin)
            drawSelf(g, selected);

            // Draw children in this node's transformed space
            for (AbstractTransformableObject child : children) {
                child.render(g, selected);
            }
        } finally {
            g.setTransform(old);
        }
    }

    protected abstract void drawSelf(Graphics2D g, boolean selected);


    @Override
    public Point2D.Double getAnchor() {
        return new Point2D.Double(anchor.x, anchor.y);
    }

    @Override
    public void setAnchor(Point2D.Double p) {

        double dx = anchor.x - p.x;
        double dy = anchor.y - p.y;
        if (Math.abs(dx) > 1e-9 || Math.abs(dy) > 1e-9) {
            translate(dx, dy);
            anchor = new Point2D.Double(p.x, p.y);
        }

    }

    @Override
    public double getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(double radians) {
        rotation = radians;
    }

    abstract Shape localShape();

    @Override
    public boolean contains(Point2D p) {
        Shape s = Utils.transformed(localShape(), anchor, rotation);
        return s.contains(p);
    }

    @Override
    public Rectangle2D getBounds() {
        return Utils.transformed(localShape(), anchor, rotation).getBounds2D();
    }


    @Override
    public int hitResizeHandle(Point2D world, double tolWorld) {
        Rectangle2D b = localShape().getBounds2D();
        List<Point2D.Double> hs = Utils.rectHandles(b);
        double tol2 = tolWorld * tolWorld;
        for (int i = 0; i < hs.size(); i++) {
            Point2D.Double h = Utils.rotatePoint(hs.get(i), anchor, rotation);
            double d2 = Utils.sqr(world.getX() - h.x) + Utils.sqr(world.getY() - h.y);
            if (d2 <= tol2) return i;
        }
        return -1;
    }
}
