package newStuff.canvas.swing;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

class PolygonObj extends AbstractTransformableObject {
    List<Point2D.Double> points; // local points
    Color color;

    PolygonObj(List<Point2D.Double> points, Color color) {
        this.points = points;
        this.color = color;
        Rectangle2D b = computeLocalBounds();
        this.anchor = new Point2D.Double(b.getCenterX(), b.getCenterY());
    }

    private Path2D.Double localPath() {
        Path2D.Double path = new Path2D.Double();
        if (points.isEmpty()) return path;
        Point2D.Double first = points.get(0);
        path.moveTo(first.x, first.y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.closePath();
        return path;
    }

    private Rectangle2D computeLocalBounds() {
        if (points.isEmpty()) return new Rectangle2D.Double(0, 0, 0, 0);
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        for (Point2D.Double p : points) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    Shape localShape() {
        return localPath();
    }

    @Override
    public void drawSelf(Graphics2D g2, boolean selected) {
        Shape s = Utils.transformed(localPath(), anchor, rotation);
        g2.setColor(color);
        g2.fill(s);
        g2.setColor(selected ? Color.YELLOW : Color.BLACK);
        g2.setStroke(new BasicStroke(selected ? 2f : 1f));
        g2.draw(s);
    }

    @Override
    public void translate(double dx, double dy) {
        for (Point2D.Double p : points) {
            p.x += dx;
            p.y += dy;
        }
        anchor.x += dx;
        anchor.y += dy;
    }

    @Override
    public void scaleFromAnchor(double sx, double sy) {
        for (Point2D.Double p : points) {
            p.x = anchor.x + (p.x - anchor.x) * sx;
            p.y = anchor.y + (p.y - anchor.y) * sy;
        }
    }

    @Override
    public void resizeByHandle(int handleIndex, Point2D.Double targetWorld) {
        Rectangle2D localBounds = computeLocalBounds();
        Point2D.Double p = Utils.inverseRotate(targetWorld, anchor, rotation);

        double x = localBounds.getX(), y = localBounds.getY();
        double x2 = x + localBounds.getWidth(), y2 = y + localBounds.getHeight();

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

        double oldW = Math.max(1e-6, localBounds.getWidth());
        double oldH = Math.max(1e-6, localBounds.getHeight());
        double newW = Math.max(2, Math.abs(x2 - x));
        double newH = Math.max(2, Math.abs(y2 - y));

        double minOldX = localBounds.getX();
        double minOldY = localBounds.getY();
        double minNewX = Math.min(x, x2);
        double minNewY = Math.min(y, y2);

        for (Point2D.Double pt : points) {
            double nx = (pt.x - minOldX) / oldW;
            double ny = (pt.y - minOldY) / oldH;
            pt.x = minNewX + nx * newW;
            pt.y = minNewY + ny * newH;
        }
    }
}
