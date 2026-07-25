package newStuff.canvas.scenegraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    private final Node root = new Node();
    private Node selected;
    private Mode mode = Mode.TRANSLATE;

    private Vec2  grabParent;
    private Vec2  startPosition;
    private float startRotation;
    private Vec2  startScale;
    private float startAngleToPivot;
    private float startDistToPivot;
    private float zoom = 5;
    private Vec2 mouse = new Vec2(0,0);

    private Mat33 viewMatrix() {
        return Mat33.translation(getWidth() / 2f, getHeight() / 2f)
                    .mul(Mat33.scale(zoom, -zoom)); // 40 px/unit, Y-up
//        return Mat33.identity();
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

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                System.out.println(e.getWheelRotation());
            }

            @Override public void mousePressed(MouseEvent e) {
                 mouse = new Vec2(e.getX(), e.getY());

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

            @Override
            public void mouseMoved(MouseEvent e) {
                mouse = new Vec2(e.getX(),e.getY()) ;
                super.mouseMoved(e);
                repaint();
            }

            @Override public void mouseDragged(MouseEvent e) {
                mouse = new Vec2(e.getX(), e.getY());
                if (selected == null) return;
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
                    case KeyEvent.VK_N: zoom++; break;
                    case KeyEvent.VK_M: zoom--; break;
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
        Vec2 vec2 = viewMatrix().invert().transformPoint(mouse);
        g2.drawString("Mouse " + mouse.x + " , " + mouse.y , 10,50);
        g2.drawString("Mouse T " + vec2.x + " , " + vec2.y , 10,70);

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
