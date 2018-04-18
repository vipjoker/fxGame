package mygame.editor.model.box2d;

/**
 * Created by oleh on 17.04.18.
 */
public class B2Fixture {
    private String name;
    private Double density;
    private Double friction;
    private Double restitution;
    private B2Polygon polygon;
    private B2Circle circle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
    }

    public Double getFriction() {
        return friction;
    }

    public void setFriction(Double friction) {
        this.friction = friction;
    }

    public Double getRestitution() {
        return restitution;
    }

    public void setRestitution(Double restitution) {
        this.restitution = restitution;
    }

    public B2Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(B2Polygon polygon) {
        this.polygon = polygon;
    }

    public B2Circle getCircle() {
        return circle;
    }

    public void setCircle(B2Circle circle) {
        this.circle = circle;
    }
}
