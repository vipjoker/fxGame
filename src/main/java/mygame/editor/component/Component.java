package mygame.editor.component;

import javafx.scene.canvas.GraphicsContext;
import mygame.editor.views.NodeView;

/**
 * Created by oleh on 17.05.18.
 */
public abstract class Component {

    protected NodeView owner;

    protected Component() {

    }

    public abstract void setNode(NodeView node);

    public abstract void update();

    public abstract int getZorder();

    public abstract Type getType();

    public abstract void draw(GraphicsContext g);

    public enum Type {
        SPRITE,
        EDIT,
        PHYSICS
    }
}
