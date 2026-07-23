package newStuff.canvas.sceneEditorFx2;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

// ===== Canvas view =====
class CanvasView extends Region {
    private final Canvas canvas = new Canvas();
    public  final List<SceneObject> objects = new ArrayList<>();

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
    private final ContextMenu contextMenu = new ContextMenu();

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


    private void showContextMenu(MouseEvent e, Point2D world) {
        contextMenu.hide();
        contextMenu.getItems().clear();

        SceneObject under = findTopmost(world);
        if (under != null) {
            setSelected(under);
        } else {
            setSelected(null);
        }

        // --- Add child (into selected container/object) ---
        Menu addChild = new Menu("Add Child");
        addChild.getItems().addAll(
                childMenuItem("Rectangle", () -> createChildRect(world)),
                childMenuItem("Circle", () -> createChildCircle(world)),
                childMenuItem("Container", () -> createChildContainer(world))
        );
        addChild.setDisable(selected == null);

        // --- Add root object at cursor ---
        Menu addRoot = new Menu("Add Root");
        addRoot.getItems().addAll(
                childMenuItem("Rectangle", () -> {
                    objects.add(new RectObj(world.getX() - 30, world.getY() - 20, 60, 40, Utils.randomColor()));
                    afterEdit();
                }),
                childMenuItem("Circle", () -> {
                    objects.add(new CircleObj(world.getX() - 25, world.getY() - 25, 50, 50, Utils.randomColor()));
                    afterEdit();
                }),
                childMenuItem("Container", () -> {
                    addContainer(world);
                })
        );

        MenuItem wrap = new MenuItem("Wrap in Container");
        wrap.setDisable(selected == null);
        wrap.setOnAction(a -> {
            if (selected != null) {
                GroupObj g = new GroupObj(selected.getWorldAnchor().getX(),
                        selected.getWorldAnchor().getY());
                // insert container where the object currently lives (root or same parent)
                SceneObject oldParent = selected.parent;
                if (oldParent == null) objects.add(g);
                else oldParent.addChild(g);
                reparent(selected, g);
                setSelected(g);
                afterEdit();
            }
        });

        MenuItem unparent = new MenuItem("Move to Root");
        unparent.setDisable(selected == null || selected.parent == null);
        unparent.setOnAction(a -> {
            if (selected != null) {
                reparent(selected, null);
                afterEdit();
            }
        });

        MenuItem bringToFront = new MenuItem("Bring to Front");
        bringToFront.setDisable(selected == null);
        bringToFront.setOnAction(a -> {
            reorder(selected, true);
            afterEdit();
        });

        MenuItem sendToBack = new MenuItem("Send to Back");
        sendToBack.setDisable(selected == null);
        sendToBack.setOnAction(a -> {
            reorder(selected, false);
            afterEdit();
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setDisable(selected == null);
        delete.setOnAction(a -> {
            if (selected != null) {
                if (selected.parent != null) selected.removeFromParent();
                else objects.remove(selected);
                selected = null;
                afterEdit();
                notifySelectionChanged();
            }
        });

        contextMenu.getItems().addAll(
                addChild, addRoot,
                new SeparatorMenuItem(),
                wrap, unparent,
                new SeparatorMenuItem(),
                bringToFront, sendToBack,
                new SeparatorMenuItem(),
                delete);

        contextMenu.show(canvas, e.getScreenX(), e.getScreenY());
    }

    private MenuItem childMenuItem(String text, Runnable action) {
        MenuItem mi = new MenuItem(text);
        mi.setOnAction(a -> action.run());
        return mi;
    }

    private void afterEdit() {
        notifySceneChanged();
        redraw();
    }


    private void createChildRect(Point2D world) {
        if (selected == null) return;
        Point2D local = selected.worldToLocal(world);
        RectObj r = new RectObj(local.getX() - 30, local.getY() - 20, 60, 40, Utils.randomColor());
        selected.addChild(r);
        afterEdit();
    }

    private void createChildCircle(Point2D world) {
        if (selected == null) return;
        Point2D local = selected.worldToLocal(world);
        CircleObj c = new CircleObj(local.getX() - 25, local.getY() - 25, 50, 50, Utils.randomColor());
        selected.addChild(c);
        afterEdit();
    }

    private void createChildContainer(Point2D world) {
        if (selected == null) return;
        Point2D local = selected.worldToLocal(world);
        GroupObj g = new GroupObj(local.getX(), local.getY());
        selected.addChild(g);
        afterEdit();
    }

    // Move object first/last among its siblings (draw order).
    private void reorder(SceneObject obj, boolean toFront) {
        List<SceneObject> siblings = (obj.parent == null) ? objects : obj.parent.children;
        if (!siblings.remove(obj)) return;
        if (toFront) siblings.add(obj);        // last = drawn on top
        else siblings.add(0, obj);             // first = drawn behind
    }


    // Create an empty container (group) as a root at the given world point.
    GroupObj addContainer(Point2D worldCenter) {
        GroupObj g = new GroupObj(worldCenter.getX(), worldCenter.getY());
        objects.add(g);
        notifySceneChanged();
        redraw();
        return g;
    }

    // Reparent 'child' under 'newParent' (null = root), preserving world position.
    void reparent(SceneObject child, SceneObject newParent) {
        if (child == null || child == newParent) return;
        if (newParent != null && newParent.isDescendantOf(child)) return; // no cycles

        // Preserve world placement: capture current world transform of child's anchor.
        Point2D worldAnchor = child.getWorldAnchor();
        double worldRot = worldRotation(child);

        child.removeFromParent();
        objects.remove(child);

        if (newParent == null) {
            objects.add(child);
            child.parent = null;
            // convert world anchor back to root space (identity) — set local anchor
            setLocalPlacement(child, worldAnchor, worldRot, null);
        } else {
            newParent.addChild(child);
            setLocalPlacement(child, worldAnchor, worldRot, newParent);
        }
        notifySceneChanged();
        redraw();
    }

    private double worldRotation(SceneObject obj) {
        double r = 0;
        for (SceneObject p = obj; p != null; p = p.parent) r += p.getRotation();
        return r;
    }

    // Adjust child's local anchor/rotation so its world placement is preserved.
    private void setLocalPlacement(SceneObject child, Point2D worldAnchor,
                                   double worldRot, SceneObject newParent) {
        double parentWorldRot = 0;
        Point2D localAnchor = worldAnchor;
        if (newParent != null) {
            parentWorldRot = worldRotation(newParent);
            try {
                localAnchor = newParent.worldTransform().inverseTransform(worldAnchor);
            } catch (Exception ignored) {
            }
        }
        double dx = localAnchor.getX() - child.anchor.getX();
        double dy = localAnchor.getY() - child.anchor.getY();
        child.translateLocal(dx, dy);
        child.setRotation(worldRot - parentWorldRot);
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

    private Point2D parentDelta(SceneObject obj, double wdx, double wdy) {
        if (obj.parent == null) return new Point2D(wdx, wdy);
        try {
            Affine pt = obj.parent.worldTransform();
            Point2D o = pt.inverseTransform(0, 0);
            Point2D d = pt.inverseTransform(wdx, wdy);
            return new Point2D(d.getX() - o.getX(), d.getY() - o.getY());
        } catch (Exception e) {
            return new Point2D(wdx, wdy);
        }
    }

    // ===== Coordinate transforms =====
    private Point2D screenToWorld(double sx, double sy) {
        return new Point2D((sx - offsetX) / scale, (sy - offsetY) / scale);
    }

    private boolean rightArmed = false;   // right button pressed, may become pan or context menu
    private boolean didDrag = false;

    // ===== Mouse handling =====
    private void onMousePressed(MouseEvent e) {
        requestFocus();
        lastMouseScreen = new Point2D(e.getX(), e.getY());
        Point2D world = screenToWorld(e.getX(), e.getY());
        didDrag = false;

        if (e.isMiddleButtonDown() || tool == Tool.PAN) {
            panning = true;
            return;
        }
        if (e.isSecondaryButtonDown()) {
            // Arm: decide on release whether it's a pan (dragged) or context menu (click)
            rightArmed = true;
            return;
        }
        if (e.isPrimaryButtonDown()) {
            onLeftPress(world);
        }
    }

    private void onMouseDragged(MouseEvent e) {
        Point2D screen = new Point2D(e.getX(), e.getY());
        Point2D world = screenToWorld(e.getX(), e.getY());
        didDrag = true;

        if (rightArmed) {          // right-drag becomes pan
            panning = true;
            rightArmed = false;
        }

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
                        double wdx = world.getX() - dragStartWorld.getX();
                        double wdy = world.getY() - dragStartWorld.getY();
                        // convert world delta to parent-local delta
                        Point2D pd = parentDelta(selected, wdx, wdy);
                        selected.translateLocal(pd.getX(), pd.getY());
                        dragStartWorld = world;
                    }
                }
                case MOVE_ANCHOR -> selected.setAnchor(world);
                case ROTATE -> {
                    Point2D a = selected.getWorldAnchor();
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

        if (rightArmed) {
            rightArmed = false;
            if (!didDrag) {
                showContextMenu(e, world);
                return;
            }
        }


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
                objects.add(new PolygonObj(new ArrayList<>(polygonPoints), Utils.randomColor()));
                polygonPoints.clear();
                notifySceneChanged();
            } else if (e.getClickCount() == 1) {
                polygonPoints.add(world);
            }
            redraw();
        }
    }

