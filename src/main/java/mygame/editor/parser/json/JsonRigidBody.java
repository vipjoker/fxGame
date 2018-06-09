package mygame.editor.parser.json;

public class JsonRigidBody extends Typeable{

    private String _name;
    private Integer _objFlags;
    private JsonId node;
    private Boolean _enabled;
    private Integer _type;
    private Boolean _allowSleep;
    private Double _gravityScale;
    private Double _linearDamping;
    private Double _angularDamping;
    private JsonVec2 _linearVelocity;
    private Double _angularVelocity;
    private Boolean _fixedRotation;
    private Boolean enabledContactListener;
    private Boolean bullet;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public Integer get_objFlags() {
        return _objFlags;
    }

    public void set_objFlags(Integer _objFlags) {
        this._objFlags = _objFlags;
    }

    public JsonId getNode() {
        return node;
    }

    public void setNode(JsonId node) {
        this.node = node;
    }

    public Boolean get_enabled() {
        return _enabled;
    }

    public void set_enabled(Boolean _enabled) {
        this._enabled = _enabled;
    }

    public Integer get_type() {
        return _type;
    }

    public void set_type(Integer _type) {
        this._type = _type;
    }

    public Boolean get_allowSleep() {
        return _allowSleep;
    }

    public void set_allowSleep(Boolean _allowSleep) {
        this._allowSleep = _allowSleep;
    }

    public Double get_gravityScale() {
        return _gravityScale;
    }

    public void set_gravityScale(Double _gravityScale) {
        this._gravityScale = _gravityScale;
    }

    public Double get_linearDamping() {
        return _linearDamping;
    }

    public void set_linearDamping(Double _linearDamping) {
        this._linearDamping = _linearDamping;
    }

    public Double get_angularDamping() {
        return _angularDamping;
    }

    public void set_angularDamping(Double _angularDamping) {
        this._angularDamping = _angularDamping;
    }

    public JsonVec2 get_linearVelocity() {
        return _linearVelocity;
    }

    public void set_linearVelocity(JsonVec2 _linearVelocity) {
        this._linearVelocity = _linearVelocity;
    }

    public Double get_angularVelocity() {
        return _angularVelocity;
    }

    public void set_angularVelocity(Double _angularVelocity) {
        this._angularVelocity = _angularVelocity;
    }

    public Boolean get_fixedRotation() {
        return _fixedRotation;
    }

    public void set_fixedRotation(Boolean _fixedRotation) {
        this._fixedRotation = _fixedRotation;
    }

    public Boolean getEnabledContactListener() {
        return enabledContactListener;
    }

    public void setEnabledContactListener(Boolean enabledContactListener) {
        this.enabledContactListener = enabledContactListener;
    }

    public Boolean getBullet() {
        return bullet;
    }

    public void setBullet(Boolean bullet) {
        this.bullet = bullet;
    }
}
