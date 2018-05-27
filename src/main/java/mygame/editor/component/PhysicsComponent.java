package mygame.editor.component;

import javafx.scene.canvas.GraphicsContext;
import mygame.editor.views.CcNode;

/**
 * Created by oleh on 17.05.18.
 */
public class PhysicsComponent extends Component {

    public PhysicsComponent() {

    }

    @Override
    public void setNode(CcNode node) {
        this.owner = node;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(GraphicsContext g) {

    }
}
