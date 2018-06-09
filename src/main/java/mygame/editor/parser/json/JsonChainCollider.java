package mygame.editor.parser.json;

import java.util.List;

public class JsonChainCollider extends Typeable {

    private String _name;
    private Integer _objFlags;
    private JsonId node;
    private Boolean _enabled;
    private Integer tag;
    private Double _density;
    private Boolean _sensor;
    private Double _friction;
    private Double _restitution;
    private Object body; //TODO
    private Boolean loop;
    private List<JsonVec2> points;

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

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Double get_density() {
        return _density;
    }

    public void set_density(Double _density) {
        this._density = _density;
    }

    public Boolean get_sensor() {
        return _sensor;
    }

    public void set_sensor(Boolean _sensor) {
        this._sensor = _sensor;
    }

    public Double get_friction() {
        return _friction;
    }

    public void set_friction(Double _friction) {
        this._friction = _friction;
    }

    public Double get_restitution() {
        return _restitution;
    }

    public void set_restitution(Double _restitution) {
        this._restitution = _restitution;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

    public List<JsonVec2> getPoints() {
        return points;
    }

    public void setPoints(List<JsonVec2> points) {
        this.points = points;
    }
}
