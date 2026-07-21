package newStuff.canvas.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class CanvasPanel extends JPanel {
    private final List<SceneObject> objects = new ArrayList<>();

    private Tool tool = Tool.SELECT_MOVE;
    private SceneObject selected;

    // Camera transform
    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    // Interaction state
    private Point lastMouseScreen;
    private Point2D.Double dragStartWorld;
    private boolean draggingObject = false;
    private boolean panning = false;

    // For shape creation
    private Point2D.Double creationStart;
    private Point2D.Double currentMouseWorld;
    private List<Point2D.Double> polygonPoints = new ArrayList<>();

    private enum ManipMode {NONE, MOVE, RESIZE, ROTATE, MOVE_ANCHOR}

    private ManipMode manipMode = ManipMode.NONE;
    private int activeHandle = -1;
    private double rotateStartAngle = 0.0;
    private double objectStartRotation = 0.0;

    private static final int LEFT_RULER_W = 56;
    private static final int BOTTOM_RULER_H = 24;

    private static final double HANDLE_SCREEN_RADIUS = 6.0;
    private static final double ANCHOR_SCREEN_RADIUS = 7.0;
    private static final double ROTATE_HANDLE_SCREEN_DIST = 28.0;
    // Sprite placement
    private BufferedImage pendingSprite;
    private String pendingSpriteName = "sprite";

    CanvasPanel() {
        setBackground(new Color(40, 43, 48));
        setFocusable(true);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                lastMouseScreen = e.getPoint();
                Point2D.Double world = screenToWorld(e.getPoint());

                boolean middleButtonPan = SwingUtilities.isMiddleMouseButton(e);
                boolean rightButtonPan = SwingUtilities.isRightMouseButton(e);
                if (middleButtonPan || rightButtonPan || tool == Tool.PAN) {
                    panning = true;
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    onLeftPress(world, e.getClickCount());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point screen = e.getPoint();
                Point2D.Double world = screenToWorld(screen);

                if (panning) {
                    int dx = screen.x - lastMouseScreen.x;
                    int dy = screen.y - lastMouseScreen.y;
                    offsetX += dx;
                    offsetY += dy;
                    lastMouseScreen = screen;
                    repaint();
                    return;
                }


                if (selected != null && manipMode != ManipMode.NONE) {
                    switch (manipMode) {
                        case MOVE -> {
                            if (dragStartWorld != null) {
                                double dx = world.x - dragStartWorld.x;
                                double dy = world.y - dragStartWorld.y;
                                selected.translate(dx, dy);
                                dragStartWorld = world;
                            }
                        }
                        case MOVE_ANCHOR -> selected.setAnchor(new Point2D.Double(world.x, world.y));
                        case ROTATE -> {
                            Point2D.Double a = selected.getAnchor();
                            double ang = Math.atan2(world.y - a.y, world.x - a.x);
                            selected.setRotation(objectStartRotation + (ang - rotateStartAngle));
                        }
                        case RESIZE -> selected.resizeByHandle(activeHandle, world);
                        default -> {
                        }
                    }
                    repaint();
                    return;
                }


                currentMouseWorld = world;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point2D.Double world = screenToWorld(e.getPoint());

                if (panning) {
                    panning = false;
                    return;
                }


                if (manipMode != ManipMode.NONE) {
                    manipMode = ManipMode.NONE;
                    activeHandle = -1;
                    dragStartWorld = null;
//                        notifySceneChanged();
//                        notifySelectionChanged();
                    repaint();
                    return;
                }

                if (draggingObject) {
                    draggingObject = false;
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    onLeftRelease(world);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                currentMouseWorld = screenToWorld(e.getPoint());
                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                zoomAt(e.getPoint(), e.getPreciseWheelRotation());
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);

        setupKeyBindings();
    }

    private List<Point2D.Double> getOrientedHandles(SceneObject obj) {
        Rectangle2D b;
        if (obj instanceof AbstractTransformableObject ato) {
            b = ato.localShape().getBounds2D(); // local bounds (unrotated)
        } else {
            b = obj.getBounds();
        }

        List<Point2D.Double> local = Utils.rectHandles(b);
        Point2D.Double a = obj.getAnchor();
        double r = obj.getRotation();

        List<Point2D.Double> out = new ArrayList<>(local.size());
        for (Point2D.Double p : local) {
            out.add(Utils.rotatePoint(p, a, r));
        }
        return out;
    }

    void setTool(Tool t) {
        this.tool = t;
        if (t != Tool.ADD_POLYGON) {
            polygonPoints.clear();
        }
        creationStart = null;
        repaint();
    }

    void clearScene() {
        objects.clear();
        selected = null;
        polygonPoints.clear();
        creationStart = null;
        repaint();
        notifySceneChanged();
    }

    SceneObject getSelected() {
        return selected;
    }

    private void drawSelectionGizmos(Graphics2D g2) {
        if (selected == null) return;

        List<Point2D.Double> hs = getOrientedHandles(selected);
        if (hs.size() != 8) return;

        double hr = HANDLE_SCREEN_RADIUS / scale;
        double ar = ANCHOR_SCREEN_RADIUS / scale;

        // Draw oriented frame (TL->TR->BR->BL->TL)
        g2.setColor(new Color(255, 235, 59));
        g2.setStroke(new BasicStroke((float) (1.5 / scale)));
        g2.draw(new Line2D.Double(hs.get(0), hs.get(2)));
        g2.draw(new Line2D.Double(hs.get(2), hs.get(4)));
        g2.draw(new Line2D.Double(hs.get(4), hs.get(6)));
        g2.draw(new Line2D.Double(hs.get(6), hs.get(0)));

        // Resize handles
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke((float) (1.2 / scale)));
        for (Point2D.Double h : hs) {
            Rectangle2D.Double box = new Rectangle2D.Double(h.x - hr, h.y - hr, hr * 2, hr * 2);
            g2.fill(box);
            g2.setColor(Color.BLACK);
            g2.draw(box);
            g2.setColor(Color.WHITE);
        }

        // Rotate handle + connector from top-center handle (index 1)
        Point2D.Double top = hs.get(1);
        Point2D.Double rot = getRotateHandleWorld(selected);
        g2.setColor(new Color(255, 193, 7));
        g2.draw(new Line2D.Double(top.x, top.y, rot.x, rot.y));
        g2.fill(new Ellipse2D.Double(rot.x - hr, rot.y - hr, hr * 2, hr * 2));

        // Anchor handle
        Point2D.Double a = selected.getAnchor();
        g2.setColor(new Color(0, 188, 212));
        g2.fill(new Ellipse2D.Double(a.x - ar, a.y - ar, ar * 2, ar * 2));
        g2.setColor(Color.BLACK);
        g2.draw(new Ellipse2D.Double(a.x - ar, a.y - ar, ar * 2, ar * 2));
    }

    void setPendingSprite(BufferedImage img, String name) {
        pendingSprite = img;
        pendingSpriteName = name;
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_FOCUSED);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        am.put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    objects.remove(selected);
                    selected = null;
                    repaint();
                }
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        am.put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creationStart = null;
                polygonPoints.clear();
                repaint();
            }
        });
    }


    private Point2D.Double getRotateHandleWorld(SceneObject obj) {
        Rectangle2D b;
        if (obj instanceof AbstractTransformableObject ato) {
            b = ato.localShape().getBounds2D();
        } else {
            b = obj.getBounds();
        }

        Point2D.Double a = obj.getAnchor();
        double r = obj.getRotation();

        // Top-center in local box, then move outward along local -Y.
        Point2D.Double topCenterLocal = new Point2D.Double(b.getCenterX(), b.getY());
        Point2D.Double topCenterWorld = Utils.rotatePoint(topCenterLocal, a, r);

        double dist = ROTATE_HANDLE_SCREEN_DIST / scale;
        double nx = -Math.sin(r); // local -Y in world
        double ny = Math.cos(r);

        return new Point2D.Double(topCenterWorld.x - nx * dist, topCenterWorld.y - ny * dist);
    }

    private boolean isOnRotateHandle(SceneObject obj, Point2D.Double world) {
        Point2D.Double p = getRotateHandleWorld(obj);
        double r = HANDLE_SCREEN_RADIUS / scale;
        return Utils.sqr(world.x - p.x) + Utils.sqr(world.y - p.y) <= Utils.sqr(r);
    }

    private boolean isOnAnchorHandle(SceneObject obj, Point2D.Double world) {
        Point2D.Double a = obj.getAnchor();
        double r = ANCHOR_SCREEN_RADIUS / scale;
        return Utils.sqr(world.x - a.x) + Utils.sqr(world.y - a.y) <= Utils.sqr(r);
    }

    private void onLeftPress(Point2D.Double world, int clickCount) {
        switch (tool) {
            case SELECT_MOVE -> {
                SceneObject hit = findTopmost(world);
                if (selected != null) {
                    if (isOnAnchorHandle(selected, world)) {
                        manipMode = ManipMode.MOVE_ANCHOR;
                        repaint();
                        return;
                    }
                    if (isOnRotateHandle(selected, world)) {
                        manipMode = ManipMode.ROTATE;
                        Point2D.Double a = selected.getAnchor();
                        rotateStartAngle = Math.atan2(world.y - a.y, world.x - a.x);
                        objectStartRotation = selected.getRotation();
                        repaint();
                        return;
                    }
                    int h = selected.hitResizeHandle(world, HANDLE_SCREEN_RADIUS / scale);
                    if (h >= 0) {
                        manipMode = ManipMode.RESIZE;
                        activeHandle = h;
                        repaint();
                        return;
                    }
                }

                selected = hit;
                notifySelectionChanged();
                if (selected != null) {
                    manipMode = ManipMode.MOVE;
                    dragStartWorld = world;
                } else {
                    manipMode = ManipMode.NONE;
                }
                repaint();
            }
            case ADD_RECT, ADD_CIRCLE, ADD_LINE -> {
                creationStart = world;
                currentMouseWorld = world;
            }
            case ADD_POLYGON -> {
                if (clickCount == 2 && polygonPoints.size() >= 3) {
                    objects.add(new PolygonObj(new ArrayList<>(polygonPoints), randomColor()));
                    polygonPoints.clear();
                } else {
                    polygonPoints.add(world);
                }
                repaint();
            }
            case ADD_SPRITE -> {
                if (pendingSprite != null) {
                    objects.add(new SpriteObj(world.x, world.y, pendingSprite, pendingSpriteName));
                    repaint();
                }
            }
            case PAN -> {
                // handled in mousePressed panning logic
            }
        }
    }

    private void onLeftRelease(Point2D.Double world) {
        if (creationStart == null) return;

        switch (tool) {
            case ADD_RECT -> {
                objects.add(RectObj.fromCorners(creationStart, world, randomColor()));
                creationStart = null;
                repaint();
                notifySceneChanged();
            }
            case ADD_CIRCLE -> {
                objects.add(CircleObj.fromCenterRadius(creationStart, world, randomColor()));
                creationStart = null;
                repaint();
                notifySceneChanged();
            }
            case ADD_LINE -> {
                objects.add(new LineObj(creationStart.x, creationStart.y, world.x, world.y, Color.WHITE));
                creationStart = null;
                repaint();
                notifySceneChanged();
            }
            default -> {
            }
        }
    }

    private void zoomAt(Point pivotScreen, double wheelRotation) {
        double oldScale = scale;
        double zoomFactor = Math.pow(1.1, -wheelRotation);
        scale = clamp(scale * zoomFactor, 0.2, 5.0);

        // Keep pivot point stationary while zooming.
        Point2D.Double before = screenToWorldWithScale(pivotScreen, oldScale, offsetX, offsetY);
        Point2D.Double after = screenToWorldWithScale(pivotScreen, scale, offsetX, offsetY);

        offsetX += (after.x - before.x) * scale;
        offsetY += (after.y - before.y) * scale;

        repaint();
    }

    private Runnable sceneChangedListener;
    private java.util.function.Consumer<SceneObject> selectionChangedListener;

    void setSceneChangedListener(Runnable l) {
        this.sceneChangedListener = l;
    }

    void setSelectionChangedListener(java.util.function.Consumer<SceneObject> l) {
        this.selectionChangedListener = l;
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

    void setSelected(SceneObject obj) {
        this.selected = obj;
        notifySelectionChanged();
        repaint();
    }

    private Point2D.Double screenToWorld(Point p) {
        return screenToWorldWithScale(p, scale, offsetX, offsetY);
    }

    private Point2D.Double screenToWorldWithScale(Point p, double s, double ox, double oy) {
        return new Point2D.Double((p.x - ox) / s, (p.y - oy) / s);
    }

    private Point worldToScreen(Point2D.Double w) {
        int sx = (int) Math.round(w.x * scale + offsetX);
        int sy = (int) Math.round(w.y * scale + offsetY);
        return new Point(sx, sy);
    }

    private SceneObject findTopmost(Point2D.Double world) {
        for (int i = objects.size() - 1; i >= 0; i--) {
            if (objects.get(i).contains(world)) return objects.get(i);
        }
        return null;
    }

    private void drawRulers(Graphics2D g2) {
        int w = getWidth();
        int h = getHeight();

// Backgrounds
        g2.setColor(new Color(30, 33, 38));
        g2.fillRect(0, 0, LEFT_RULER_W, h - BOTTOM_RULER_H);               // left ruler
        g2.fillRect(LEFT_RULER_W, h - BOTTOM_RULER_H, w - LEFT_RULER_W, BOTTOM_RULER_H); // bottom ruler
        g2.fillRect(0, h - BOTTOM_RULER_H, LEFT_RULER_W, BOTTOM_RULER_H);  // corner

        g2.setColor(new Color(80, 85, 95));
        g2.drawLine(LEFT_RULER_W, 0, LEFT_RULER_W, h - BOTTOM_RULER_H);
        g2.drawLine(LEFT_RULER_W, h - BOTTOM_RULER_H, w, h - BOTTOM_RULER_H);

        drawLeftRulerTicks(g2);
        drawBottomRulerTicks(g2);
    }

    private void drawLeftRulerTicks(Graphics2D g2) {
        int sceneTop = 0;
        int sceneBottom = getHeight() - BOTTOM_RULER_H;

        double worldStep = chooseRulerWorldStep(); // world units between labels
        double worldTop = (sceneTop - offsetY) / scale;
        double worldBottom = (sceneBottom - offsetY) / scale;

        long startIdx = (long) Math.floor(worldTop / worldStep);
        long endIdx = (long) Math.ceil(worldBottom / worldStep);

        g2.setFont(g2.getFont().deriveFont(11f));

        for (long i = startIdx; i <= endIdx; i++) {
            double worldY = i * worldStep;
            int sy = (int) Math.round(worldY * scale + offsetY);

            if (sy < sceneTop || sy > sceneBottom) continue;

            g2.setColor(new Color(130, 135, 145));
            g2.drawLine(LEFT_RULER_W - 8, sy, LEFT_RULER_W, sy);

            String label = formatRounded(worldY, worldStep);
            g2.setColor(new Color(220, 225, 235));
            FontMetrics fm = g2.getFontMetrics();
            int tx = LEFT_RULER_W - 10 - fm.stringWidth(label);
            int ty = sy + fm.getAscent() / 2 - 2;
            g2.drawString(label, Math.max(2, tx), ty);
        }
    }

    private void drawBottomRulerTicks(Graphics2D g2) {
        int sceneLeft = LEFT_RULER_W;
        int sceneRight = getWidth();
        int yBase = getHeight() - BOTTOM_RULER_H;

        double worldStep = chooseRulerWorldStep();
        double worldLeft = (sceneLeft - offsetX) / scale;
        double worldRight = (sceneRight - offsetX) / scale;

        long startIdx = (long) Math.floor(worldLeft / worldStep);
        long endIdx = (long) Math.ceil(worldRight / worldStep);

        g2.setFont(g2.getFont().deriveFont(11f));

        for (long i = startIdx; i <= endIdx; i++) {
            double worldX = i * worldStep;
            int sx = (int) Math.round(worldX * scale + offsetX);

            if (sx < sceneLeft || sx > sceneRight) continue;

            g2.setColor(new Color(130, 135, 145));
            g2.drawLine(sx, yBase, sx, yBase + 8);

            String label = formatRounded(worldX, worldStep);
            g2.setColor(new Color(220, 225, 235));
            g2.drawString(label, sx + 3, yBase + 18);
        }
    }

    private double chooseRulerWorldStep() {
        // Keep ~80 px spacing between labels.
        double targetPx = 80.0;
        double raw = targetPx / scale; // world units wanted per label

        // Round to 1-2-5 progression
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

        // decimals based on step
        int decimals = 0;
        if (step < 1) decimals = (int) Math.ceil(-Math.log10(step));
        decimals = Math.max(0, Math.min(decimals, 6));

        String fmt = "%." + decimals + "f";
        return String.format(java.util.Locale.US, fmt, v);
    }

    private int chooseLabelStepPixels() {
// Keep labels readable regardless of zoom.
        if (scale < 0.4) return 160;
        if (scale < 0.8) return 120;
        if (scale < 1.6) return 80;
        return 60;
    }

    private String formatGridValue(double v) {
// Snap tiny values around zero.
        if (Math.abs(v) < 1e-6) return "0";
// Integer-ish values shown without decimals.
        if (Math.abs(v - Math.rint(v)) < 1e-6) return Integer.toString((int) Math.rint(v));
        return String.format(java.util.Locale.US, "%.1f", v);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape oldClip = g2.getClip();
        g2.setClip(LEFT_RULER_W, 0, getWidth() - LEFT_RULER_W, getHeight() - BOTTOM_RULER_H);

        drawGrid(g2);

        // Apply camera transform
        AffineTransform old = g2.getTransform();
        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);

        for (SceneObject obj : objects) {
            obj.render(g2, obj == selected);
            if (obj == selected) {
                drawSelectionGizmos(g2);
            }
        }

        drawCreationPreview(g2);

        g2.setTransform(old);
        g2.setClip(oldClip);
        drawRulers(g2);   // NEW
        drawOverlay(g2);
        g2.dispose();
    }

    private static final int TARGET_MINOR_PIXELS = 36;
    private static final int MAJOR_EVERY_MINOR = 5;
    private static final int RULER_TEXT_MARGIN = 2;

    private void drawGrid(Graphics2D g2) {
        double minorWorld = chooseNiceStep(TARGET_MINOR_PIXELS / scale);
        double majorWorld = minorWorld * MAJOR_EVERY_MINOR;

        // visible world bounds
        double worldLeft = (-offsetX) / scale;
        double worldTop = (-offsetY) / scale;
        double worldRight = (getWidth() - offsetX) / scale;
        double worldBottom = (getHeight() - offsetY) / scale;

        // styling
        Color minorColor = new Color(72, 76, 84, 110);   // thin, subtle
        Color majorColor = new Color(108, 115, 126, 170); // bold rounded steps
        Color axisColor = new Color(160, 170, 185, 170);  // zero axes
        Color rulerText = new Color(220, 225, 235, 210);

        Stroke oldStroke = g2.getStroke();

        // ---- vertical grid ----
        long iStartX = (long) Math.floor(worldLeft / minorWorld);
        long iEndX = (long) Math.ceil(worldRight / minorWorld);

        for (long i = iStartX; i <= iEndX; i++) {
            double wx = i * minorWorld;
            int sx = (int) Math.round(wx * scale + offsetX);

            boolean isMajor = i % MAJOR_EVERY_MINOR == 0;
            boolean isAxis = Math.abs(wx) < (minorWorld * 0.5);

            if (isAxis) {
                g2.setColor(axisColor);
                g2.setStroke(new BasicStroke(1.2f));
            } else if (isMajor) {
                g2.setColor(majorColor);
                g2.setStroke(new BasicStroke(1.0f));
            } else {
                g2.setColor(minorColor);
                g2.setStroke(new BasicStroke(0.6f));
            }
            g2.drawLine(sx, 0, sx, getHeight());
        }

        // ---- horizontal grid ----
        long iStartY = (long) Math.floor(worldTop / minorWorld);
        long iEndY = (long) Math.ceil(worldBottom / minorWorld);

        for (long i = iStartY; i <= iEndY; i++) {
            double wy = i * minorWorld;
            int sy = (int) Math.round(wy * scale + offsetY);

            boolean isMajor = i % MAJOR_EVERY_MINOR == 0;
            boolean isAxis = Math.abs(wy) < (minorWorld * 0.5);

            if (isAxis) {
                g2.setColor(axisColor);
                g2.setStroke(new BasicStroke(1.2f));
            } else if (isMajor) {
                g2.setColor(majorColor);
                g2.setStroke(new BasicStroke(1.0f));
            } else {
                g2.setColor(minorColor);
                g2.setStroke(new BasicStroke(0.6f));
            }
            g2.drawLine(0, sy, getWidth(), sy);
        }

        g2.setStroke(oldStroke);

        // ---- rulers labels (left + bottom), tied to major rounded steps ----
        g2.setColor(rulerText);
        FontMetrics fm = g2.getFontMetrics();

        // bottom ruler for X
        long majorStartX = (long) Math.floor(worldLeft / majorWorld);
        long majorEndX = (long) Math.ceil(worldRight / majorWorld);
        for (long i = majorStartX; i <= majorEndX; i++) {
            double wx = i * majorWorld;
            int sx = (int) Math.round(wx * scale + offsetX);
            String label = formatRounded(wx, majorWorld);
            int tw = fm.stringWidth(label);
            int tx = sx - tw / 2;
            int ty = getHeight() - 6;
            if (tx >= 0 && tx + tw <= getWidth()) {
                g2.drawString(label, tx, ty);
            }
        }

        // left ruler for Y
        long majorStartY = (long) Math.floor(worldTop / majorWorld);
        long majorEndY = (long) Math.ceil(worldBottom / majorWorld);
        for (long i = majorStartY; i <= majorEndY; i++) {
            double wy = i * majorWorld;
            int sy = (int) Math.round(wy * scale + offsetY);
            String label = formatRounded(wy, majorWorld);
            int tx = 4;
            int ty = sy - RULER_TEXT_MARGIN;
            if (ty > fm.getAscent() && ty < getHeight() - 4) {
                g2.drawString(label, tx, ty);
            }
        }
    }

    private static double chooseNiceStep(double raw) {
        // 1/2/5 * 10^n
        double pow = Math.pow(10, Math.floor(Math.log10(Math.max(raw, 1e-9))));
        double m = raw / pow;
        double niceM;
        if (m <= 1.0) niceM = 1.0;
        else if (m <= 2.0) niceM = 2.0;
        else if (m <= 5.0) niceM = 5.0;
        else niceM = 10.0;
        return niceM * pow;
    }

