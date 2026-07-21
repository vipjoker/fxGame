package newStuff.canvas.swing;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

class CircleObj extends AbstractTransformableObject {
    Ellipse2D.Double circle;
    Color color;

    CircleObj(Ellipse2D.Double circle, Color color) {
        this.circle = circle;
        this.color = color;
        this.anchor = new Point2D.Double(circle.getCenterX(), circle.getCenterY());
    }

    static CircleObj fromCenterRadius(Point2D center, Point2D edge, Color c) {
        double r = center.distance(edge);
        return new CircleObj(
                new Ellipse2D.Double(center.getX() - r, center.getY() - r, r * 2, r * 2), c
        );
    }

    @Override
    Shape localShape() {
        return circle;
    }

    @Override
    public void drawSelf(Graphics2D g2, boolean selected) {
        Shape s = Utils.transformed(circle, anchor, rotation);
        g2.setColor(color);
        g2.fill(s);
        g2.setColor(selected ? Color.YELLOW : Color.BLACK);
        g2.setStroke(new BasicStroke(selected ? 2f : 1f));
        g2.draw(s);
    }

    @Override
    public void translate(double dx, double dy) {
        circle.x += dx;
        circle.y += dy;
        anchor.x += dx;
        anchor.y += dy;
    }

    @Override
    public void scaleFromAnchor(double sx, double sy) {
        double x1 = anchor.x + (circle.x - anchor.x) * sx;
        double y1 = anchor.y + (circle.y - anchor.y) * sy;
        double x2 = anchor.x + (circle.x + circle.width - anchor.x) * sx;
        double y2 = anchor.y + (circle.y + circle.height - anchor.y) * sy;
        circle.x = Math.min(x1, x2);
        circle.y = Math.min(y1, y2);
        circle.width = Math.max(2, Math.abs(x2 - x1));
        circle.height = Math.max(2, Math.abs(y2 - y1));
    }

    @Override
    public void resizeByHandle(int handleIndex, Point2D.Double targetWorld) {
        Point2D.Double p = Utils.inverseRotate(targetWorld, anchor, rotation);

        double x = circle.x, y = circle.y, w = circle.width, h = circle.height;
        double x2 = x + w, y2 = y + h;
        double cx = x + w / 2.0, cy = y + h / 2.0;

        switch (handleIndex) {
            case 0 -> {
                x = p.x;
                y = p.y;
            }
            case 1 -> y = p.y;
            case 2 -> {
                x2 = p.x;
                y = p.y;
            }
            case 3 -> x2 = p.x;
            case 4 -> {
                x2 = p.x;
                y2 = p.y;
            }
            case 5 -> y2 = p.y;
            case 6 -> {
                x = p.x;
                y2 = p.y;
            }
            case 7 -> x = p.x;
            default -> {
            }
        }

        circle.x = Math.min(x, x2);
        circle.y = Math.min(y, y2);
        circle.width = Math.max(2, Math.abs(x2 - x));
        circle.height = Math.max(2, Math.abs(y2 - y));

        if (handleIndex == 1 || handleIndex == 5) circle.x = cx - circle.width / 2.0;
        if (handleIndex == 3 || handleIndex == 7) circle.y = cy - circle.height / 2.0;
    }
}
