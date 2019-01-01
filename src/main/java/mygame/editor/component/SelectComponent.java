package mygame.editor.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.views.NodeView;

/**
 * Created by oleh on 17.05.18.
 */
public class SelectComponent extends Component {
    public SelectComponent() {
    }

    @Override
    public void update() {

    }

    @Override
    public void setNode(NodeView node) {
        this.owner = node;
    }

    @Override
    public void draw(GraphicsContext g) {
        if(owner.active) {
            g.setStroke(Color.CYAN);
            g.strokeRect(-5, -owner.getHeight().doubleValue() - 5, owner.getWidth().doubleValue()+ 10, owner.getHeight().doubleValue()+ 10);
            g.stroke();
        }
    }


    @Override
    public int getZorder() {
        return 0;
    }

    @Override
    public Type getType() {
        return Type.EDIT;
    }
}
