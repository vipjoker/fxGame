package mygame.editor.model;

public class Selectable {
    private final Class type;
    private final Object data;

    public Selectable(Class type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Class getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
