package mygame.editor.actions;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

/**
 * Created by oleh on 3/18/17.
 */
public abstract class Drawer {
    protected Parent parent;

    protected Drawer(Parent parent) {
        this.parent = parent;
    }


    public abstract void finishDrawing();

    public abstract void mouseMoved(MouseEvent event);

    public abstract void mousePressed(MouseEvent event);

    public abstract void mouseReleased(MouseEvent event);
}
