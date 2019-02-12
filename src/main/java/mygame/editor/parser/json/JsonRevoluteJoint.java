package mygame.editor.parser.json;

public class JsonRevoluteJoint extends Typeable {
    private String _name;
    private int _objFlags;
    private JsonId node;
    private boolean _enabled;
    private JsonVec2 anchor;
    private JsonVec2 connectedAnchor;
    private JsonId connectedBody;
    private boolean collideConnected;
    private float _maxMotorTorque;
    private float _motorSpeed;
    private boolean _enableLimit;
    private boolean _enableMotor;
    private float referenceAngle;
    private float lowerAngle;
    private float upperAngle;

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

    public float get_maxMotorTorque() {
        return _maxMotorTorque;
    }

    public void set_maxMotorTorque(float _maxMotorTorque) {
        this._maxMotorTorque = _maxMotorTorque;
    }

    public float get_motorSpeed() {
        return _motorSpeed;
    }

    public void set_motorSpeed(float _motorSpeed) {
        this._motorSpeed = _motorSpeed;
    }

    public boolean is_enableLimit() {
        return _enableLimit;
    }

    public void set_enableLimit(boolean _enableLimit) {
        this._enableLimit = _enableLimit;
    }

    public boolean is_enableMotor() {
        return _enableMotor;
    }

    public void set_enableMotor(boolean _enableMotor) {
        this._enableMotor = _enableMotor;
    }

    public float getReferenceAngle() {
        return referenceAngle;
    }

    public void setReferenceAngle(float referenceAngle) {
        this.referenceAngle = referenceAngle;
    }

    public float getLowerAngle() {
        return lowerAngle;
    }

    public void setLowerAngle(float lowerAngle) {
        this.lowerAngle = lowerAngle;
    }

    public float getUpperAngle() {
        return upperAngle;
    }

    public void setUpperAngle(float upperAngle) {
        this.upperAngle = upperAngle;
    }
}
