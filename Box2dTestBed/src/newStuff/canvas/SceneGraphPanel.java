package newStuff.canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Self-contained matrix scene-graph demo.
 *
 * Features:
 *  - Mat33 affine transforms (translate/rotate/scale) with invert + decompose.
 *  - Node hierarchy: world = parentWorld * local, local = T(pos)*T(anchor)*R*S*T(-anchor).
 *  - Mouse picking via matrix inverse (screen -> local).
 *  - Interactive TRANSLATE / ROTATE / SCALE about an editable anchor.
 *  - Editable anchor (drag) + presets (corners / edges / center).
 *
 * Keys: T translate, E rotate, S scale, A edit-anchor.
 *       Numpad/1-9 or Q/W/... snap anchor presets (see key map below).
 */
public final class SceneGraphPanel extends JPanel {

    // ======================================================================
    // Vec2 — minimal 2D vector (standalone so this file has no dependencies).
    // ======================================================================
    static final class Vec2 {
        float x, y;
        Vec2() {}
        Vec2(float x, float y) { this.x = x; this.y = y; }
        Vec2 add(Vec2 v) { return new Vec2(x + v.x, y + v.y); }
        Vec2 sub(Vec2 v) { return new Vec2(x - v.x, y - v.y); }
        Vec2 mul(float s) { return new Vec2(x * s, y * s); }
        float length() { return (float) Math.sqrt(x * x + y * y); }
    }

    // ======================================================================
    // Mat33 — 3x3 affine matrix (row-major, last row implied (0,0,1)-style
    // but stored fully to support a general inverse).
    // ======================================================================
    static final class Mat33 {
        float m00, m01, m02;
        float m10, m11, m12;
        float m20, m21, m22;

        Mat33() { m00 = m11 = m22 = 1f; }

        Mat33(float m00, float m01, float m02,
              float m10, float m11, float m12,
              float m20, float m21, float m22) {
            this.m00 = m00; this.m01 = m01; this.m02 = m02;
            this.m10 = m10; this.m11 = m11; this.m12 = m12;
            this.m20 = m20; this.m21 = m21; this.m22 = m22;
        }

        static Mat33 identity() { return new Mat33(); }

        static Mat33 translation(float tx, float ty) {
            return new Mat33(1f, 0f, tx,
                             0f, 1f, ty,
                             0f, 0f, 1f);
        }

        static Mat33 scale(float sx, float sy) {
            return new Mat33(sx, 0f, 0f,
                             0f, sy, 0f,
                             0f, 0f, 1f);
        }

        static Mat33 rotation(float angle) {
            float c = (float) Math.cos(angle), s = (float) Math.sin(angle);
            return new Mat33( c, -s, 0f,
                              s,  c, 0f,
                             0f, 0f, 1f);
        }

        Mat33 mul(Mat33 o) {
            return new Mat33(
                m00*o.m00 + m01*o.m10 + m02*o.m20,
                m00*o.m01 + m01*o.m11 + m02*o.m21,
                m00*o.m02 + m01*o.m12 + m02*o.m22,

                m10*o.m00 + m11*o.m10 + m12*o.m20,
                m10*o.m01 + m11*o.m11 + m12*o.m21,
                m10*o.m02 + m11*o.m12 + m12*o.m22,

                m20*o.m00 + m21*o.m10 + m22*o.m20,
                m20*o.m01 + m21*o.m11 + m22*o.m21,
                m20*o.m02 + m21*o.m12 + m22*o.m22);
        }

        Vec2 transformPoint(Vec2 p) {
            return new Vec2(m00*p.x + m01*p.y + m02,
                            m10*p.x + m11*p.y + m12);
        }

        Vec2 transformVector(Vec2 v) {
            return new Vec2(m00*v.x + m01*v.y,
                            m10*v.x + m11*v.y);
        }

        Mat33 invert() {
            float c00 =  (m11 * m22 - m12 * m21);
            float c01 = -(m10 * m22 - m12 * m20);
            float c02 =  (m10 * m21 - m11 * m20);
            float det = m00 * c00 + m01 * c01 + m02 * c02;
            if (det == 0f) throw new IllegalStateException("Matrix not invertible");
            float invDet = 1f / det;
            return new Mat33(
                c00 * invDet,
                (m02 * m21 - m01 * m22) * invDet,
                (m01 * m12 - m02 * m11) * invDet,

                c01 * invDet,
                (m00 * m22 - m02 * m20) * invDet,
                (m02 * m10 - m00 * m12) * invDet,

                c02 * invDet,
                (m01 * m20 - m00 * m21) * invDet,
                (m00 * m11 - m01 * m10) * invDet);
        }

