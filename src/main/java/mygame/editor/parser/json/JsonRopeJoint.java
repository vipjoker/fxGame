package mygame.editor.parser.json;

public class JsonRopeJoint extends Typeable {
    private String _name;
    private int _objFlags;
    private JsonId node;
    private boolean _enabled;
    private JsonVec2 anchor;
    private JsonVec2 connectedAnchor;
    private JsonId connectedBody;
    private boolean collideConnected;
    private float _maxLength;

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

    public float get_maxLength() {
        return _maxLength;
    }

    public void set_maxLength(float _maxLength) {
        this._maxLength = _maxLength;
    }
}