//        private static String formatRounded(double value, double step) {
//            // round to step precision to avoid jitter labels during pan/zoom
//            double rounded = Math.rint(value / step) * step;
//            int decimals = Math.max(0, (int) Math.ceil(-Math.log10(step)));
//            decimals = Math.min(decimals, 4);
//
//            // avoid "-0"
//            if (Math.abs(rounded) < step * 0.5e-3) rounded = 0.0;
//
//            return String.format(java.util.Locale.US, "%." + decimals + "f", rounded);
//        }

    private void drawCreationPreview(Graphics2D g2) {
        if (creationStart != null && currentMouseWorld != null) {
            g2.setColor(new Color(255, 255, 255, 140));
            switch (tool) {
                case ADD_RECT -> {
                    Rectangle2D r = rectFromCorners(creationStart, currentMouseWorld);
                    g2.draw(r);
                }
                case ADD_CIRCLE -> {
                    double dx = currentMouseWorld.x - creationStart.x;
                    double dy = currentMouseWorld.y - creationStart.y;
                    double radius = Math.hypot(dx, dy);
                    g2.draw(new Ellipse2D.Double(
                            creationStart.x - radius, creationStart.y - radius,
                            radius * 2, radius * 2
                    ));
                }
                case ADD_LINE -> g2.draw(new Line2D.Double(
                        creationStart.x, creationStart.y,
                        currentMouseWorld.x, currentMouseWorld.y
                ));
                default -> {
                }
            }
        }

        if (tool == Tool.ADD_POLYGON && !polygonPoints.isEmpty()) {
            g2.setColor(new Color(255, 255, 255, 180));
            for (int i = 0; i < polygonPoints.size() - 1; i++) {
                Point2D.Double a = polygonPoints.get(i);
                Point2D.Double b = polygonPoints.get(i + 1);
                g2.draw(new Line2D.Double(a.x, a.y, b.x, b.y));
            }
            // preview segment to mouse
            if (currentMouseWorld != null) {
                Point2D.Double last = polygonPoints.get(polygonPoints.size() - 1);
                g2.draw(new Line2D.Double(last.x, last.y, currentMouseWorld.x, currentMouseWorld.y));
            }
            for (Point2D.Double p : polygonPoints) {
                g2.fill(new Ellipse2D.Double(p.x - 2, p.y - 2, 4, 4));
            }
        }
    }

    private void drawOverlay(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 210));
        String text = "Objects: " + objects.size() + " | Zoom: " + String.format("%.2fx", scale);
        g2.drawString(text, LEFT_RULER_W + 8, getHeight() - BOTTOM_RULER_H - 6);

        if (tool == Tool.ADD_POLYGON) {
            g2.drawString("Polygon mode: click to add points, double-click to finish, ESC to cancel",
                    12, 20);
        }
    }

    public static Rectangle2D rectFromCorners(Point2D a, Point2D b) {
        double x = Math.min(a.getX(), b.getX());
        double y = Math.min(a.getY(), b.getY());
        double w = Math.abs(a.getX() - b.getX());
        double h = Math.abs(a.getY() - b.getY());
        return new Rectangle2D.Double(x, y, w, h);
    }

    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public static Color randomColor() {
        float h = (float) Math.random();
        return Color.getHSBColor(h, 0.6f, 0.95f);
    }

    private static AffineTransform buildTransform(Point2D.Double anchor, double rotation) {
        AffineTransform at = new AffineTransform();
        at.translate(anchor.x, anchor.y);
        at.rotate(rotation);
        at.translate(-anchor.x, -anchor.y);
        return at;
    }


}
