package org.jbox2d.serialization.json.model;

import java.util.ArrayList;
import java.util.List;

public class JsonShape {
    private Long tag;
    private JsonShapeType type;
    private JsonVec2 center; // circle
    private float radius; // circle

    private List<JsonVec2> points  = new ArrayList<>(); // polygon, chain
    private List<JsonVec2> normals = new ArrayList<>(); // polygon
    private JsonVec2 centroid; // polygon

    private JsonVec2 v0; // edge
    private JsonVec2 v1; // edge
    private JsonVec2 v2; // edge
    private JsonVec2 v3; // edge
    private boolean has0; // edge, chain
    private boolean has3; // edge, chain

    private JsonVec2 prev; // chain
    private JsonVec2 next; // chain

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public JsonShapeType getType() {
        return type;
    }

    public void setType(JsonShapeType type) {
        this.type = type;
    }

    public JsonVec2 getCenter() {
        return center;
    }

    public void setCenter(JsonVec2 center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public List<JsonVec2> getPoints() {
        return points;
    }

    public void setPoints(List<JsonVec2> points) {
        this.points = points;
    }

    public List<JsonVec2> getNormals() {
        return normals;
    }

    public void setNormals(List<JsonVec2> normals) {
        this.normals = normals;
    }

    public JsonVec2 getCentroid() {
        return centroid;
    }

    public void setCentroid(JsonVec2 centroid) {
        this.centroid = centroid;
    }

    public JsonVec2 getV0() {
        return v0;
    }

    public void setV0(JsonVec2 v0) {
        this.v0 = v0;
    }

    public JsonVec2 getV1() {
        return v1;
    }

    public void setV1(JsonVec2 v1) {
        this.v1 = v1;
    }

    public JsonVec2 getV2() {
        return v2;
    }

    public void setV2(JsonVec2 v2) {
        this.v2 = v2;
    }

    public JsonVec2 getV3() {
        return v3;
    }

    public void setV3(JsonVec2 v3) {
        this.v3 = v3;
    }

    public boolean isHas0() {
        return has0;
    }

    public void setHas0(boolean has0) {
        this.has0 = has0;
    }

    public boolean isHas3() {
        return has3;
    }

    public void setHas3(boolean has3) {
        this.has3 = has3;
    }

    public JsonVec2 getPrev() {
        return prev;
    }

    public void setPrev(JsonVec2 prev) {
        this.prev = prev;
    }

    public JsonVec2 getNext() {
        return next;
    }

    public void setNext(JsonVec2 next) {

    }

    public void addPoints(JsonVec2 jsonVec2) {
        this.points.add(jsonVec2);
    }

    public void addNormals(JsonVec2 jsonVec2) {
        this.normals.add(jsonVec2);
    }
}
