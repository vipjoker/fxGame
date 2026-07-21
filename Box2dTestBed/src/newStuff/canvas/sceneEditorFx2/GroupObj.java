package newStuff.canvas.sceneEditorFx2;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

// ===== Group / container object =====
class GroupObj extends SceneObject {
    double w = 40, h = 40; // visual handle size of the empty container
    String name = "Group";

    GroupObj(double cx, double cy) {
        this.anchor = new Point2D(cx, cy);
    }

    @Override
    Bounds localBounds() {
        // If it has children, bounds cover them (in local space); else a small box.
        if (children.isEmpty()) {
            return new Bounds(anchor.getX() - w / 2, anchor.getY() - h / 2, w, h);
        }
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        for (SceneObject c : children) {
            Affine t = c.localTransform();
            Bounds b = c.localBounds();
            for (Point2D corner : new Point2D[]{
                    new Point2D(b.x, b.y), new Point2D(b.x + b.w, b.y),
                    new Point2D(b.x + b.w, b.y + b.h), new Point2D(b.x, b.y + b.h)}) {
                Point2D p = t.transform(corner);
                minX = Math.min(minX, p.getX());
                minY = Math.min(minY, p.getY());
                maxX = Math.max(maxX, p.getX());
                maxY = Math.max(maxY, p.getY());
            }
        }
        return new Bounds(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    void drawSelf(GraphicsContext g, boolean selected) {
        Bounds b = localBounds();
        g.setStroke(selected ? Color.YELLOW : Color.rgb(120, 200, 255, 0.7));
        g.setLineWidth(selected ? 2 : 1);
        g.setLineDashes(4, 4);
        g.strokeRect(b.x, b.y, b.w, b.h);
        g.setLineDashes(null);
        // anchor cross
        g.strokeLine(anchor.getX() - 6, anchor.getY(), anchor.getX() + 6, anchor.getY());
        g.strokeLine(anchor.getX(), anchor.getY() - 6, anchor.getX(), anchor.getY() + 6);
    }

    @Override
    boolean containsLocal(Point2D p) {
        Bounds b = localBounds();
        return p.getX() >= b.x && p.getX() <= b.x + b.w
                && p.getY() >= b.y && p.getY() <= b.y + b.h;
    }

    @Override
    void translateLocal(double dx, double dy) {
        anchor = anchor.add(dx, dy);
    }

    @Override
    void scaleFromAnchor(double sx, double sy) {
        for (SceneObject c : children) c.scaleFromAnchor(sx, sy);
    }

    @Override
    void resizeByHandleLocal(int handleIndex, Point2D p) {
        // containers auto-size to children; ignore direct resize
    }
}
