package mygame.editor.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.views.CcNode;

/**
 * Created by oleh on 17.05.18.
 */
public class EditComponent extends Component {
    public EditComponent() {
    }

    @Override
    public void update() {

    }

    @Override
    public void setNode(CcNode node) {
        this.owner = node;
    }

    @Override
    public void draw(GraphicsContext g) {
        g.setFill(Color.FIREBRICK);
        g.fillRect(owner.x,owner.y,owner.width,owner.height);
        System.out.printf("COMPONENET WORKS %f %f %f %f \n", owner.x,owner.y,owner.height,owner.width);
        g.fill();
    }
}
