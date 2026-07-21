package newStuff.canvas.sceneEditorFx2;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

// ===== Rectangle =====
class RectObj extends SceneObject {
    double x, y, w, h;
    Color color;

    RectObj(double x, double y, double w, double h, Color color) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        this.anchor = new Point2D(x + w / 2.0, y + h / 2.0);
    }

    static RectObj fromCorners(Point2D a, Point2D b, Color c) {
        double x = Math.min(a.getX(), b.getX());
        double y = Math.min(a.getY(), b.getY());
        return new RectObj(x, y, Math.abs(a.getX() - b.getX()), Math.abs(a.getY() - b.getY()), c);
    }

    @Override
    Bounds localBounds() {
        return new Bounds(x, y, w, h);
    }

    @Override
    Affine localTransform() {
        Affine a = new Affine();
        a.appendTranslation(x, y);
        a.appendRotation(Math.toDegrees(rotation));
        return a;
    }

    @Override
    Affine worldTransform() {
        Affine a = (parent == null) ? new Affine() : parent.worldTransform();
        a.append(localTransform());
        return a;
    }
    @Override
    void drawSelf(GraphicsContext g, boolean selected) {
        g.setFill(color);
        g.fillRect(x, y, w, h);
        g.setStroke(selected ? Color.YELLOW : Color.BLACK);
        g.setLineWidth(selected ? 2 : 1);
        g.strokeRect(x, y, w, h);
    }

    @Override
    boolean containsLocal(Point2D p) {
        return p.getX() >= x && p.getX() <= x + w && p.getY() >= y && p.getY() <= y + h;
    }


    @Override
    void translateLocal(double dx, double dy) {
        x += dx;
        y += dy;
        anchor = anchor.add(dx, dy);
    }

    @Override
    void scaleFromAnchor(double sx, double sy) {
        double x1 = anchor.getX() + (x - anchor.getX()) * sx;
        double y1 = anchor.getY() + (y - anchor.getY()) * sy;
        double x2 = anchor.getX() + (x + w - anchor.getX()) * sx;
        double y2 = anchor.getY() + (y + h - anchor.getY()) * sy;
        x = Math.min(x1, x2);
        y = Math.min(y1, y2);
        w = Math.max(2, Math.abs(x2 - x1));
        h = Math.max(2, Math.abs(y2 - y1));
    }

    @Override
    void resizeByHandleLocal(int handleIndex, Point2D p) {
        double x1 = x, y1 = y, x2 = x + w, y2 = y + h;
        double cx = x + w / 2.0, cy = y + h / 2.0;
        switch (handleIndex) {
            case 0 -> {
                x1 = p.getX();
                y1 = p.getY();
            }
            case 1 -> y1 = p.getY();
            case 2 -> {
                x2 = p.getX();
                y1 = p.getY();
            }
            case 3 -> x2 = p.getX();
            case 4 -> {
                x2 = p.getX();
                y2 = p.getY();
            }
            case 5 -> y2 = p.getY();
            case 6 -> {
                x1 = p.getX();
                y2 = p.getY();
            }
            case 7 -> x1 = p.getX();
        }
        x = Math.min(x1, x2);
        y = Math.min(y1, y2);
        w = Math.max(2, Math.abs(x2 - x1));
        h = Math.max(2, Math.abs(y2 - y1));
        if (handleIndex == 1 || handleIndex == 5) x = cx - w / 2.0;
        if (handleIndex == 3 || handleIndex == 7) y = cy - h / 2.0;
    }
}