        Vec2 getTranslation() { return new Vec2(m02, m12); }
        float getRotation() { return (float) Math.atan2(m10, m00); }
        Vec2 getScale() {
            return new Vec2((float) Math.sqrt(m00*m00 + m10*m10),
                            (float) Math.sqrt(m01*m01 + m11*m11));
        }
    }

    // ======================================================================
    // Node — a scene-graph element with editable T / R / S and anchor.
    // ======================================================================
    static final class Node {
        Mat33 local = Mat33.identity();
        final List<Node> children = new ArrayList<>();
        Node parent;

        // Editable local components (source of truth).
        Vec2 position = new Vec2(0f, 0f);
        float rotation = 0f;
        Vec2 scale = new Vec2(1f, 1f);
        Vec2 anchor = new Vec2(0f, 0f);   // pivot, in LOCAL space

        // Local-space half extents (box centered at origin) for hit-testing/draw.
        float halfW = 1f, halfH = 1f;
        Color color = Color.LIGHT_GRAY;

        void add(Node child) { child.parent = this; children.add(child); }

        /** local = T(pos) * T(anchor) * R * S * T(-anchor). */
        void updateLocal() {
            local = Mat33.translation(position.x, position.y)
                        .mul(Mat33.translation(anchor.x, anchor.y))
                        .mul(Mat33.rotation(rotation))
                        .mul(Mat33.scale(scale.x, scale.y))
                        .mul(Mat33.translation(-anchor.x, -anchor.y));
        }

        Mat33 worldTransform() {
            return (parent == null) ? local : parent.worldTransform().mul(local);
        }

        Mat33 parentWorld() {
            return (parent == null) ? Mat33.identity() : parent.worldTransform();
        }

        /** Anchor point expressed in the parent coordinate space. */
        Vec2 anchorInParent() { return position.add(anchor); }

        Vec2 mouseToParent(Vec2 mouseScreen, Mat33 view) {
            return view.mul(parentWorld()).invert().transformPoint(mouseScreen);
        }

        Vec2 mouseToLocal(Vec2 mouseScreen, Mat33 view) {
            return view.mul(worldTransform()).invert().transformPoint(mouseScreen);
        }

        boolean hitTest(Vec2 mouseScreen, Mat33 view) {
            Vec2 p = mouseToLocal(mouseScreen, view);
            return Math.abs(p.x) <= halfW && Math.abs(p.y) <= halfH;
        }

        Node pick(Vec2 mouseScreen, Mat33 view) {
            for (int i = children.size() - 1; i >= 0; i--) {
                Node hit = children.get(i).pick(mouseScreen, view);
                if (hit != null) return hit;
            }
            return hitTest(mouseScreen, view) ? this : null;
        }

        void draw(Graphics2D g, Mat33 parentWorld) {
            Mat33 world = parentWorld.mul(local);
            drawSelf(g, world);
            for (Node c : children) c.draw(g, world);
        }

        void drawSelf(Graphics2D g, Mat33 world) {
            Vec2 p0 = world.transformPoint(new Vec2(-halfW, -halfH));
            Vec2 p1 = world.transformPoint(new Vec2( halfW, -halfH));
            Vec2 p2 = world.transformPoint(new Vec2( halfW,  halfH));
            Vec2 p3 = world.transformPoint(new Vec2(-halfW,  halfH));
            int[] xs = {(int)p0.x, (int)p1.x, (int)p2.x, (int)p3.x};
            int[] ys = {(int)p0.y, (int)p1.y, (int)p2.y, (int)p3.y};

            g.setColor(color);
            g.drawPolygon(xs, ys, 4);
        }
    }

    // ======================================================================
    // Anchor presets (in local space, derived from halfW/halfH).
    // ======================================================================
    enum AnchorPreset {
        CENTER, TOP_LEFT, TOP, TOP_RIGHT, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;

        Vec2 forNode(Node n) {
            switch (this) {
                case CENTER:       return new Vec2(0f, 0f);
                case TOP_LEFT:     return new Vec2(-n.halfW,  n.halfH);
                case TOP:          return new Vec2(0f,        n.halfH);
                case TOP_RIGHT:    return new Vec2( n.halfW,  n.halfH);
                case LEFT:         return new Vec2(-n.halfW,  0f);
                case RIGHT:        return new Vec2( n.halfW,  0f);
                case BOTTOM_LEFT:  return new Vec2(-n.halfW, -n.halfH);
                case BOTTOM:       return new Vec2(0f,       -n.halfH);
                case BOTTOM_RIGHT: return new Vec2( n.halfW, -n.halfH);
            }
            return new Vec2(0f, 0f);
        }
    }

    // ======================================================================
    // Interaction state.
    // ======================================================================
    private enum Mode { TRANSLATE, ROTATE, SCALE, EDIT_ANCHOR }

