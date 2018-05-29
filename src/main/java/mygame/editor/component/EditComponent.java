package mygame.editor.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.views.CcNode;

/**
 * Created by oleh on 28.05.18.
 */
public class EditComponent extends Component {
    @Override
    public void setNode(CcNode node) {
        this.owner = node;
    }

    @Override
    public void update() {

    }

    @Override
    public int getZorder() {
        return 3;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public void draw(GraphicsContext g) {
        g.setFill(Color.RED);
        drawCircle(g,0,0);
        drawCircle(g,0,-owner.getHeight());
        drawCircle(g,owner.getWidth(),0);
        drawCircle(g,owner.getWidth(),-owner.getHeight());

        g.fill();
    }

    private void drawCircle(GraphicsContext g,double x,double y ){
        g.fillOval(x- 5,y-5,10,10);
    }
}
