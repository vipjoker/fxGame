package mygame.editor.model.box2d;

/**
 * Created by oleh on 17.04.18.
 */
public class B2Circle {
    private B2Point center;
    private Double radius;

    public B2Point getCenter() {
        return center;
    }

    public void setCenter(B2Point center) {
        this.center = center;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
