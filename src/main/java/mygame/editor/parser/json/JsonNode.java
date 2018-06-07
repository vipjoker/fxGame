package mygame.editor.parser.json;

import com.google.gson.JsonSerializationContext;

import java.util.List;

public class JsonNode {
    private String __type__;
    private String _name;
    private Integer _objFlags;
    private JsonId _parent;
    private List<JsonId> _children;
    private Integer _tag;
    private Boolean _active;
    private List<JsonId> _components;
    private Object _prefab;
    private String _id;
    private Integer _opacity;
    private JsonColor _color;
    private Boolean _cascadeOpacityEnabled;
    private JsonVec2 _anchorPoint;
    private JsonSize _contentSize;
    private Double _rotationX;
    private Double _rotationY;
    private Double _scaleX;
    private Double _scaleY;
    private JsonVec2 _position;
    private Double _skewX;
    private Double _skewY;
    private Double _localZOrder;
    private Double _globalZOrder;
    private Boolean _opacityModifyRGB;
    private Integer groupIndex;

    public String get__type__() {
        return __type__;
    }

    public void set__type__(String __type__) {
        this.__type__ = __type__;
    }

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

    public JsonId get_parent() {
        return _parent;
    }

    public void set_parent(JsonId _parent) {
        this._parent = _parent;
    }

    public List<JsonId> get_children() {
        return _children;
    }

    public void set_children(List<JsonId> _children) {
        this._children = _children;
    }

    public Integer get_tag() {
        return _tag;
    }

    public void set_tag(Integer _tag) {
        this._tag = _tag;
    }

    public Boolean get_active() {
        return _active;
    }

    public void set_active(Boolean _active) {
        this._active = _active;
    }

    public List<JsonId> get_components() {
        return _components;
    }

    public void set_components(List<JsonId> _components) {
        this._components = _components;
    }

    public Object get_prefab() {
        return _prefab;
    }

    public void set_prefab(Object _prefab) {
        this._prefab = _prefab;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer get_opacity() {
        return _opacity;
    }

    public void set_opacity(Integer _opacity) {
        this._opacity = _opacity;
    }

    public JsonColor get_color() {
        return _color;
    }

    public void set_color(JsonColor _color) {
        this._color = _color;
    }

    public Boolean get_cascadeOpacityEnabled() {
        return _cascadeOpacityEnabled;
    }

    public void set_cascadeOpacityEnabled(Boolean _cascadeOpacityEnabled) {
        this._cascadeOpacityEnabled = _cascadeOpacityEnabled;
    }

    public JsonVec2 get_anchorPoint() {
        return _anchorPoint;
    }

    public void set_anchorPoint(JsonVec2 _anchorPoint) {
        this._anchorPoint = _anchorPoint;
    }

    public JsonSize get_contentSize() {
        return _contentSize;
    }

    public void set_contentSize(JsonSize _contentSize) {
        this._contentSize = _contentSize;
    }

    public Double get_rotationX() {
        return _rotationX;
    }

    public void set_rotationX(Double _rotationX) {
        this._rotationX = _rotationX;
    }

    public Double get_rotationY() {
        return _rotationY;
    }

    public void set_rotationY(Double _rotationY) {
        this._rotationY = _rotationY;
    }

    public Double get_scaleX() {
        return _scaleX;
    }

    public void set_scaleX(Double _scaleX) {
        this._scaleX = _scaleX;
    }

    public Double get_scaleY() {
        return _scaleY;
    }

    public void set_scaleY(Double _scaleY) {
        this._scaleY = _scaleY;
    }

    public JsonVec2 get_position() {
        return _position;
    }

    public void set_position(JsonVec2 _position) {
        this._position = _position;
    }

    public Double get_skewX() {
        return _skewX;
    }

    public void set_skewX(Double _skewX) {
        this._skewX = _skewX;
    }

    public Double get_skewY() {
        return _skewY;
    }

    public void set_skewY(Double _skewY) {
        this._skewY = _skewY;
    }

    public Double get_localZOrder() {
        return _localZOrder;
    }

    public void set_localZOrder(Double _localZOrder) {
        this._localZOrder = _localZOrder;
    }

    public Double get_globalZOrder() {
        return _globalZOrder;
    }

    public void set_globalZOrder(Double _globalZOrder) {
        this._globalZOrder = _globalZOrder;
    }

    public Boolean get_opacityModifyRGB() {
        return _opacityModifyRGB;
    }

    public void set_opacityModifyRGB(Boolean _opacityModifyRGB) {
        this._opacityModifyRGB = _opacityModifyRGB;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }
}
