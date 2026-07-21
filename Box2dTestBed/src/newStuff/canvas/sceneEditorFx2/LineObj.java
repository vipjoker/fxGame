package newStuff.canvas.sceneEditorFx2;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

// ===== Line =====
class LineObj extends SceneObject {
    double x1, y1, x2, y2;
    Color color;

    LineObj(double x1, double y1, double x2, double y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.anchor = new Point2D((x1 + x2) / 2.0, (y1 + y2) / 2.0);
    }

    @Override
    Bounds localBounds() {
        double x = Math.min(x1, x2), y = Math.min(y1, y2);
        return new Bounds(x - 4, y - 4, Math.abs(x2 - x1) + 8, Math.abs(y2 - y1) + 8);
    }

    @Override
    void drawSelf(GraphicsContext g, boolean selected) {
        g.setStroke(selected ? Color.YELLOW : color);
        g.setLineWidth(selected ? 3 : 2);
        g.strokeLine(x1, y1, x2, y2);
        g.restore();
    }

    @Override
    boolean containsLocal(Point2D p) {
        double dist = ptSegDist(x1, y1, x2, y2, p.getX(), p.getY());
        return dist <= 5.0;
    }

    private static double ptSegDist(double ax, double ay, double bx, double by, double px, double py) {
        double dx = bx - ax, dy = by - ay;
        double len2 = dx * dx + dy * dy;
        if (len2 == 0) return Math.hypot(px - ax, py - ay);
        double t = Utils.clamp(((px - ax) * dx + (py - ay) * dy) / len2, 0, 1);
        return Math.hypot(px - (ax + t * dx), py - (ay + t * dy));
    }

    @Override
    void translateLocal(double dx, double dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
        anchor = anchor.add(dx, dy);
    }

    @Override
    void scaleFromAnchor(double sx, double sy) {
        x1 = anchor.getX() + (x1 - anchor.getX()) * sx;
        y1 = anchor.getY() + (y1 - anchor.getY()) * sy;
        x2 = anchor.getX() + (x2 - anchor.getX()) * sx;
        y2 = anchor.getY() + (y2 - anchor.getY()) * sy;
    }

    @Override
    int hitResizeHandle(Point2D world, double tolWorld) {
        Affine wt = worldTransform();
        Point2D p1 = wt.transform(new Point2D(x1, y1));
        Point2D p2 = wt.transform(new Point2D(x2, y2));
        double tol2 = tolWorld * tolWorld;
        if (Utils.sqr(world.getX() - p1.getX()) + Utils.sqr(world.getY() - p1.getY()) <= tol2)
            return 0;
        if (Utils.sqr(world.getX() - p2.getX()) + Utils.sqr(world.getY() - p2.getY()) <= tol2)
            return 1;
        return -1;
    }

    @Override
    void resizeByHandleLocal(int handleIndex, Point2D p) {
        if (handleIndex == 0) {
            x1 = p.getX();
            y1 = p.getY();
        } else if (handleIndex == 1) {
            x2 = p.getX();
            y2 = p.getY();
        }
    }
}
