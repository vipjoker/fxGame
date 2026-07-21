package newStuff.canvas.sceneEditorFx2;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.List;

// ===== Scene object model =====
// ===== Scene object model =====
abstract class SceneObject {
    Point2D anchor = new Point2D(0, 0);
    double rotation = 0.0; // radians (LOCAL, relative to parent)

    SceneObject parent;
    final List<SceneObject> children = new ArrayList<>();

    abstract void drawSelf(GraphicsContext g, boolean selected);

    abstract boolean containsLocal(Point2D localP);   // hit-test in this node's local space

    abstract void translateLocal(double dx, double dy);

    abstract Bounds localBounds();       // unrotated local bounds

    abstract void scaleFromAnchor(double sx, double sy);

    abstract void resizeByHandleLocal(int handleIndex, Point2D localP);

    // ----- hierarchy -----
    void addChild(SceneObject child) {
        if (child.parent != null) child.parent.children.remove(child);
        child.parent = this;
        children.add(child);
    }

    void removeFromParent() {
        if (parent != null) {
            parent.children.remove(this);
            parent = null;
        }
    }

    boolean isDescendantOf(SceneObject other) {
        for (SceneObject p = parent; p != null; p = p.parent) {
            if (p == other) return true;
        }
        return false;
    }

    // ----- transforms -----
    // Local transform: rotate around anchor. (Anchor stored in local/parent space.)
    Affine localTransform() {
        Affine a = new Affine();
        a.appendTranslation(anchor.getX(), anchor.getY());
        a.appendRotation(Math.toDegrees(rotation));
        a.appendTranslation(-anchor.getX(), -anchor.getY());
        return a;
    }

    // Full transform from root to this node's local space.
    Affine worldTransform() {
        Affine a = (parent == null) ? new Affine() : parent.worldTransform();
        a.append(localTransform());
        return a;
    }

    Point2D worldToLocal(Point2D world) {
        try {
            return worldTransform().inverseTransform(world);
        } catch (Exception e) {
            return world;
        }
    }

    Point2D localToWorld(Point2D local) {
        return worldTransform().transform(local);
    }

    // Anchor expressed in world coordinates (parent transform applied).
    Point2D getWorldAnchor() {
        Affine pt = (parent == null) ? new Affine() : parent.worldTransform();
        return pt.transform(anchor);
    }

    Point2D getAnchor() {
        return anchor;
    }

    // Move anchor to a new position given in PARENT space, keeping shape put.
    void setAnchor(Point2D pParent) {
        double dx = anchor.getX() - pParent.getX();
        double dy = anchor.getY() - pParent.getY();
        if (Math.abs(dx) > 1e-9 || Math.abs(dy) > 1e-9) {
            translateLocal(dx, dy);
            anchor = pParent;
        }
    }

    double getRotation() {
        return rotation;
    }

    void setRotation(double r) {
        rotation = r;
    }

    // ----- world-space hit test (this node only, ignoring children) -----
    boolean contains(Point2D world) {
        return containsLocal(worldToLocal(world));
    }

    // Translate in parent space; converts to local delta via parent rotation.
    void translate(double dxParent, double dyParent) {
        translateLocal(dxParent, dyParent);
    }

    // Resize handle hit-test in world space against oriented local rect handles.
    int hitResizeHandle(Point2D world, double tolWorld) {
        Bounds b = localBounds();
        List<Point2D> hs = Utils.rectHandles(b.x, b.y, b.w, b.h);
        Affine wt = worldTransform();
        double tol2 = tolWorld * tolWorld;
        for (int i = 0; i < hs.size(); i++) {
            Point2D h = wt.transform(hs.get(i));
            if (Utils.sqr(world.getX() - h.getX()) + Utils.sqr(world.getY() - h.getY()) <= tol2)
                return i;
        }
        return -1;
    }

    void resizeByHandle(int handleIndex, Point2D world) {
        resizeByHandleLocal(handleIndex, worldToLocal(world));
    }

    // ----- rendering (applies local transform, draws self, then children) -----
    void render(GraphicsContext g, SceneObject selected) {
        g.save();
        g.transform(localTransform());
        drawSelf(g, this == selected);
        for (SceneObject c : children) {
            c.render(g, selected);
        }
        g.restore();
    }
}
