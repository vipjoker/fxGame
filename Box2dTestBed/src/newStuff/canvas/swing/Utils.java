package newStuff.canvas.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static double sqr(double v) { return v * v; }


    public static Point2D.Double inverseRotate(Point2D.Double p, Point2D.Double anchor, double angle) {
        double c = Math.cos(-angle), si = Math.sin(-angle);
        double dx = p.x - anchor.x, dy = p.y - anchor.y;
        return new Point2D.Double(anchor.x + dx * c - dy * si, anchor.y + dx * si + dy * c);
    }

    public static Point2D.Double rotatePoint(Point2D.Double p, Point2D.Double anchor, double angle) {
        double c = Math.cos(angle), si = Math.sin(angle);
        double dx = p.x - anchor.x, dy = p.y - anchor.y;
        return new Point2D.Double(anchor.x + dx * c - dy * si, anchor.y + dx * si + dy * c);
    }

    public static List<Point2D.Double> rectHandles(Rectangle2D r) {
        double x = r.getX(), y = r.getY(), w = r.getWidth(), h = r.getHeight();
        double cx = x + w / 2.0, cy = y + h / 2.0;
        List<Point2D.Double> hs = new ArrayList<>(8);
        hs.add(new Point2D.Double(x, y));         // 0 TL
        hs.add(new Point2D.Double(cx, y));        // 1 T
        hs.add(new Point2D.Double(x + w, y));     // 2 TR
        hs.add(new Point2D.Double(x + w, cy));    // 3 R
        hs.add(new Point2D.Double(x + w, y + h)); // 4 BR
        hs.add(new Point2D.Double(cx, y + h));    // 5 B
        hs.add(new Point2D.Double(x, y + h));     // 6 BL
        hs.add(new Point2D.Double(x, cy));        // 7 L
        return hs;
    }

    public static AffineTransform buildTransform(Point2D.Double anchor, double rotation) {
        AffineTransform at = new AffineTransform();
        at.translate(anchor.x, anchor.y);
        at.rotate(rotation);
        at.translate(-anchor.x, -anchor.y);
        return at;
    }

    public static Shape transformed(Shape s, Point2D.Double anchor, double rotation) {
        return buildTransform(anchor, rotation).createTransformedShape(s);
    }
}