    private void onLeftPress(Point2D world) {
        contextMenu.hide();
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
                        Point2D a = selected.getWorldAnchor();
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
                objects.add(RectObj.fromCorners(creationStart, world, Utils.randomColor()));
                creationStart = null;
                notifySceneChanged();
                redraw();
            }
            case ADD_CIRCLE -> {
                objects.add(CircleObj.fromCenterRadius(creationStart, world, Utils.randomColor()));
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
        scale = Utils.clamp(scale * zoomFactor, 0.2, 5.0);

        Point2D before = new Point2D((e.getX() - offsetX) / oldScale, (e.getY() - offsetY) / oldScale);
        Point2D after = new Point2D((e.getX() - offsetX) / scale, (e.getY() - offsetY) / scale);
        offsetX += (after.getX() - before.getX()) * scale;
        offsetY += (after.getY() - before.getY()) * scale;
        redraw();
    }

    private void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.DELETE && selected != null) {
            if (selected.parent != null) selected.removeFromParent();
            else objects.remove(selected);
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
            SceneObject hit = hitTest(objects.get(i), world);
            if (hit != null) return hit;
        }
        return null;
    }

    // Depth-first, children take priority over parent.
    private SceneObject hitTest(SceneObject obj, Point2D world) {
        for (int i = obj.children.size() - 1; i >= 0; i--) {
            SceneObject hit = hitTest(obj.children.get(i), world);
            if (hit != null) return hit;
        }
        return obj.contains(world) ? obj : null;
    }

    // ===== Gizmo geometry =====
    private List<Point2D> getOrientedHandles(SceneObject obj) {
        Bounds b = obj.localBounds();
        List<Point2D> local = Utils.rectHandles(b.x, b.y, b.w, b.h);
        Affine wt = obj.worldTransform();
        List<Point2D> out = new ArrayList<>(local.size());
        for (Point2D p : local) out.add(wt.transform(p));
        return out;
    }

    private Point2D getRotateHandleWorld(SceneObject obj) {
        Bounds b = obj.localBounds();
        Affine wt = obj.worldTransform();
        Point2D topCenterWorld = wt.transform(new Point2D(b.centerX(), b.y));
        // outward direction = local -Y transformed to world (delta of two transformed points)
        Point2D originW = wt.transform(new Point2D(b.centerX(), b.y));
        Point2D upW = wt.transform(new Point2D(b.centerX(), b.y - 1));
        double nx = upW.getX() - originW.getX();
        double ny = upW.getY() - originW.getY();
        double len = Math.hypot(nx, ny);
        if (len > 1e-9) {
            nx /= len;
            ny /= len;
        }
        double dist = ROTATE_HANDLE_DIST / scale;
        return new Point2D(topCenterWorld.getX() + nx * dist, topCenterWorld.getY() + ny * dist);
    }


    private boolean isOnAnchorHandle(SceneObject obj, Point2D world) {
        Point2D a = obj.getWorldAnchor();
        double r = ANCHOR_RADIUS / scale;
        return Utils.sqr(world.getX() - a.getX()) + Utils.sqr(world.getY() - a.getY()) <= Utils.sqr(r);
    }

    private boolean isOnRotateHandle(SceneObject obj, Point2D world) {
        Point2D p = getRotateHandleWorld(obj);
        double r = HANDLE_RADIUS / scale;
        return Utils.sqr(world.getX() - p.getX()) + Utils.sqr(world.getY() - p.getY()) <= Utils.sqr(r);
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
            obj.render(g, selected);
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

        Point2D a = selected.getWorldAnchor();
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
