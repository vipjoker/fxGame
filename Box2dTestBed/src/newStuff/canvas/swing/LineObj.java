package newStuff.canvas.swing;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

class LineObj extends AbstractTransformableObject {
    Line2D.Double line;
    Color color;

    LineObj(double x1, double y1, double x2, double y2, Color color) {
        this.line = new Line2D.Double(x1, y1, x2, y2);
        this.color = color;
        this.anchor = new Point2D.Double((x1 + x2) / 2.0, (y1 + y2) / 2.0);
    }

    @Override
    Shape localShape() {
        return line;
    }

    @Override
    public void drawSelf(Graphics2D g2, boolean selected) {
        Shape s = Utils.transformed(line, anchor, rotation);
        g2.setColor(selected ? Color.YELLOW : color);
        g2.setStroke(new BasicStroke(selected ? 3f : 2f));
        g2.draw(s);
    }

    @Override
    public boolean contains(Point2D p) {
        Shape s = Utils.transformed(line, anchor, rotation);
        // stroke-based hit test for line thickness
        Shape stroked = new BasicStroke(8f).createStrokedShape(s);
        return stroked.contains(p);
    }

    @Override
    public void translate(double dx, double dy) {
        line.x1 += dx;
        line.y1 += dy;
        line.x2 += dx;
        line.y2 += dy;
        anchor.x += dx;
        anchor.y += dy;
    }

    @Override
    public void scaleFromAnchor(double sx, double sy) {
        Point2D.Double p1 = new Point2D.Double(
                anchor.x + (line.x1 - anchor.x) * sx,
                anchor.y + (line.y1 - anchor.y) * sy
        );
        Point2D.Double p2 = new Point2D.Double(
                anchor.x + (line.x2 - anchor.x) * sx,
                anchor.y + (line.y2 - anchor.y) * sy
        );
        line.x1 = p1.x;
        line.y1 = p1.y;
        line.x2 = p2.x;
        line.y2 = p2.y;
    }

    @Override
    public int hitResizeHandle(Point2D world, double tolWorld) {
        Point2D.Double p1 = Utils.rotatePoint(new Point2D.Double(line.x1, line.y1), anchor, rotation);
        Point2D.Double p2 = Utils.rotatePoint(new Point2D.Double(line.x2, line.y2), anchor, rotation);
        double tol2 = tolWorld * tolWorld;

        double d1 = Utils.sqr(world.getX() - p1.x) + Utils.sqr(world.getY() - p1.y);
        if (d1 <= tol2) return 0; // endpoint1

        double d2 = Utils.sqr(world.getX() - p2.x) + Utils.sqr(world.getY() - p2.y);
        if (d2 <= tol2) return 1; // endpoint2

        return -1;
    }

    @Override
    public void resizeByHandle(int handleIndex, Point2D.Double targetWorld) {
        Point2D.Double p = Utils.inverseRotate(targetWorld, anchor, rotation);
        if (handleIndex == 0) {
            line.x1 = p.x;
            line.y1 = p.y;
        } else if (handleIndex == 1) {
            line.x2 = p.x;
            line.y2 = p.y;
        }
    }

    @Override
    public Rectangle2D getBounds() {
        Shape s = Utils.transformed(line, anchor, rotation);
        Rectangle2D b = s.getBounds2D();
        double pad = 4.0;
        return new Rectangle2D.Double(b.getX() - pad, b.getY() - pad, b.getWidth() + 2 * pad, b.getHeight() + 2 * pad);
    }
}
