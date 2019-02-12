package mygame.editor.parser.json;

import com.google.gson.JsonSerializationContext;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonNode extends Typeable{
    private String _name;
    private Integer _objFlags;
    private JsonId _parent;
    private List<JsonId> _children;
    private Integer _tag;
    private Boolean _active;
    @SerializedName("_components")

    private List<JsonId> components;
    private Object _prefab;
    private String _id;
    private Integer _opacity;
    private JsonColor _color;
    private Boolean _cascadeOpacityEnabled;
    private JsonVec2 _anchorPoint;
    private JsonVec2 _scale;
    private JsonSize _contentSize;
    private JsonQuat _quat;

    private JsonVec2 _position;
    private Double _skewX;
    private Double _skewY;
    private Double _localZOrder;
    private Double _globalZOrder;
    private Boolean _opacityModifyRGB;
    private Integer groupIndex;

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

    public List<JsonId> getComponents() {
        return components;
    }

    public void setComponents(List<JsonId> components) {
        this.components = components;
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

    public JsonQuat get_quat() {
        return _quat;
    }

    public void set_quat(JsonQuat _quat) {
        this._quat = _quat;
    }

    public JsonVec2 get_scale() {
        return _scale;
    }

    public void set_scale(JsonVec2 _scale) {
        this._scale = _scale;
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
