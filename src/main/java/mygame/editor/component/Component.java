package mygame.editor.component;

import javafx.scene.canvas.GraphicsContext;
import mygame.editor.views.CcNode;

/**
 * Created by oleh on 17.05.18.
 */
public abstract class Component {
    protected CcNode owner;
    protected Component(CcNode owner){
        this.owner = owner;
    }
    public abstract void update();

    public abstract void draw(GraphicsContext g);

}
