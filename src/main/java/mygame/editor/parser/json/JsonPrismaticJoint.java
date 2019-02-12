package mygame.editor.parser.json;

public class JsonPrismaticJoint extends Typeable {

        private String _name;
        private int _objFlags;
        private JsonId node;
        private boolean _enabled;
        private JsonVec2 anchor;
        private JsonVec2 connectedAnchor;
        private JsonId connectedBody;
        private boolean collideConnected;
        private JsonVec2 localAxisA;
        private float referenceAngle;
        private boolean enableLimit;
        private boolean enableMotor;
        private float lowerLimit;
        private float upperLimit;
        private float _maxMotorForce;
        private float _motorSpeed;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_objFlags() {
        return _objFlags;
    }

    public void set_objFlags(int _objFlags) {
        this._objFlags = _objFlags;
    }

    public JsonId getNode() {
        return node;
    }

    public void setNode(JsonId node) {
        this.node = node;
    }

    public boolean is_enabled() {
        return _enabled;
    }

    public void set_enabled(boolean _enabled) {
        this._enabled = _enabled;
    }

    public JsonVec2 getAnchor() {
        return anchor;
    }

    public void setAnchor(JsonVec2 anchor) {
        this.anchor = anchor;
    }

    public JsonVec2 getConnectedAnchor() {
        return connectedAnchor;
    }

    public void setConnectedAnchor(JsonVec2 connectedAnchor) {
        this.connectedAnchor = connectedAnchor;
    }

    public JsonId getConnectedBody() {
        return connectedBody;
    }

    public void setConnectedBody(JsonId connectedBody) {
        this.connectedBody = connectedBody;
    }

    public boolean isCollideConnected() {
        return collideConnected;
    }

    public void setCollideConnected(boolean collideConnected) {
        this.collideConnected = collideConnected;
    }

    public JsonVec2 getLocalAxisA() {
        return localAxisA;
    }

    public void setLocalAxisA(JsonVec2 localAxisA) {
        this.localAxisA = localAxisA;
    }

    public float getReferenceAngle() {
        return referenceAngle;
    }

    public void setReferenceAngle(float referenceAngle) {
        this.referenceAngle = referenceAngle;
    }

    public boolean isEnableLimit() {
        return enableLimit;
    }

    public void setEnableLimit(boolean enableLimit) {
        this.enableLimit = enableLimit;
    }

    public boolean isEnableMotor() {
        return enableMotor;
    }

    public void setEnableMotor(boolean enableMotor) {
        this.enableMotor = enableMotor;
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

    public float get_maxMotorForce() {
        return _maxMotorForce;
    }

    public void set_maxMotorForce(float _maxMotorForce) {
        this._maxMotorForce = _maxMotorForce;
    }

    public float get_motorSpeed() {
        return _motorSpeed;
    }

    public void set_motorSpeed(float _motorSpeed) {
        this._motorSpeed = _motorSpeed;
    }
}