    private final Node root = new Node();
    private Node selected;
    private Mode mode = Mode.TRANSLATE;

    private Vec2  grabParent;
    private Vec2  startPosition;
    private float startRotation;
    private Vec2  startScale;
    private float startAngleToPivot;
    private float startDistToPivot;

    private Mat33 viewMatrix() {
        return Mat33.translation(getWidth() / 2f, getHeight() / 2f)
                    .mul(Mat33.scale(40f, -40f)); // 40 px/unit, Y-up
    }

    public SceneGraphPanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(900, 650));
        buildScene();
        installMouse();
        installKeys();
    }

    private void buildScene() {
        Node parent = new Node();
        parent.position = new Vec2(2f, 1f);
        parent.rotation = 0.3f;
        parent.halfW = 2f; parent.halfH = 1.5f;
        parent.color = new Color(0.6f, 0.8f, 1f);
        parent.updateLocal();

        Node child = new Node();
        child.position = new Vec2(1.5f, 0f);
        child.rotation = 0.5f;
        child.scale = new Vec2(0.6f, 0.6f);
        child.halfW = 1f; child.halfH = 1f;
        child.color = new Color(1f, 0.7f, 0.4f);
        child.updateLocal();

        parent.add(child);
        root.add(parent);
        selected = child;
    }

    // ----------------------------------------------------------------------
    private void installMouse() {
        MouseAdapter ma = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                Vec2 mouse = new Vec2(e.getX(), e.getY());
                selected = root.pick(mouse, viewMatrix());
                if (selected == null) { repaint(); return; }

                grabParent    = selected.mouseToParent(mouse, viewMatrix());
                startPosition = new Vec2(selected.position.x, selected.position.y);
                startRotation = selected.rotation;
                startScale    = new Vec2(selected.scale.x, selected.scale.y);

                Vec2 pivot = selected.anchorInParent();
                Vec2 d = grabParent.sub(pivot);
                startAngleToPivot = (float) Math.atan2(d.y, d.x);
                startDistToPivot  = Math.max(1e-4f, d.length());
                repaint();
            }

            @Override public void mouseDragged(MouseEvent e) {
                if (selected == null) return;
                Vec2 mouse = new Vec2(e.getX(), e.getY());
                Vec2 nowParent = selected.mouseToParent(mouse, viewMatrix());
                Vec2 pivot = selected.anchorInParent();

                switch (mode) {
                    case TRANSLATE: {
                        Vec2 delta = nowParent.sub(grabParent);
                        selected.position = startPosition.add(delta);
                        break;
                    }
                    case ROTATE: {
                        Vec2 d = nowParent.sub(pivot);
                        float angleNow = (float) Math.atan2(d.y, d.x);
                        selected.rotation = startRotation + (angleNow - startAngleToPivot);
                        break;
                    }
                    case SCALE: {
                        Vec2 d = nowParent.sub(pivot);
                        float distNow = Math.max(1e-4f, d.length());
                        float factor = distNow / startDistToPivot;
                        selected.scale = new Vec2(startScale.x * factor,
                                                  startScale.y * factor);
                        break;
                    }
                    case EDIT_ANCHOR: {
                        setAnchorKeepingPlace(selected,
                                selected.mouseToLocal(mouse, viewMatrix()));
                        break;
                    }
                }
                selected.updateLocal();
                repaint();
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

//    /** Move anchor to newLocalAnchor without visually moving the node. */
//    private void setAnchorKeepingPlace(Node n, Vec2 newLocalAnchor) {
//        Vec2 before = n.anchorInParent();
//        n.anchor = newLocalAnchor;
//        n.updateLocal();
//        Vec2 after = n.anchorInParent();
//        n.position = n.position.add(before.sub(after));
//        n.updateLocal();
//    }


    /**
 * Move the anchor to newLocalAnchor while keeping the SHAPE visually fixed.
 * We pin the node's local origin (0,0) in parent space: it is invariant to
 * the anchor change, so any drift it shows is exactly the correction needed.
 */
private void setAnchorKeepingPlace(Node n, Vec2 newLocalAnchor) {
    // World position of a fixed reference point (local origin) BEFORE the change.
    Vec2 refLocal = new Vec2(0f, 0f);
    Vec2 before = n.local.transformPoint(refLocal);   // in PARENT space

    // Apply the new anchor and rebuild.
    n.anchor = newLocalAnchor;
    n.updateLocal();

    // Where did that same reference point go? Correct position to cancel the drift.
    Vec2 after = n.local.transformPoint(refLocal);
    Vec2 drift = after.sub(before);
    n.position = n.position.sub(drift);
    n.updateLocal();
}
    private void applyAnchorPreset(AnchorPreset preset) {
        if (selected == null) return;
        setAnchorKeepingPlace(selected, preset.forNode(selected));
        repaint();
    }

    // ----------------------------------------------------------------------
    private void installKeys() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_T: mode = Mode.TRANSLATE;   break;
                    case KeyEvent.VK_E: mode = Mode.ROTATE;      break;
                    case KeyEvent.VK_S: mode = Mode.SCALE;       break;
                    case KeyEvent.VK_A: mode = Mode.EDIT_ANCHOR; break;

                    // Anchor presets on a 3x3 grid (like a numpad):
                    case KeyEvent.VK_NUMPAD7: applyAnchorPreset(AnchorPreset.TOP_LEFT);     break;
                    case KeyEvent.VK_NUMPAD8: applyAnchorPreset(AnchorPreset.TOP);          break;
                    case KeyEvent.VK_NUMPAD9: applyAnchorPreset(AnchorPreset.TOP_RIGHT);    break;
                    case KeyEvent.VK_NUMPAD4: applyAnchorPreset(AnchorPreset.LEFT);         break;
                    case KeyEvent.VK_NUMPAD5: applyAnchorPreset(AnchorPreset.CENTER);       break;
                    case KeyEvent.VK_NUMPAD6: applyAnchorPreset(AnchorPreset.RIGHT);        break;
                    case KeyEvent.VK_NUMPAD1: applyAnchorPreset(AnchorPreset.BOTTOM_LEFT);  break;
                    case KeyEvent.VK_NUMPAD2: applyAnchorPreset(AnchorPreset.BOTTOM);       break;
                    case KeyEvent.VK_NUMPAD3: applyAnchorPreset(AnchorPreset.BOTTOM_RIGHT); break;

                    // Fallback (no numpad): C center, corners on QWEASDZXC-ish grid.
                    case KeyEvent.VK_C: applyAnchorPreset(AnchorPreset.CENTER);       break;
                    case KeyEvent.VK_Q: applyAnchorPreset(AnchorPreset.TOP_LEFT);     break;
                    case KeyEvent.VK_W: applyAnchorPreset(AnchorPreset.TOP);          break;
                    case KeyEvent.VK_R: applyAnchorPreset(AnchorPreset.TOP_RIGHT);    break;
                    case KeyEvent.VK_D: applyAnchorPreset(AnchorPreset.RIGHT);        break;
                    case KeyEvent.VK_F: applyAnchorPreset(AnchorPreset.LEFT);         break;
                    case KeyEvent.VK_Z: applyAnchorPreset(AnchorPreset.BOTTOM_LEFT);  break;
                    case KeyEvent.VK_X: applyAnchorPreset(AnchorPreset.BOTTOM);       break;
                    case KeyEvent.VK_V: applyAnchorPreset(AnchorPreset.BOTTOM_RIGHT); break;
                }
                repaint();
            }
        });
    }

    // ----------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        root.draw(g2, viewMatrix());

        if (selected != null) {
            // Highlight selection.
            g2.setColor(Color.YELLOW);
            Mat33 selWorld = viewMatrix().mul(selected.worldTransform());
            Vec2 p0 = selWorld.transformPoint(new Vec2(-selected.halfW, -selected.halfH));
            Vec2 p1 = selWorld.transformPoint(new Vec2( selected.halfW, -selected.halfH));
            Vec2 p2 = selWorld.transformPoint(new Vec2( selected.halfW,  selected.halfH));
            Vec2 p3 = selWorld.transformPoint(new Vec2(-selected.halfW,  selected.halfH));
            g2.drawPolygon(new int[]{(int)p0.x,(int)p1.x,(int)p2.x,(int)p3.x},
                           new int[]{(int)p0.y,(int)p1.y,(int)p2.y,(int)p3.y}, 4);

            // Anchor handle (cyan dot).
            Vec2 aWorld = selected.worldTransform().transformPoint(selected.anchor);
            Vec2 aScreen = viewMatrix().transformPoint(aWorld);
            g2.setColor(Color.CYAN);
            g2.fillOval((int) aScreen.x - 5, (int) aScreen.y - 5, 10, 10);
        }

        g2.setColor(Color.WHITE);
        g2.drawString("Mode: " + mode
                + "   [T]ranslate [E]rotate [S]cale [A]nchor-edit", 10, 20);
        g2.drawString("Anchor presets: Numpad 1-9 (grid) or C=center, "
                + "Q/W/R corners-top, Z/X/V corners-bottom, D/F sides", 10, 38);
    }

    // ----------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Matrix Scene Graph — Transform + Editable Anchor");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            SceneGraphPanel p = new SceneGraphPanel();
            f.add(p);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            p.requestFocusInWindow();
        });
    }
}
