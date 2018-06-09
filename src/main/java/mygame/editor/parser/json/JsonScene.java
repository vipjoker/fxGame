package mygame.editor.parser.json;

import java.util.List;

public class JsonScene extends Typeable{
    private Integer _objFlags;
    private Object _parent;
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
    private Integer _localZOrder;
    private Integer _globalZOrder;
    private Boolean _opacityModifyRGB;
    private Integer groupIndex;
    private Boolean autoReleaseAssets;

    public Integer get_objFlags() {
        return _objFlags;
    }

    public void set_objFlags(Integer _objFlags) {
        this._objFlags = _objFlags;
    }

    public Object get_parent() {
        return _parent;
    }

    public void set_parent(Object _parent) {
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

    public Integer get_localZOrder() {
        return _localZOrder;
    }

    public void set_localZOrder(Integer _localZOrder) {
        this._localZOrder = _localZOrder;
    }

    public Integer get_globalZOrder() {
        return _globalZOrder;
    }

    public void set_globalZOrder(Integer _globalZOrder) {
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

    public Boolean getAutoReleaseAssets() {
        return autoReleaseAssets;
    }

    public void setAutoReleaseAssets(Boolean autoReleaseAssets) {
        this.autoReleaseAssets = autoReleaseAssets;
    }
}
