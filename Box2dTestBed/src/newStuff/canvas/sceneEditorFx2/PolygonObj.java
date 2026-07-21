package newStuff.canvas.sceneEditorFx2;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

// ===== Polygon =====
class PolygonObj extends SceneObject {
    List<Point2D> points;
    Color color;

    PolygonObj(List<Point2D> points, Color color) {
        this.points = points;
        this.color = color;
        Bounds b = computeLocalBounds();
        this.anchor = new Point2D(b.centerX(), b.centerY());
    }

    private Bounds computeLocalBounds() {
        if (points.isEmpty()) return new Bounds(0, 0, 0, 0);
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        for (Point2D p : points) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        return new Bounds(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    Bounds localBounds() {
        return computeLocalBounds();
    }

    @Override
    void drawSelf(GraphicsContext g, boolean selected) {
        int n = points.size();
        double[] xs = new double[n], ys = new double[n];
        for (int i = 0; i < n; i++) {
            xs[i] = points.get(i).getX();
            ys[i] = points.get(i).getY();
        }
        g.setFill(color);
        g.fillPolygon(xs, ys, n);
        g.setStroke(selected ? Color.YELLOW : Color.BLACK);
        g.setLineWidth(selected ? 2 : 1);
        g.strokePolygon(xs, ys, n);
        g.restore();
    }

    @Override
    boolean containsLocal(Point2D p) {
        boolean inside = false;
        int n = points.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = points.get(i).getX(), yi = points.get(i).getY();
            double xj = points.get(j).getX(), yj = points.get(j).getY();
            if (((yi > p.getY()) != (yj > p.getY())) &&
                    (p.getX() < (xj - xi) * (p.getY() - yi) / (yj - yi) + xi)) {
                inside = !inside;
            }
        }
        return inside;
    }

    @Override
    void translateLocal(double dx, double dy) {
        for (int i = 0; i < points.size(); i++) points.set(i, points.get(i).add(dx, dy));
        anchor = anchor.add(dx, dy);
    }

    @Override
    void scaleFromAnchor(double sx, double sy) {
        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            points.set(i, new Point2D(
                    anchor.getX() + (p.getX() - anchor.getX()) * sx,
                    anchor.getY() + (p.getY() - anchor.getY()) * sy));
        }
    }

    @Override
    void resizeByHandleLocal(int handleIndex, Point2D p) {
        Bounds b = computeLocalBounds();
        double x = b.x, y = b.y, x2 = b.x + b.w, y2 = b.y + b.h;
        switch (handleIndex) {
            case 0 -> {
                x = p.getX();
                y = p.getY();
            }
            case 1 -> y = p.getY();
            case 2 -> {
                x2 = p.getX();
                y = p.getY();
            }
            case 3 -> x2 = p.getX();
            case 4 -> {
                x2 = p.getX();
                y2 = p.getY();
            }
            case 5 -> y2 = p.getY();
            case 6 -> {
                x = p.getX();
                y2 = p.getY();
            }
            case 7 -> x = p.getX();
        }
        double oldW = Math.max(1e-6, b.w), oldH = Math.max(1e-6, b.h);
        double newW = Math.max(2, Math.abs(x2 - x)), newH = Math.max(2, Math.abs(y2 - y));
        double minNewX = Math.min(x, x2), minNewY = Math.min(y, y2);
        for (int i = 0; i < points.size(); i++) {
            Point2D pt = points.get(i);
            double nx = (pt.getX() - b.x) / oldW;
            double ny = (pt.getY() - b.y) / oldH;
            points.set(i, new Point2D(minNewX + nx * newW, minNewY + ny * newH));
        }
    }
}
