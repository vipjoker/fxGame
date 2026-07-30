package org.jbox2d.serialization.json.model;

import java.util.List;

public class JsonJoint {
     Long tag ;
    private JsonJointType type;

    private int bodyA;
    private int bodyB;

    private boolean collideConnected ;

    private JsonVec2 localAnchorA ;
    private JsonVec2 localAnchorB ;

    private float refAngle; // revolute, prismatic, joint
    private boolean enableLimit; // revolute, prismatic
    private float lowerLimit; // revolute, prismatic
    private float upperLimit; // revolute, prismatic
    private boolean enableMotor; // revolute, prismatic, wheel
    private float motorSpeed; // revolute, prismatic, wheel
    private float maxMotorTorque; // revolute, wheel

    private JsonVec2 localAxisA; // prismatic, wheel
    private float maxMotorForce; // prismatic

    private float length ; // distance
    private float frequency ; // distance, mouse, wheel, weld, con vol
    private float dampingRatio; // distance, mouse, wheel, weld, col vol

    private JsonVec2 groundAnchorA; // pulley
    private JsonVec2 groundAnchorB; // pulley
    private float lengthA; // pulley
    private float lengthB; // pulley
    private float ratio ; // pulley, gear

    private JsonVec2 target; // mouse
    private float maxForce; // mouse, friction, rope

    private int joint1 ; // gear
    private int joint2 ; // gear

    // wheel joint 70

    // weld joint 80

    private float maxTorque = 90; // friction

    private float maxLength = 100; // rope

    private List<Integer> bodies ; // con vol
    private List<Integer>  joints; // con vol

    public int getBodyA() {
        return bodyA;
    }

    public void setBodyA(int bodyA) {
        this.bodyA = bodyA;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public JsonJointType getType() {
        return type;
    }

    public void setType(JsonJointType type) {
        this.type = type;
    }

    public int getBodyB() {
        return bodyB;
    }

    public void setBodyB(int bodyB) {
        this.bodyB = bodyB;
    }

    public boolean isCollideConnected() {
        return collideConnected;
    }

    public void setCollideConnected(boolean collideConnected) {
        this.collideConnected = collideConnected;
    }

    public JsonVec2 getLocalAnchorA() {
        return localAnchorA;
    }

    public void setLocalAnchorA(JsonVec2 localAnchorA) {
        this.localAnchorA = localAnchorA;
    }

    public JsonVec2 getLocalAnchorB() {
        return localAnchorB;
    }

    public void setLocalAnchorB(JsonVec2 localAnchorB) {
        this.localAnchorB = localAnchorB;
    }

    public float getRefAngle() {
        return refAngle;
    }

    public void setRefAngle(float refAngle) {
        this.refAngle = refAngle;
    }

    public boolean isEnableLimit() {
        return enableLimit;
    }

    public void setEnableLimit(boolean enableLimit) {
        this.enableLimit = enableLimit;
    }

    public float getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(float lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public float getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(float upperLimit) {
        this.upperLimit = upperLimit;
    }

    public boolean isEnableMotor() {
        return enableMotor;
    }

    public void setEnableMotor(boolean enableMotor) {
        this.enableMotor = enableMotor;
    }

    public float getMotorSpeed() {
        return motorSpeed;
    }

    public void setMotorSpeed(float motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    public float getMaxMotorTorque() {
        return maxMotorTorque;
    }

    public void setMaxMotorTorque(float maxMotorTorque) {
        this.maxMotorTorque = maxMotorTorque;
    }

    public JsonVec2 getLocalAxisA() {
        return localAxisA;
    }

    public void setLocalAxisA(JsonVec2 localAxisA) {
        this.localAxisA = localAxisA;
    }

    public float getMaxMotorForce() {
        return maxMotorForce;
    }

    public void setMaxMotorForce(float maxMotorForce) {
        this.maxMotorForce = maxMotorForce;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float getDampingRatio() {
        return dampingRatio;
    }

    public void setDampingRatio(float dampingRatio) {
        this.dampingRatio = dampingRatio;
    }

    public JsonVec2 getGroundAnchorA() {
        return groundAnchorA;
    }

    public void setGroundAnchorA(JsonVec2 groundAnchorA) {
        this.groundAnchorA = groundAnchorA;
    }

    public JsonVec2 getGroundAnchorB() {
        return groundAnchorB;
    }

    public void setGroundAnchorB(JsonVec2 groundAnchorB) {
        this.groundAnchorB = groundAnchorB;
    }

    public float getLengthA() {
        return lengthA;
    }

    public void setLengthA(float lengthA) {
        this.lengthA = lengthA;
    }

    public float getLengthB() {
        return lengthB;
    }

    public void setLengthB(float lengthB) {
        this.lengthB = lengthB;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public JsonVec2 getTarget() {
        return target;
    }

    public void setTarget(JsonVec2 target) {
        this.target = target;
    }

    public float getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    public int getJoint1() {
        return joint1;
    }

    public void setJoint1(int joint1) {
        this.joint1 = joint1;
    }

    public int getJoint2() {
        return joint2;
    }

    public void setJoint2(int joint2) {
        this.joint2 = joint2;
    }

    public float getMaxTorque() {
        return maxTorque;
    }

    public void setMaxTorque(float maxTorque) {
        this.maxTorque = maxTorque;
    }

    public float getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(float maxLength) {
        this.maxLength = maxLength;
    }

    public List<Integer> getBodies() {
        return bodies;
    }

    public void setBodies(List<Integer> bodies) {
        this.bodies = bodies;
    }

    public List<Integer> getJoints() {
        return joints;
    }

    public void setJoints(List<Integer> joints) {
        this.joints = joints;
    }

    public void clearTag() {
        this.tag = null;
    }

    public void addBodies(Integer integer) {
        this.bodies.add(integer);
    }

    public void addJoints(Integer integer) {
        this.joints.add(integer);
    }
}
