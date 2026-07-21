package newStuff.canvas.sceneEditorFx2;

// ===== Bounds helper =====
class Bounds {
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
