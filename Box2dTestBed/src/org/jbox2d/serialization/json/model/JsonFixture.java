package org.jbox2d.serialization.json.model;

public class JsonFixture {
    private Long tag;
    private float restitution;
    private float friction;
    private float density;
    private boolean sensor;
    private JsonFilter filter;
    private JsonShape shape;

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public boolean isSensor() {
        return sensor;
    }

    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }

    public JsonFilter getFilter() {
        return filter;
    }

    public void setFilter(JsonFilter filter) {
        this.filter = filter;
    }

    public JsonShape getShape() {
        return shape;
    }

    public void setShape(JsonShape shape) {
        this.shape = shape;
    }
}
