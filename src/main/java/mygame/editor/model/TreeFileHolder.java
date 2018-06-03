package mygame.editor.model;

public class TreeFileHolder {
    final String name;
    final String path;

    public TreeFileHolder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
