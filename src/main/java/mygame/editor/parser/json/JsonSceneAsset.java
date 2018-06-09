package mygame.editor.parser.json;

public class JsonSceneAsset extends Typeable {
    private String _name;
    private Integer _objFlags;
    private Object _rawFiles;
    private JsonId scene;

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

    public Object get_rawFiles() {
        return _rawFiles;
    }

    public void set_rawFiles(Object _rawFiles) {
        this._rawFiles = _rawFiles;
    }

    public JsonId getScene() {
        return scene;
    }

    public void setScene(JsonId scene) {
        this.scene = scene;
    }
}
