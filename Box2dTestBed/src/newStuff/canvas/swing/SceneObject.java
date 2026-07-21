package newStuff.canvas.swing;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

// ===== Scene Objects =====
interface SceneObject {
    void render(Graphics2D g2, boolean selected);

    boolean contains(Point2D p);

    void translate(double dx, double dy);

    // world-space bounds of transformed shape
    Rectangle2D getBounds();

    // pivot used for rotation/scaling
    Point2D.Double getAnchor();

    // move anchor to a new world position
    void setAnchor(Point2D.Double p);

    // angle in radians
    double getRotation();

    // set absolute rotation (radians)
    void setRotation(double radians);

    // scale from anchor; factors are multiplicative (e.g., 1.1 grows 10%)
    void scaleFromAnchor(double sx, double sy);

    // hit-test for resize handles (0..7 corners/edges), -1 if none
    int hitResizeHandle(Point2D world, double tolWorld);

    // resize by dragging selected handle to target point
    void resizeByHandle(int handleIndex, Point2D.Double targetWorld);


}
