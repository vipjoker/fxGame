package org.jbox2d.serialization.json.model;

import java.util.List;

public class JsonWorld {

        private long tag ;
        private JsonVec2 gravity ;
        private boolean allowSleep ;
        private boolean autoClearForces ;
        private boolean warmStarting ;
        private boolean continuousPhysics ;
        private boolean subStepping ;

        private List <JsonBody> bodies;
        private List<JsonJoint> joints;

    public long getTag() {
        return tag;
    }

    public void setTag(long tag) {
        this.tag = tag;
    }

    public JsonVec2 getGravity() {
        return gravity;
    }

    public void setGravity(JsonVec2 gravity) {
        this.gravity = gravity;
    }

    public boolean isAllowSleep() {
        return allowSleep;
    }

    public void setAllowSleep(boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    public boolean isAutoClearForces() {
        return autoClearForces;
    }

    public void setAutoClearForces(boolean autoClearForces) {
        this.autoClearForces = autoClearForces;
    }

    public boolean isWarmStarting() {
        return warmStarting;
    }

    public void setWarmStarting(boolean warmStarting) {
        this.warmStarting = warmStarting;
    }

    public boolean isContinuousPhysics() {
        return continuousPhysics;
    }

    public void setContinuousPhysics(boolean continuousPhysics) {
        this.continuousPhysics = continuousPhysics;
    }

    public boolean isSubStepping() {
        return subStepping;
    }

    public void setSubStepping(boolean subStepping) {
        this.subStepping = subStepping;
    }

    public List<JsonBody> getBodies() {
        return bodies;
    }

    public void setBodies(List<JsonBody> bodies) {
        this.bodies = bodies;
    }

    public List<JsonJoint> getJoints() {
        return joints;
    }

    public void setJoints(List<JsonJoint> joints) {
        this.joints = joints;
    }


    public void addBodies(JsonBody jsonBody) {
        this.bodies.add(jsonBody);
    }

    public void addJoints(JsonJoint jsonJoint) {
        this.joints.add(jsonJoint);
    }
}
