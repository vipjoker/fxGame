package newStuff.canvas.sceneEditorFx2;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

// ===== Sprite =====
class SpriteObj extends SceneObject {
    double x, y, w, h;
    Image img;
    String name;

    SpriteObj(double x, double y, Image img, String name) {
        this.img = img;
        this.name = name;
        this.w = img.getWidth();
        this.h = img.getHeight();
        this.x = x - w / 2.0;
        this.y = y - h / 2.0;
        this.anchor = new Point2D(x, y);
    }

    @Override
    Bounds localBounds() {
        return new Bounds(x, y, w, h);
    }

    @Override
    void drawSelf(GraphicsContext g, boolean selected) {
        g.drawImage(img, x, y, w, h);
        if (selected) {
            g.setStroke(Color.YELLOW);
            g.setLineWidth(2);
            g.strokeRect(x, y, w, h);
            g.setFill(Color.WHITE);
            g.fillText(name, x, y - 4);
        }
        g.restore();
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
