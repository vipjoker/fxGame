package mygame.editor.component;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.views.CcNode;

/**
 * Created by oleh on 28.05.18.
 */
public class EditComponent extends Component {
    private Rectangle2D rectangle2D;
    private boolean isActive = false;
    @Override
    public void setNode(CcNode node) {
        this.owner = node;
    }

    @Override
    public void update() {

    }

    public void setActive(boolean active){
        this.isActive = active;
    }

    @Override
    public int getZorder() {
        return 3;
    }

    @Override
    public Type getType() {
        return Type.EDIT;
    }

    public boolean contains(Point2D p){
//        Point2D point2D = owner.convertToLocalSpace(p);
        Point2D normalized = new Point2D(p.getX(),-p.getY());
        return rectangle2D.contains(normalized);
    }

    @Override
    public void draw(GraphicsContext g) {



        if(owner.active) {

            g.setStroke(Color.GREEN);
            g.strokeRect(-5, -owner.getHeight() - 5, owner.getWidth()+ 10, owner.getHeight()+ 10);
            g.stroke();



            g.setFill(isActive ?Color.RED:Color.GREEN);
//            drawCircle(g, 0, 0);
//            drawCircle(g, 0, -owner.getHeight());
//            drawCircle(g, owner.getWidth(), 0);
            drawRect(g,owner.getWidth(),-owner.getHeight());
//            drawCircle(g, owner.getWidth(), -owner.getHeight());
            g.fill();
        }
    }

    private void drawRect(GraphicsContext g,double x,double y){
        rectangle2D = new Rectangle2D(x-10,y-10,20,20);
        g.fillRect(rectangle2D.getMinX(),rectangle2D.getMinY(),rectangle2D.getWidth(),rectangle2D.getHeight());
    }

    private void drawCircle(GraphicsContext g,double x,double y ){
        g.fillOval(x- 5,y-5,10,10);
    }
}
