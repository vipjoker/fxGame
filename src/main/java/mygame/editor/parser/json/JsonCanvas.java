package mygame.editor.parser.json;

public class JsonCanvas extends Typeable {
    private String _name;
    private Integer _objFlags;
    private JsonId node;
    private Boolean _enabled;
    private JsonSize _designResolution;
    private Boolean _fitWidth;
    private Boolean _fitHeight;

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

    public JsonSize get_designResolution() {
        return _designResolution;
    }

    public void set_designResolution(JsonSize _designResolution) {
        this._designResolution = _designResolution;
    }

    public Boolean get_fitWidth() {
        return _fitWidth;
    }

    public void set_fitWidth(Boolean _fitWidth) {
        this._fitWidth = _fitWidth;
    }

    public Boolean get_fitHeight() {
        return _fitHeight;
    }

    public void set_fitHeight(Boolean _fitHeight) {
        this._fitHeight = _fitHeight;
    }
}
