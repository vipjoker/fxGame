package newStuff.canvas.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

class SpriteObj extends AbstractTransformableObject {
    double x, y;
    double width, height;
    BufferedImage img;
    String name;

    SpriteObj(double x, double y, BufferedImage img, String name) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.name = name;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.anchor = new Point2D.Double(x + width / 2.0, y + height / 2.0);
    }

    private Rectangle2D.Double localRect() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    Shape localShape() {
        return localRect();
    }

    @Override
    public void drawSelf(Graphics2D g2, boolean selected) {
        AffineTransform old = g2.getTransform();
        g2.transform(Utils.buildTransform(anchor, rotation));
        g2.drawImage(img, (int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height), null);
        g2.setTransform(old);

        if (selected) {
            Shape s = Utils.transformed(localRect(), anchor, rotation);
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(s);

            Rectangle2D b = s.getBounds2D();
            g2.drawString(name, (int) b.getX(), (int) (b.getY() - 4));
        }
    }

    @Override
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
        anchor.x += dx;
        anchor.y += dy;
    }

    @Override
    public void scaleFromAnchor(double sx, double sy) {
        double x1 = anchor.x + (x - anchor.x) * sx;
        double y1 = anchor.y + (y - anchor.y) * sy;
        double x2 = anchor.x + (x + width - anchor.x) * sx;
        double y2 = anchor.y + (y + height - anchor.y) * sy;

        x = Math.min(x1, x2);
        y = Math.min(y1, y2);
        width = Math.max(2, Math.abs(x2 - x1));
        height = Math.max(2, Math.abs(y2 - y1));
    }

    @Override
    public void resizeByHandle(int handleIndex, Point2D.Double targetWorld) {
        Point2D.Double p = Utils.inverseRotate(targetWorld, anchor, rotation);

        double x1 = x, y1 = y, x2 = x + width, y2 = y + height;
        double cx = x + width / 2.0, cy = y + height / 2.0;

        switch (handleIndex) {
            case 0 -> {
                x1 = p.x;
                y1 = p.y;
            }
            case 1 -> y1 = p.y;
            case 2 -> {
                x2 = p.x;
                y1 = p.y;
            }
            case 3 -> x2 = p.x;
            case 4 -> {
                x2 = p.x;
                y2 = p.y;
            }
            case 5 -> y2 = p.y;
            case 6 -> {
                x1 = p.x;
                y2 = p.y;
            }
            case 7 -> x1 = p.x;
            default -> {
            }
        }

        x = Math.min(x1, x2);
        y = Math.min(y1, y2);
        width = Math.max(2, Math.abs(x2 - x1));
        height = Math.max(2, Math.abs(y2 - y1));

        if (handleIndex == 1 || handleIndex == 5) x = cx - width / 2.0;
        if (handleIndex == 3 || handleIndex == 7) y = cy - height / 2.0;
    }
}
