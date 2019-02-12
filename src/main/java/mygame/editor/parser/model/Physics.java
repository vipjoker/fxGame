package mygame.editor.parser.model;

import java.util.List;

public class Physics {
    private int bodyId;
    private String type;
    private float linearDamping;
    private float angularDamping;
    private float linearVelocityX;
    private float linearVelocityY;
    private float angularVelocity;
    private boolean isBullet;
    private boolean fixedRotation;

    private List<Shape> shapes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public float getLinearVelocityX() {
        return linearVelocityX;
    }

    public void setLinearVelocityX(float linearVelocityX) {
        this.linearVelocityX = linearVelocityX;
    }

    public float getLinearVelocityY() {
        return linearVelocityY;
    }

    public void setLinearVelocityY(float linearVelocityY) {
        this.linearVelocityY = linearVelocityY;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public boolean isBullet() {
        return isBullet;
    }

    public void setBullet(boolean bullet) {
        isBullet = bullet;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }


    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public int getBodyId() {
        return bodyId;
    }

    public void setBodyId(int bodyId) {
        this.bodyId = bodyId;
    }
}
