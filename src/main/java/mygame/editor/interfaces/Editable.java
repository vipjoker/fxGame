package mygame.editor.interfaces;

public interface Editable {
    void move(float x,float y);
    void rotate(float angle);
    void select();
    boolean contains(float x,float y);
}
