package newStuff.canvas.scenegraph;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

// ======================================================================
// Node — a scene-graph element with editable T / R / S and anchor.
// ======================================================================
final class Node {
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

    void add(Node child) {
        child.parent = this;
        children.add(child);
    }

    /**
     * local = T(pos) * T(anchor) * R * S * T(-anchor).
     */
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

    /**
     * Anchor point expressed in the parent coordinate space.
     */
    Vec2 anchorInParent() {
        return position.add(anchor);
    }

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
        for (Node c : children){
            c.draw(g, world);
        }
    }

    void drawSelf(Graphics2D g, Mat33 world) {
        Vec2 p0 = world.transformPoint(new Vec2(-halfW, -halfH));
        Vec2 p1 = world.transformPoint(new Vec2(halfW, -halfH));
        Vec2 p2 = world.transformPoint(new Vec2(halfW, halfH));
        Vec2 p3 = world.transformPoint(new Vec2(-halfW, halfH));


        int[] xs = {(int) p0.x, (int) p1.x, (int) p2.x, (int) p3.x};
        int[] ys = {(int) p0.y, (int) p1.y, (int) p2.y, (int) p3.y};

        g.setColor(color);
        g.drawPolygon(xs, ys, 4);
    }
}
