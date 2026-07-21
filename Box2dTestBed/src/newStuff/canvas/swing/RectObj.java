package newStuff.canvas.swing;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

class RectObj extends AbstractTransformableObject {
    Rectangle2D.Double rect;
    Color color;

    RectObj(Rectangle2D.Double rect, Color color) {
        this.rect = rect;
        this.color = color;
        this.anchor = new Point2D.Double(rect.getCenterX(), rect.getCenterY());
    }

    static RectObj fromCorners(Point2D a, Point2D b, Color c) {
        Rectangle2D r = CanvasPanel.rectFromCorners(a, b);
        return new RectObj(new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight()), c);
    }

    @Override
    Shape localShape() {
        return rect;
    }

    @Override
    public void drawSelf(Graphics2D g2, boolean selected) {
        Shape s = Utils.transformed(rect, anchor, rotation);
        g2.setColor(color);
        g2.fill(s);
        g2.setColor(selected ? Color.YELLOW : Color.BLACK);
        g2.setStroke(new BasicStroke(selected ? 2f : 1f));
        g2.draw(s);
    }

    @Override
    public void translate(double dx, double dy) {
        rect.x += dx;
        rect.y += dy;
        anchor.x += dx;
        anchor.y += dy;
    }

    @Override
    public void scaleFromAnchor(double sx, double sy) {
        double x1 = anchor.x + (rect.x - anchor.x) * sx;
        double y1 = anchor.y + (rect.y - anchor.y) * sy;
        double x2 = anchor.x + (rect.x + rect.width - anchor.x) * sx;
        double y2 = anchor.y + (rect.y + rect.height - anchor.y) * sy;
        rect.x = Math.min(x1, x2);
        rect.y = Math.min(y1, y2);
        rect.width = Math.max(2, Math.abs(x2 - x1));
        rect.height = Math.max(2, Math.abs(y2 - y1));
    }

    @Override
    public void resizeByHandle(int handleIndex, Point2D.Double targetWorld) {
        Point2D.Double p = Utils.inverseRotate(targetWorld, anchor, rotation);
        double x = rect.x, y = rect.y, w = rect.width, h = rect.height;
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
        }

        rect.x = Math.min(x, x2);
        rect.y = Math.min(y, y2);
        rect.width = Math.max(2, Math.abs(x2 - x));
        rect.height = Math.max(2, Math.abs(y2 - y));

        // preserve center handle axes if needed
        if (handleIndex == 1 || handleIndex == 5) rect.x = cx - rect.width / 2.0;
        if (handleIndex == 3 || handleIndex == 7) rect.y = cy - rect.height / 2.0;
    }
}
