package newStuff.canvas.scenegraph;

// ======================================================================
// Anchor presets (in local space, derived from halfW/halfH).
// ======================================================================
enum AnchorPreset {
    CENTER, TOP_LEFT, TOP, TOP_RIGHT, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;

    Vec2 forNode(Node n) {
        switch (this) {
            case CENTER:
                return new Vec2(0f, 0f);
            case TOP_LEFT:
                return new Vec2(-n.halfW, n.halfH);
            case TOP:
                return new Vec2(0f, n.halfH);
            case TOP_RIGHT:
                return new Vec2(n.halfW, n.halfH);
            case LEFT:
                return new Vec2(-n.halfW, 0f);
            case RIGHT:
                return new Vec2(n.halfW, 0f);
            case BOTTOM_LEFT:
                return new Vec2(-n.halfW, -n.halfH);
            case BOTTOM:
                return new Vec2(0f, -n.halfH);
            case BOTTOM_RIGHT:
                return new Vec2(n.halfW, -n.halfH);
        }
        return new Vec2(0f, 0f);
    }
}
