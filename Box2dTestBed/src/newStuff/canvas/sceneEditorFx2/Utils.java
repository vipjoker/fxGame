package newStuff.canvas.sceneEditorFx2;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static double sqr(double v) {
        return v * v;
    }

    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public static Color randomColor() {
        return Color.hsb(Math.random() * 360, 0.6, 0.95);
    }

    // Local (unrotated) rectangle handles: TL,T,TR,R,BR,B,BL,L
    public  static List<Point2D> rectHandles(double x, double y, double w, double h) {
        double cx = x + w / 2.0;
        double cy = y + h / 2.0;
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
}
