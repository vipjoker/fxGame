package newStuff.canvas.scenegraph;

// ======================================================================
// Vec2 — minimal 2D vector (standalone so this file has no dependencies).
// ======================================================================
final class Vec2 {
    float x, y;

    Vec2() {
    }

    Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    Vec2 add(Vec2 v) {
        return new Vec2(x + v.x, y + v.y);
    }

    Vec2 sub(Vec2 v) {
        return new Vec2(x - v.x, y - v.y);
    }

    Vec2 mul(float s) {
        return new Vec2(x * s, y * s);
    }

    float length() {
        return (float) Math.sqrt(x * x + y * y);
    }
}
