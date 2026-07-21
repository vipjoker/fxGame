package newStuff.canvas;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SceneEditorFx extends Application {

    // ===== Entry point =====
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SceneEditorPane root = new SceneEditorPane();
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("2D Scene Editor (JavaFX)");
        stage.setScene(scene);
        stage.show();
    }

    // ===== Tools =====
    enum Tool {
        SELECT_MOVE, PAN, ADD_RECT, ADD_CIRCLE, ADD_LINE, ADD_POLYGON, ADD_SPRITE
    }

    // ===== Math helpers =====
    private static Point2D rotatePoint(Point2D p, Point2D anchor, double angle) {
        double c = Math.cos(angle), s = Math.sin(angle);
        double dx = p.getX() - anchor.getX(), dy = p.getY() - anchor.getY();
        return new Point2D(anchor.getX() + dx * c - dy * s, anchor.getY() + dx * s + dy * c);
    }

    private static Point2D inverseRotate(Point2D p, Point2D anchor, double angle) {
        return rotatePoint(p, anchor, -angle);
    }

    private static double sqr(double v) {
        return v * v;
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private static Color randomColor() {
        return Color.hsb(Math.random() * 360, 0.6, 0.95);
    }

    // Local (unrotated) rectangle handles: TL,T,TR,R,BR,B,BL,L
    private static List<Point2D> rectHandles(double x, double y, double w, double h) {
        double cx = x + w / 2.0, cy = y + h / 2.0;
        List<Point2D> hs = new ArrayList<>(8);
        hs.add(new Point2D(x, y));
        hs.add(new Point2D(cx, y));
        hs.add(new Point2D(x + w, y));
        hs.add(new Point2D(x + w, cy));
        hs.add(new Point2D(x + w, y + h));
        hs.add(new Point2D(cx, y + h));
        hs.add(new Point2D(x, y + h));
        hs.add(new Point2D(x, cy));
        return hs;
    }

    // ===== Bounds helper =====
    static class Bounds {
        double x, y, w, h;

        Bounds(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        double centerX() {
            return x + w / 2.0;
        }

        double centerY() {
            return y + h / 2.0;
        }
    }

    // ===== Scene object model =====
    static abstract class SceneObject {
        Point2D anchor = new Point2D(0, 0);
        double rotation = 0.0; // radians

        abstract void render(GraphicsContext g, boolean selected);

        abstract boolean contains(Point2D worldP);

        abstract void translate(double dx, double dy);

        abstract Bounds localBounds();       // unrotated local bounds

        abstract void scaleFromAnchor(double sx, double sy);

        abstract void resizeByHandle(int handleIndex, Point2D targetWorld);

        Point2D getAnchor() {
            return anchor;
        }

        void setAnchor(Point2D p) {
            double dx = anchor.getX() - p.getX();
            double dy = anchor.getY() - p.getY();
            if (Math.abs(dx) > 1e-9 || Math.abs(dy) > 1e-9) {
                translate(dx, dy);
                anchor = p;
            }
        }

        double getRotation() {
            return rotation;
        }

        void setRotation(double r) {
            rotation = r;
        }

        // Default resize-handle hit test against oriented local rect handles
        int hitResizeHandle(Point2D world, double tolWorld) {
            Bounds b = localBounds();
            List<Point2D> hs = rectHandles(b.x, b.y, b.w, b.h);
            double tol2 = tolWorld * tolWorld;
            for (int i = 0; i < hs.size(); i++) {
                Point2D h = rotatePoint(hs.get(i), anchor, rotation);
                if (sqr(world.getX() - h.getX()) + sqr(world.getY() - h.getY()) <= tol2) return i;
            }
            return -1;
        }

        // Apply anchor-centered rotation to the GraphicsContext (call save/restore around use)
        void applyTransform(GraphicsContext g) {
            g.translate(anchor.getX(), anchor.getY());
            g.rotate(Math.toDegrees(rotation));
            g.translate(-anchor.getX(), -anchor.getY());
        }
    }

    // ===== Rectangle =====
    static class RectObj extends SceneObject {
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
        void render(GraphicsContext g, boolean selected) {
            g.save();
            applyTransform(g);
            g.setFill(color);
            g.fillRect(x, y, w, h);
            g.setStroke(selected ? Color.YELLOW : Color.BLACK);
            g.setLineWidth(selected ? 2 : 1);
            g.strokeRect(x, y, w, h);
            g.restore();
        }

        @Override
        boolean contains(Point2D worldP) {
            Point2D p = inverseRotate(worldP, anchor, rotation);
            return p.getX() >= x && p.getX() <= x + w && p.getY() >= y && p.getY() <= y + h;
        }

        @Override
        void translate(double dx, double dy) {
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
        void resizeByHandle(int handleIndex, Point2D targetWorld) {
            Point2D p = inverseRotate(targetWorld, anchor, rotation);
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

    // ===== Circle / Ellipse =====
    static class CircleObj extends SceneObject {
        double x, y, w, h; // bounding box
        Color color;

        CircleObj(double x, double y, double w, double h, Color color) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.color = color;
            this.anchor = new Point2D(x + w / 2.0, y + h / 2.0);
        }

        static CircleObj fromCenterRadius(Point2D center, Point2D edge, Color c) {
            double r = center.distance(edge);
            return new CircleObj(center.getX() - r, center.getY() - r, r * 2, r * 2, c);
        }

        @Override
        Bounds localBounds() {
            return new Bounds(x, y, w, h);
        }

        @Override
        void render(GraphicsContext g, boolean selected) {
            g.save();
            applyTransform(g);
            g.setFill(color);
            g.fillOval(x, y, w, h);
            g.setStroke(selected ? Color.YELLOW : Color.BLACK);
            g.setLineWidth(selected ? 2 : 1);
            g.strokeOval(x, y, w, h);
            g.restore();
        }

        @Override
        boolean contains(Point2D worldP) {
            Point2D p = inverseRotate(worldP, anchor, rotation);
            double rx = w / 2.0, ry = h / 2.0;
            double cx = x + rx, cy = y + ry;
            if (rx <= 0 || ry <= 0) return false;
            double nx = (p.getX() - cx) / rx, ny = (p.getY() - cy) / ry;
            return nx * nx + ny * ny <= 1.0;
        }

        @Override
        void translate(double dx, double dy) {
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
        void resizeByHandle(int handleIndex, Point2D targetWorld) {
            Point2D p = inverseRotate(targetWorld, anchor, rotation);
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

    // ===== Line =====
    static class LineObj extends SceneObject {
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
        void render(GraphicsContext g, boolean selected) {
            g.save();
            applyTransform(g);
            g.setStroke(selected ? Color.YELLOW : color);
            g.setLineWidth(selected ? 3 : 2);
            g.strokeLine(x1, y1, x2, y2);
            g.restore();
        }

        @Override
        boolean contains(Point2D worldP) {
            Point2D p = inverseRotate(worldP, anchor, rotation);
            double dist = ptSegDist(x1, y1, x2, y2, p.getX(), p.getY());
            return dist <= 5.0;
        }

        private static double ptSegDist(double ax, double ay, double bx, double by, double px, double py) {
            double dx = bx - ax, dy = by - ay;
            double len2 = dx * dx + dy * dy;
            if (len2 == 0) return Math.hypot(px - ax, py - ay);
            double t = clamp(((px - ax) * dx + (py - ay) * dy) / len2, 0, 1);
            return Math.hypot(px - (ax + t * dx), py - (ay + t * dy));
        }

        @Override
        void translate(double dx, double dy) {
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
            Point2D p1 = rotatePoint(new Point2D(x1, y1), anchor, rotation);
            Point2D p2 = rotatePoint(new Point2D(x2, y2), anchor, rotation);
            double tol2 = tolWorld * tolWorld;
            if (sqr(world.getX() - p1.getX()) + sqr(world.getY() - p1.getY()) <= tol2) return 0;
            if (sqr(world.getX() - p2.getX()) + sqr(world.getY() - p2.getY()) <= tol2) return 1;
            return -1;
        }

        @Override
        void resizeByHandle(int handleIndex, Point2D targetWorld) {
            Point2D p = inverseRotate(targetWorld, anchor, rotation);
            if (handleIndex == 0) {
                x1 = p.getX();
                y1 = p.getY();
            } else if (handleIndex == 1) {
                x2 = p.getX();
                y2 = p.getY();
            }
        }
    }

    // ===== Polygon =====
    static class PolygonObj extends SceneObject {
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
        void render(GraphicsContext g, boolean selected) {
            g.save();
            applyTransform(g);
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
        boolean contains(Point2D worldP) {
            Point2D p = inverseRotate(worldP, anchor, rotation);
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
        void translate(double dx, double dy) {
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
        void resizeByHandle(int handleIndex, Point2D targetWorld) {
            Bounds b = computeLocalBounds();
            Point2D p = inverseRotate(targetWorld, anchor, rotation);
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

    // ===== Sprite =====
    static class SpriteObj extends SceneObject {
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
        void render(GraphicsContext g, boolean selected) {
            g.save();
            applyTransform(g);
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
        boolean contains(Point2D worldP) {
            Point2D p = inverseRotate(worldP, anchor, rotation);
            return p.getX() >= x && p.getX() <= x + w && p.getY() >= y && p.getY() <= y + h;
        }

        @Override
        void translate(double dx, double dy) {
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
        void resizeByHandle(int handleIndex, Point2D targetWorld) {
            Point2D p = inverseRotate(targetWorld, anchor, rotation);
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

    // ===== Canvas view =====
    static class CanvasView extends Region {
        private final Canvas canvas = new Canvas();
        private final List<SceneObject> objects = new ArrayList<>();

        private Tool tool = Tool.SELECT_MOVE;
        private SceneObject selected;

        private double scale = 1.0;
        private double offsetX = 400, offsetY = 300;

        private Point2D lastMouseScreen;
        private Point2D dragStartWorld;
        private boolean panning = false;

        private Point2D creationStart;
        private Point2D currentMouseWorld;
        private final List<Point2D> polygonPoints = new ArrayList<>();

        private enum ManipMode {NONE, MOVE, RESIZE, ROTATE, MOVE_ANCHOR}

        private ManipMode manipMode = ManipMode.NONE;
        private int activeHandle = -1;
        private double rotateStartAngle = 0.0;
        private double objectStartRotation = 0.0;

        private static final double LEFT_RULER_W = 56;
        private static final double BOTTOM_RULER_H = 24;
        private static final double HANDLE_RADIUS = 6.0;
        private static final double ANCHOR_RADIUS = 7.0;
        private static final double ROTATE_HANDLE_DIST = 28.0;
        private static final int TARGET_MINOR_PIXELS = 36;
        private static final int MAJOR_EVERY_MINOR = 5;

        private Image pendingSprite;
        private String pendingSpriteName = "sprite";

        private Runnable sceneChangedListener;
        private Consumer<SceneObject> selectionChangedListener;

        CanvasView() {
            getChildren().add(canvas);
            setFocusTraversable(true);

            widthProperty().addListener((o, a, b) -> resizeCanvas());
            heightProperty().addListener((o, a, b) -> resizeCanvas());

            canvas.setOnMousePressed(this::onMousePressed);
            canvas.setOnMouseDragged(this::onMouseDragged);
            canvas.setOnMouseReleased(this::onMouseReleased);
            canvas.setOnMouseMoved(e -> {
                currentMouseWorld = screenToWorld(e.getX(), e.getY());
                redraw();
            });
            canvas.setOnScroll(this::onScroll);
            canvas.setOnMouseClicked(this::onMouseClicked);

            setOnKeyPressed(this::onKeyPressed);
        }

        void setSceneChangedListener(Runnable r) {
            this.sceneChangedListener = r;
        }

        void setSelectionChangedListener(Consumer<SceneObject> c) {
            this.selectionChangedListener = c;
        }

        private void notifySceneChanged() {
            if (sceneChangedListener != null) sceneChangedListener.run();
        }

        private void notifySelectionChanged() {
            if (selectionChangedListener != null) selectionChangedListener.accept(selected);
        }

        List<SceneObject> getObjectsView() {
            return java.util.Collections.unmodifiableList(objects);
        }

        SceneObject getSelected() {
            return selected;
        }

        void setSelected(SceneObject obj) {
            this.selected = obj;
            notifySelectionChanged();
            redraw();
        }

        void setTool(Tool t) {
            this.tool = t;
            if (t != Tool.ADD_POLYGON) polygonPoints.clear();
            creationStart = null;
            redraw();
        }

        void setPendingSprite(Image img, String name) {
            this.pendingSprite = img;
            this.pendingSpriteName = name;
        }

        void clearScene() {
            objects.clear();
            selected = null;
            polygonPoints.clear();
            creationStart = null;
            redraw();
            notifySceneChanged();
        }

        @Override
        protected void layoutChildren() {
            resizeCanvas();
        }

        private void resizeCanvas() {
            double w = getWidth(), h = getHeight();
            if (w <= 0 || h <= 0) return;
            canvas.setWidth(w);
            canvas.setHeight(h);
            redraw();
        }

        // ===== Coordinate transforms =====
        private Point2D screenToWorld(double sx, double sy) {
            return new Point2D((sx - offsetX) / scale, (sy - offsetY) / scale);
        }

        // ===== Mouse handling =====
        private void onMousePressed(MouseEvent e) {
            requestFocus();
            lastMouseScreen = new Point2D(e.getX(), e.getY());
            Point2D world = screenToWorld(e.getX(), e.getY());

            if (e.isMiddleButtonDown() || e.isSecondaryButtonDown() || tool == Tool.PAN) {
                panning = true;
                return;
            }
            if (e.isPrimaryButtonDown()) {
                onLeftPress(world);
            }
        }

        private void onMouseDragged(MouseEvent e) {
            Point2D screen = new Point2D(e.getX(), e.getY());
            Point2D world = screenToWorld(e.getX(), e.getY());

            if (panning) {
                offsetX += screen.getX() - lastMouseScreen.getX();
                offsetY += screen.getY() - lastMouseScreen.getY();
                lastMouseScreen = screen;
                redraw();
                return;
            }

            if (selected != null && manipMode != ManipMode.NONE) {
                switch (manipMode) {
                    case MOVE -> {
                        if (dragStartWorld != null) {
                            selected.translate(world.getX() - dragStartWorld.getX(),
                                    world.getY() - dragStartWorld.getY());
                            dragStartWorld = world;
                        }
                    }
                    case MOVE_ANCHOR -> selected.setAnchor(world);
                    case ROTATE -> {
                        Point2D a = selected.getAnchor();
                        double ang = Math.atan2(world.getY() - a.getY(), world.getX() - a.getX());
                        selected.setRotation(objectStartRotation + (ang - rotateStartAngle));
                    }
                    case RESIZE -> selected.resizeByHandle(activeHandle, world);
                    default -> {
                    }
                }
                redraw();
                return;
            }

            currentMouseWorld = world;
            redraw();
        }

        private void onMouseReleased(MouseEvent e) {
            Point2D world = screenToWorld(e.getX(), e.getY());
            if (panning) {
                panning = false;
                return;
            }

            if (manipMode != ManipMode.NONE) {
                manipMode = ManipMode.NONE;
                activeHandle = -1;
                dragStartWorld = null;
                notifySceneChanged();
                redraw();
                return;
            }
            onLeftRelease(world);
        }

        private void onMouseClicked(MouseEvent e) {
            if (tool == Tool.ADD_POLYGON && e.getButton() == MouseButton.PRIMARY) {
                Point2D world = screenToWorld(e.getX(), e.getY());
                if (e.getClickCount() == 2 && polygonPoints.size() >= 3) {
                    objects.add(new PolygonObj(new ArrayList<>(polygonPoints), randomColor()));
                    polygonPoints.clear();
                    notifySceneChanged();
                } else if (e.getClickCount() == 1) {
                    polygonPoints.add(world);
                }
                redraw();
            }
        }

        private void onLeftPress(Point2D world) {
            switch (tool) {
                case SELECT_MOVE -> {
                    if (selected != null) {
                        if (isOnAnchorHandle(selected, world)) {
                            manipMode = ManipMode.MOVE_ANCHOR;
                            redraw();
                            return;
                        }
                        if (isOnRotateHandle(selected, world)) {
                            manipMode = ManipMode.ROTATE;
                            Point2D a = selected.getAnchor();
                            rotateStartAngle = Math.atan2(world.getY() - a.getY(), world.getX() - a.getX());
                            objectStartRotation = selected.getRotation();
                            redraw();
                            return;
                        }
                        int h = selected.hitResizeHandle(world, HANDLE_RADIUS / scale);
                        if (h >= 0) {
                            manipMode = ManipMode.RESIZE;
                            activeHandle = h;
                            redraw();
                            return;
                        }
                    }
                    selected = findTopmost(world);
                    notifySelectionChanged();
                    if (selected != null) {
                        manipMode = ManipMode.MOVE;
                        dragStartWorld = world;
                    } else {
                        manipMode = ManipMode.NONE;
                    }
                    redraw();
                }
                case ADD_RECT, ADD_CIRCLE, ADD_LINE -> {
                    creationStart = world;
                    currentMouseWorld = world;
                }
                case ADD_SPRITE -> {
                    if (pendingSprite != null) {
                        objects.add(new SpriteObj(world.getX(), world.getY(), pendingSprite, pendingSpriteName));
                        notifySceneChanged();
                        redraw();
                    }
                }
                default -> {
                }
            }
        }

        private void onLeftRelease(Point2D world) {
            if (creationStart == null) return;
            switch (tool) {
                case ADD_RECT -> {
                    objects.add(RectObj.fromCorners(creationStart, world, randomColor()));
                    creationStart = null;
                    notifySceneChanged();
                    redraw();
                }
                case ADD_CIRCLE -> {
                    objects.add(CircleObj.fromCenterRadius(creationStart, world, randomColor()));
                    creationStart = null;
                    notifySceneChanged();
                    redraw();
                }
                case ADD_LINE -> {
                    objects.add(new LineObj(creationStart.getX(), creationStart.getY(),
                            world.getX(), world.getY(), Color.WHITE));
                    creationStart = null;
                    notifySceneChanged();
                    redraw();
                }
                default -> {
                }
            }
        }

        private void onScroll(ScrollEvent e) {
            double oldScale = scale;
            double zoomFactor = Math.pow(1.1, e.getDeltaY() / 40.0);
            scale = clamp(scale * zoomFactor, 0.2, 5.0);

            Point2D before = new Point2D((e.getX() - offsetX) / oldScale, (e.getY() - offsetY) / oldScale);
            Point2D after = new Point2D((e.getX() - offsetX) / scale, (e.getY() - offsetY) / scale);
            offsetX += (after.getX() - before.getX()) * scale;
            offsetY += (after.getY() - before.getY()) * scale;
            redraw();
        }

        private void onKeyPressed(KeyEvent e) {
            if (e.getCode() == KeyCode.DELETE && selected != null) {
                objects.remove(selected);
                selected = null;
                notifySceneChanged();
                notifySelectionChanged();
                redraw();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                creationStart = null;
                polygonPoints.clear();
                redraw();
            }
        }

        private SceneObject findTopmost(Point2D world) {
            for (int i = objects.size() - 1; i >= 0; i--) {
                if (objects.get(i).contains(world)) return objects.get(i);
            }
            return null;
        }

        // ===== Gizmo geometry =====
        private List<Point2D> getOrientedHandles(SceneObject obj) {
            Bounds b = obj.localBounds();
            List<Point2D> local = rectHandles(b.x, b.y, b.w, b.h);
            List<Point2D> out = new ArrayList<>(local.size());
            for (Point2D p : local) out.add(rotatePoint(p, obj.getAnchor(), obj.getRotation()));
            return out;
        }

        private Point2D getRotateHandleWorld(SceneObject obj) {
            Bounds b = obj.localBounds();
            Point2D a = obj.getAnchor();
            double r = obj.getRotation();
            Point2D topCenterWorld = rotatePoint(new Point2D(b.centerX(), b.y), a, r);
            double dist = ROTATE_HANDLE_DIST / scale;
            double nx = -Math.sin(r), ny = Math.cos(r);
            return new Point2D(topCenterWorld.getX() - nx * dist, topCenterWorld.getY() - ny * dist);
        }

        private boolean isOnRotateHandle(SceneObject obj, Point2D world) {
            Point2D p = getRotateHandleWorld(obj);
            double r = HANDLE_RADIUS / scale;
            return sqr(world.getX() - p.getX()) + sqr(world.getY() - p.getY()) <= sqr(r);
        }

        private boolean isOnAnchorHandle(SceneObject obj, Point2D world) {
            Point2D a = obj.getAnchor();
            double r = ANCHOR_RADIUS / scale;
            return sqr(world.getX() - a.getX()) + sqr(world.getY() - a.getY()) <= sqr(r);
        }

        // ===== Rendering =====
        void redraw() {
            double w = canvas.getWidth(), h = canvas.getHeight();
            if (w <= 0 || h <= 0) return;
            GraphicsContext g = canvas.getGraphicsContext2D();

            g.setFill(Color.rgb(40, 43, 48));
            g.fillRect(0, 0, w, h);

            // Clip drawing area (leave rulers)
            g.save();
            g.beginPath();
            g.rect(LEFT_RULER_W, 0, w - LEFT_RULER_W, h - BOTTOM_RULER_H);
            g.clip();

            drawGrid(g);

            g.save();
            Affine cam = new Affine();
            cam.appendTranslation(offsetX, offsetY);
            cam.appendScale(scale, scale);
            g.setTransform(cam);

            for (SceneObject obj : objects) {
                obj.render(g, obj == selected);
                if (obj == selected) drawSelectionGizmos(g);
            }
            drawCreationPreview(g);
            g.restore(); // camera

            g.restore(); // clip

            drawRulers(g);
            drawOverlay(g);
        }

        private void drawSelectionGizmos(GraphicsContext g) {
            if (selected == null) return;
            List<Point2D> hs = getOrientedHandles(selected);
            if (hs.size() != 8) return;

            double hr = HANDLE_RADIUS / scale;
            double ar = ANCHOR_RADIUS / scale;

            g.setStroke(Color.rgb(255, 235, 59));
            g.setLineWidth(1.5 / scale);
            strokeLine(g, hs.get(0), hs.get(2));
            strokeLine(g, hs.get(2), hs.get(4));
            strokeLine(g, hs.get(4), hs.get(6));
            strokeLine(g, hs.get(6), hs.get(0));

            for (Point2D hp : hs) {
                g.setFill(Color.WHITE);
                g.fillRect(hp.getX() - hr, hp.getY() - hr, hr * 2, hr * 2);
                g.setStroke(Color.BLACK);
                g.setLineWidth(1.2 / scale);
                g.strokeRect(hp.getX() - hr, hp.getY() - hr, hr * 2, hr * 2);
            }

            Point2D top = hs.get(1);
            Point2D rot = getRotateHandleWorld(selected);
            g.setStroke(Color.rgb(255, 193, 7));
            strokeLine(g, top, rot);
            g.setFill(Color.rgb(255, 193, 7));
            g.fillOval(rot.getX() - hr, rot.getY() - hr, hr * 2, hr * 2);

            Point2D a = selected.getAnchor();
            g.setFill(Color.rgb(0, 188, 212));
            g.fillOval(a.getX() - ar, a.getY() - ar, ar * 2, ar * 2);
            g.setStroke(Color.BLACK);
            g.strokeOval(a.getX() - ar, a.getY() - ar, ar * 2, ar * 2);
        }

        private void strokeLine(GraphicsContext g, Point2D a, Point2D b) {
            g.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
        }

        private void drawCreationPreview(GraphicsContext g) {
            if (creationStart != null && currentMouseWorld != null) {
                g.setStroke(Color.rgb(255, 255, 255, 0.55));
                g.setLineWidth(1 / scale);
                switch (tool) {
                    case ADD_RECT -> {
                        double x = Math.min(creationStart.getX(), currentMouseWorld.getX());
                        double y = Math.min(creationStart.getY(), currentMouseWorld.getY());
                        double w = Math.abs(creationStart.getX() - currentMouseWorld.getX());
                        double h = Math.abs(creationStart.getY() - currentMouseWorld.getY());
                        g.strokeRect(x, y, w, h);
                    }
                    case ADD_CIRCLE -> {
                        double radius = creationStart.distance(currentMouseWorld);
                        g.strokeOval(creationStart.getX() - radius, creationStart.getY() - radius,
                                radius * 2, radius * 2);
                    }
                    case ADD_LINE -> g.strokeLine(creationStart.getX(), creationStart.getY(),
                            currentMouseWorld.getX(), currentMouseWorld.getY());
                    default -> {
                    }
                }
            }

            if (tool == Tool.ADD_POLYGON && !polygonPoints.isEmpty()) {
                g.setStroke(Color.rgb(255, 255, 255, 0.7));
                g.setLineWidth(1 / scale);
                for (int i = 0; i < polygonPoints.size() - 1; i++) {
                    strokeLine(g, polygonPoints.get(i), polygonPoints.get(i + 1));
                }
                if (currentMouseWorld != null) {
                    strokeLine(g, polygonPoints.get(polygonPoints.size() - 1), currentMouseWorld);
                }
                g.setFill(Color.WHITE);
                for (Point2D p : polygonPoints) g.fillOval(p.getX() - 2, p.getY() - 2, 4, 4);
            }
        }

        private void drawGrid(GraphicsContext g) {
            double minorWorld = chooseNiceStep(TARGET_MINOR_PIXELS / scale);
            double w = canvas.getWidth(), h = canvas.getHeight();
            double worldLeft = (-offsetX) / scale;
            double worldTop = (-offsetY) / scale;
            double worldRight = (w - offsetX) / scale;
            double worldBottom = (h - offsetY) / scale;

            Color minorColor = Color.rgb(72, 76, 84, 0.43);
            Color majorColor = Color.rgb(108, 115, 126, 0.67);
            Color axisColor = Color.rgb(160, 170, 185, 0.67);

            long iStartX = (long) Math.floor(worldLeft / minorWorld);
            long iEndX = (long) Math.ceil(worldRight / minorWorld);
            for (long i = iStartX; i <= iEndX; i++) {
                double wx = i * minorWorld;
                double sx = wx * scale + offsetX;
                styleGridLine(g, i, wx, minorWorld, axisColor, majorColor, minorColor);
                g.strokeLine(sx, 0, sx, h);
            }
            long iStartY = (long) Math.floor(worldTop / minorWorld);
            long iEndY = (long) Math.ceil(worldBottom / minorWorld);
            for (long i = iStartY; i <= iEndY; i++) {
                double wy = i * minorWorld;
                double sy = wy * scale + offsetY;
                styleGridLine(g, i, wy, minorWorld, axisColor, majorColor, minorColor);
                g.strokeLine(0, sy, w, sy);
            }
        }

        private void styleGridLine(GraphicsContext g, long i, double worldVal, double minorWorld,
                                   Color axis, Color major, Color minor) {
            boolean isMajor = i % MAJOR_EVERY_MINOR == 0;
            boolean isAxis = Math.abs(worldVal) < (minorWorld * 0.5);
            if (isAxis) {
                g.setStroke(axis);
                g.setLineWidth(1.2);
            } else if (isMajor) {
                g.setStroke(major);
                g.setLineWidth(1.0);
            } else {
                g.setStroke(minor);
                g.setLineWidth(0.6);
            }
        }

        private static double chooseNiceStep(double raw) {
            double pow = Math.pow(10, Math.floor(Math.log10(Math.max(raw, 1e-9))));
            double m = raw / pow;
            double niceM;
            if (m <= 1.0) niceM = 1.0;
            else if (m <= 2.0) niceM = 2.0;
            else if (m <= 5.0) niceM = 5.0;
            else niceM = 10.0;
            return niceM * pow;
        }

        private void drawRulers(GraphicsContext g) {
            double w = canvas.getWidth(), h = canvas.getHeight();

            g.setFill(Color.rgb(30, 33, 38));
            g.fillRect(0, 0, LEFT_RULER_W, h - BOTTOM_RULER_H);
            g.fillRect(LEFT_RULER_W, h - BOTTOM_RULER_H, w - LEFT_RULER_W, BOTTOM_RULER_H);
            g.fillRect(0, h - BOTTOM_RULER_H, LEFT_RULER_W, BOTTOM_RULER_H);

            g.setStroke(Color.rgb(80, 85, 95));
            g.strokeLine(LEFT_RULER_W, 0, LEFT_RULER_W, h - BOTTOM_RULER_H);
            g.strokeLine(LEFT_RULER_W, h - BOTTOM_RULER_H, w, h - BOTTOM_RULER_H);

            double worldStep = chooseRulerWorldStep();
            g.setFont(Font.font(11));

            // Left ruler (Y)
            double worldTop = (-offsetY) / scale;
            double worldBottom = (h - BOTTOM_RULER_H - offsetY) / scale;
            long sY = (long) Math.floor(worldTop / worldStep);
            long eY = (long) Math.ceil(worldBottom / worldStep);
            for (long i = sY; i <= eY; i++) {
                double worldY = i * worldStep;
                double sy = worldY * scale + offsetY;
                if (sy < 0 || sy > h - BOTTOM_RULER_H) continue;
                g.setStroke(Color.rgb(130, 135, 145));
                g.strokeLine(LEFT_RULER_W - 8, sy, LEFT_RULER_W, sy);
                g.setFill(Color.rgb(220, 225, 235));
                g.fillText(formatRounded(worldY, worldStep), 4, sy - 2);
            }

            // Bottom ruler (X)
            double worldLeft = (LEFT_RULER_W - offsetX) / scale;
            double worldRight = (w - offsetX) / scale;
            long sX = (long) Math.floor(worldLeft / worldStep);
            long eX = (long) Math.ceil(worldRight / worldStep);
            double yBase = h - BOTTOM_RULER_H;
            for (long i = sX; i <= eX; i++) {
                double worldX = i * worldStep;
                double sx = worldX * scale + offsetX;
                if (sx < LEFT_RULER_W || sx > w) continue;
                g.setStroke(Color.rgb(130, 135, 145));
                g.strokeLine(sx, yBase, sx, yBase + 8);
                g.setFill(Color.rgb(220, 225, 235));
                g.fillText(formatRounded(worldX, worldStep), sx + 3, yBase + 18);
            }
        }

        private double chooseRulerWorldStep() {
            double raw = 80.0 / scale;
            double pow10 = Math.pow(10, Math.floor(Math.log10(Math.max(raw, 1e-9))));
            double n = raw / pow10;
            double snapped;
            if (n <= 1) snapped = 1;
            else if (n <= 2) snapped = 2;
            else if (n <= 5) snapped = 5;
            else snapped = 10;
            return snapped * pow10;
        }

        private String formatRounded(double value, double step) {
            double v = Math.abs(value) < 1e-9 ? 0.0 : value;
            int decimals = 0;
            if (step < 1) decimals = (int) Math.ceil(-Math.log10(step));
            decimals = Math.max(0, Math.min(decimals, 6));
            return String.format(Locale.US, "%." + decimals + "f", v);
        }

        private void drawOverlay(GraphicsContext g) {
            g.setFill(Color.rgb(255, 255, 255, 0.82));
            g.setFont(Font.font(12));
            g.fillText("Objects: " + objects.size() +
                            " | Zoom: " + String.format("%.2fx", scale),
                    LEFT_RULER_W + 8, canvas.getHeight() - BOTTOM_RULER_H - 6);
            if (tool == Tool.ADD_POLYGON) {
                g.fillText("Polygon mode: click to add points, double-click to finish, ESC to cancel",
                        12, 18);
            }
        }
    }

    // ===== Hierarchy tree item =====
    static class ObjectItem {
        final SceneObject object;
        final String label;

        ObjectItem(SceneObject object, String label) {
            this.object = object;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // ===== Main editor pane =====
    static class SceneEditorPane extends BorderPane {
        private final CanvasView canvas = new CanvasView();
        private final Label status = new Label("Ready");
        private TreeView<Object> tree;
        private TreeItem<Object> objectsNode;
        private boolean suppressTreeSelection = false;

        // Property fields
        private final TextField posX = new TextField("0");
        private final TextField posY = new TextField("0");
        private final TextField rotDeg = new TextField("0");
        private final TextField scaleX = new TextField("1");
        private final TextField scaleY = new TextField("1");

        SceneEditorPane() {
            setPadding(new Insets(6));
            setTop(createToolbar());
            setCenter(createMainSplit());
            setBottom(createStatusBar());

            canvas.setSceneChangedListener(this::rebuildTree);
            canvas.setSelectionChangedListener(this::onSelectionChanged);
            rebuildTree();
        }

        private ToolBar createToolbar() {
            ComboBox<Tool> toolBox = new ComboBox<>();
            toolBox.getItems().addAll(Tool.values());
            toolBox.setValue(Tool.SELECT_MOVE);
            toolBox.setOnAction(e -> {
                Tool t = toolBox.getValue();
                canvas.setTool(t);
                status.setText("Tool: " + t);
            });

            Button loadSprite = new Button("Load Sprite");
            loadSprite.setOnAction(e -> {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                File f = fc.showOpenDialog(getScene().getWindow());
                if (f != null) {
                    try {
                        Image img = new Image(f.toURI().toString());
                        if (!img.isError()) {
                            canvas.setPendingSprite(img, f.getName());
                            canvas.setTool(Tool.ADD_SPRITE);
                            toolBox.setValue(Tool.ADD_SPRITE);
                            status.setText("Sprite loaded: " + f.getName() + " (click to place)");
                        }
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR,
                                "Failed to load image: " + ex.getMessage()).showAndWait();
                    }
                }
            });

            Button clear = new Button("Clear");
            clear.setOnAction(e -> {
                canvas.clearScene();
                status.setText("Scene cleared");
            });

            return new ToolBar(new Label("Tool:"), toolBox, loadSprite, clear,
                    new Label("  Tips: wheel=zoom, right/middle-drag=pan, drag object=move"));
        }

        private SplitPane createMainSplit() {
            SplitPane rightSplit = new SplitPane(canvas, createPropertiesPanel());
            rightSplit.setOrientation(Orientation.HORIZONTAL);
            rightSplit.setDividerPositions(0.78);

            SplitPane mainSplit = new SplitPane(createTreePanel(), rightSplit);
            mainSplit.setOrientation(Orientation.HORIZONTAL);
            mainSplit.setDividerPositions(0.20);
            return mainSplit;
        }

        private TitledPane createTreePanel() {
            TreeItem<Object> root = new TreeItem<>("Scene");
            objectsNode = new TreeItem<>("Objects");
            root.getChildren().add(objectsNode);
            root.setExpanded(true);
            objectsNode.setExpanded(true);

            tree = new TreeView<>(root);
            tree.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> {
                if (suppressTreeSelection) return;
                if (b != null && b.getValue() instanceof ObjectItem oi) {
                    canvas.setSelected(oi.object);
                    status.setText("Selected: " + oi.label);
                } else {
                    canvas.setSelected(null);
                }
            });

            TitledPane pane = new TitledPane("Hierarchy", tree);
            pane.setCollapsible(false);
            return pane;
        }

        private TitledPane createPropertiesPanel() {
            GridPane form = new GridPane();
            form.setHgap(6);
            form.setVgap(6);
            form.setPadding(new Insets(8));

            int r = 0;
            form.add(new Label("Position X"), 0, r);
            form.add(posX, 1, r++);
            form.add(new Label("Position Y"), 0, r);
            form.add(posY, 1, r++);
            form.add(new Label("Rotation°"), 0, r);
            form.add(rotDeg, 1, r++);
            form.add(new Label("Scale X"), 0, r);
            form.add(scaleX, 1, r++);
            form.add(new Label("Scale Y"), 0, r);
            form.add(scaleY, 1, r++);

            Button apply = new Button("Apply");
            apply.setOnAction(e -> applyProperties());
            form.add(apply, 1, r);

            posX.setOnAction(e -> applyProperties());
            posY.setOnAction(e -> applyProperties());
            rotDeg.setOnAction(e -> applyProperties());
            scaleX.setOnAction(e -> applyProperties());
            scaleY.setOnAction(e -> applyProperties());

            TitledPane pane = new TitledPane("Properties", form);
            pane.setCollapsible(false);
            return pane;
        }

        private void applyProperties() {
            SceneObject s = canvas.getSelected();
            if (s == null) return;
            try {
                double nx = Double.parseDouble(posX.getText().trim());
                double ny = Double.parseDouble(posY.getText().trim());
                Point2D a = s.getAnchor();
                s.translate(nx - a.getX(), ny - a.getY());

                s.setRotation(Math.toRadians(Double.parseDouble(rotDeg.getText().trim())));

                double sx = Double.parseDouble(scaleX.getText().trim());
                double sy = Double.parseDouble(scaleY.getText().trim());
                if (sx != 1.0 || sy != 1.0) {
                    s.scaleFromAnchor(sx, sy);
                    scaleX.setText("1");
                    scaleY.setText("1");
                }
                canvas.redraw();
            } catch (NumberFormatException ignored) {
                status.setText("Invalid number in properties");
            }
        }

        private HBox createStatusBar() {
            HBox box = new HBox(status);
            box.setPadding(new Insets(4, 8, 4, 8));
            return box;
        }

        private void rebuildTree() {
            if (objectsNode == null) return;
            objectsNode.getChildren().clear();
            int i = 1;
            for (SceneObject o : canvas.getObjectsView()) {
                objectsNode.getChildren().add(
                        new TreeItem<>(new ObjectItem(o, i + " - " + o.getClass().getSimpleName())));
                i++;
            }
            objectsNode.setExpanded(true);
        }

        private void onSelectionChanged(SceneObject target) {
            // Update tree selection
            suppressTreeSelection = true;
            try {
                if (target == null) {
                    tree.getSelectionModel().clearSelection();
                } else {
                    for (TreeItem<Object> item : objectsNode.getChildren()) {
                        if (item.getValue() instanceof ObjectItem oi && oi.object == target) {
                            tree.getSelectionModel().select(item);
                            break;
                        }
                    }
                }
            } finally {
                suppressTreeSelection = false;
            }
            // Update property fields
            if (target != null) {
                Point2D a = target.getAnchor();
                posX.setText(String.format(Locale.US, "%.2f", a.getX()));
                posY.setText(String.format(Locale.US, "%.2f", a.getY()));
                rotDeg.setText(String.format(Locale.US, "%.2f", Math.toDegrees(target.getRotation())));
                scaleX.setText("1");
                scaleY.setText("1");
            }
        }
    }
}

