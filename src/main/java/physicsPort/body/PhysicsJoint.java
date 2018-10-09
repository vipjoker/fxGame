package physicsPort.body;

public class PhysicsJoint {


    public float ratio;
    public int lowerTranslation;
    public int upperTranslation;
    public float maxMotorForce;
    public float maxLength;
    public float lengthA;
    public float lengthB;
    public float maxLengthA;
    public float maxLengthB;
    public float frequencyHZ;
    public float dampingRatio;
    public float referenceAngle;
    public boolean enableLimit;
    public boolean enableMotor;
    public float lowerAngle;
    public float maxMotorTorque;
    public float motorSpeed;
    public float upperAngle;
    public float[] localAxisA;
    public float[] groundAnchorA;
    public float[] groundAnchorB;
    public int joint1;
    public int joint2;
    public float length;
    public int bodyA;
    public int bodyB;
    public int jointType;
    public float[] localAnchorA;
    public float[] localAnchorB;
    public boolean collideConnected;
    public String userData;

    public PhysicsJoint(Joint joint) {
        this.localAnchorA = joint.localAnchorA;
        this.localAnchorB = joint.localAnchorB;
        this.userData = joint.userData;
        this.collideConnected = joint.collideConnected;
        this.userData = joint.userData;

        this.jointType = joint.jointType;
        if (this.jointType == Joint.JOINT_DISTANCE) {
            this.length = joint.length;
            this.frequencyHZ = joint.frequencyHZ;
            this.dampingRatio = joint.dampingRatio;
        } else if (this.jointType == Joint.JOINT_WELD) {
            this.referenceAngle = joint.referenceAngle;
        } else if (this.jointType == Joint.JOINT_REVOLUTE) {
            this.enableLimit = joint.enableLimit;
            this.enableMotor = joint.enableMotor;
            this.lowerAngle = joint.lowerAngle;
            this.maxMotorTorque = joint.maxMotorTorque;
            this.motorSpeed = joint.motorSpeed;
            this.referenceAngle = joint.referenceAngle;
            this.upperAngle = joint.upperAngle;
        } else if (this.jointType == Joint.JOINT_WHEEL) {
            this.localAxisA = joint.localAxisA;
            this.enableMotor = joint.enableMotor;
            this.maxMotorTorque = joint.maxMotorTorque;
            this.motorSpeed = joint.motorSpeed;
            this.frequencyHZ = joint.frequencyHZ;
            this.dampingRatio = joint.dampingRatio;
        } else if (this.jointType == Joint.JOINT_PULLEY) {
            this.groundAnchorA = joint.groundAnchorA;
            this.groundAnchorB = joint.groundAnchorB;
            this.lengthA = joint.lengthA;
            this.lengthB = joint.lengthB;
            this.maxLengthA = joint.maxLengthA;
            this.maxLengthB = joint.maxLengthB;
            this.ratio = joint.frequencyHZ;
        } else if (this.jointType == Joint.JOINT_GEAR) {
            this.ratio = joint.frequencyHZ;
            this.joint1 = -1;
            this.joint2 = -1;
            this.localAnchorA = null;
            this.localAnchorB = null;
        } else if (this.jointType == Joint.JOINT_PRISMATIC) {
            this.enableLimit = joint.enableLimit;
            this.enableMotor = joint.enableMotor;
            this.lowerTranslation = joint.lowerTranslation;
            this.upperTranslation = joint.upperTranslation;
            this.localAxisA = joint.localAxisA;
            this.maxMotorForce = joint.maxMotorTorque;
            this.motorSpeed = joint.motorSpeed;
            this.referenceAngle = joint.referenceAngle;
        } else if (this.jointType == Joint.JOINT_ROPE) {
            this.maxLength = joint.frequencyHZ;
        }
    }

}
