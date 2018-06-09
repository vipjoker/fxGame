package mygame.editor.parser.json;

public class JsonSprite extends Typeable{

    private String _name;
    private Integer _objFlags;
    private JsonId node;
    private Boolean _enabled;
    private JsonSpriteFrame _spriteFrame;
    private Integer _type;
    private Integer _sizeMode;
    private Integer _fillType;
    private JsonVec2 _fillCenter;
    private Integer _fillStart;
    private Integer _fillRange;
    private Integer _srcBlendFactor;
    private Integer _dstBlendFactor;
    private Boolean _isTrimmedMode;
    private Object _atlas;


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

    public JsonSpriteFrame get_spriteFrame() {
        return _spriteFrame;
    }

    public void set_spriteFrame(JsonSpriteFrame _spriteFrame) {
        this._spriteFrame = _spriteFrame;
    }

    public Integer get_type() {
        return _type;
    }

    public void set_type(Integer _type) {
        this._type = _type;
    }

    public Integer get_sizeMode() {
        return _sizeMode;
    }

    public void set_sizeMode(Integer _sizeMode) {
        this._sizeMode = _sizeMode;
    }

    public Integer get_fillType() {
        return _fillType;
    }

    public void set_fillType(Integer _fillType) {
        this._fillType = _fillType;
    }

    public JsonVec2 get_fillCenter() {
        return _fillCenter;
    }

    public void set_fillCenter(JsonVec2 _fillCenter) {
        this._fillCenter = _fillCenter;
    }

    public Integer get_fillStart() {
        return _fillStart;
    }

    public void set_fillStart(Integer _fillStart) {
        this._fillStart = _fillStart;
    }

    public Integer get_fillRange() {
        return _fillRange;
    }

    public void set_fillRange(Integer _fillRange) {
        this._fillRange = _fillRange;
    }

    public Integer get_srcBlendFactor() {
        return _srcBlendFactor;
    }

    public void set_srcBlendFactor(Integer _srcBlendFactor) {
        this._srcBlendFactor = _srcBlendFactor;
    }

    public Integer get_dstBlendFactor() {
        return _dstBlendFactor;
    }

    public void set_dstBlendFactor(Integer _dstBlendFactor) {
        this._dstBlendFactor = _dstBlendFactor;
    }

    public Boolean get_isTrimmedMode() {
        return _isTrimmedMode;
    }

    public void set_isTrimmedMode(Boolean _isTrimmedMode) {
        this._isTrimmedMode = _isTrimmedMode;
    }

    public Object get_atlas() {
        return _atlas;
    }

    public void set_atlas(Object _atlas) {
        this._atlas = _atlas;
    }
}
