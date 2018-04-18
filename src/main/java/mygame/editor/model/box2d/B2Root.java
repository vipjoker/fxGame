package mygame.editor.model.box2d;


import java.util.List;

/**
 * Created by oleh on 17.04.18.
 */
public class B2Root {
    private Boolean allowSleep;
    private Boolean autoClearForces;
    private List<B2Body> body;
    private Boolean continuousPhysics;
    private B2Point gravity;
    private List<B2Joint> joint;
    private Integer positionIterations;
    private Integer velocityIterations;
    private Double stepsPerSecond;
    private Boolean warmStarting;
    private Boolean subStepping;

    public Boolean getAllowSleep() {
        return allowSleep;
    }

    public void setAllowSleep(Boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    public Boolean getAutoClearForces() {
        return autoClearForces;
    }

    public void setAutoClearForces(Boolean autoClearForces) {
        this.autoClearForces = autoClearForces;
    }

    public List<B2Body> getBody() {
        return body;
    }

    public void setBody(List<B2Body> body) {
        this.body = body;
    }

    public Boolean getContinuousPhysics() {
        return continuousPhysics;
    }

    public void setContinuousPhysics(Boolean continuousPhysics) {
        this.continuousPhysics = continuousPhysics;
    }

    public B2Point getGravity() {
        return gravity;
    }

    public void setGravity(B2Point gravity) {
        this.gravity = gravity;
    }

    public List<B2Joint> getJoint() {
        return joint;
    }

    public void setJoint(List<B2Joint> joint) {
        this.joint = joint;
    }

    public Integer getPositionIterations() {
        return positionIterations;
    }

    public void setPositionIterations(Integer positionIterations) {
        this.positionIterations = positionIterations;
    }

    public Integer getVelocityIterations() {
        return velocityIterations;
    }

    public void setVelocityIterations(Integer velocityIterations) {
        this.velocityIterations = velocityIterations;
    }

    public Double getStepsPerSecond() {
        return stepsPerSecond;
    }

    public void setStepsPerSecond(Double stepsPerSecond) {
        this.stepsPerSecond = stepsPerSecond;
    }

    public Boolean getWarmStarting() {
        return warmStarting;
    }

    public void setWarmStarting(Boolean warmStarting) {
        this.warmStarting = warmStarting;
    }

    public Boolean getSubStepping() {
        return subStepping;
    }

    public void setSubStepping(Boolean subStepping) {
        this.subStepping = subStepping;
    }
}

