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
    private boolean isRotating = false;
    private Point2D rotatateHandle = new Point2D(-40,0);
    private double radius = 10;
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

    public void setRotateFlat(boolean rotate){
        this.isRotating = rotate;
    }

    @Override
    public int getZorder() {
        return 3;
    }

    @Override
    public Type getType() {
        return Type.EDIT;
    }

    public boolean canBeResized(Point2D p){
//        Point2D point2D = owner.convertToLocalSpace(p);
        Point2D normalized = new Point2D(p.getX(),-p.getY());
        return rectangle2D.contains(normalized);
    }

    public boolean canBeRotated(Point2D point){
        Point2D normalized = new Point2D(point.getX(),-point.getY());
        return normalized.distance(rotatateHandle) < radius;
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


            drawArrow(g);
            g.fillText(owner.convertToLocalSpace(new Point2D(-1,0)).toString(),0,0);
//            drawCircle(g, owner.getWidth(), -owner.getHeight());
            g.fill();
        }
    }

    private void drawArrow(GraphicsContext g) {
        g.beginPath();
        g.setStroke(isRotating ? Color.GREEN :Color.RED);
        g.moveTo(0,0);
        g.setLineWidth(3);
        g.lineTo(rotatateHandle.getX(),rotatateHandle.getY());
        g.closePath();
        g.stroke();
        g.fillOval(rotatateHandle.getX() -radius,rotatateHandle.getY() -radius,radius*2,radius*2);
    }

    private void drawRect(GraphicsContext g,double x,double y){
        rectangle2D = new Rectangle2D(x-10,y-10,20,20);
        g.fillRect(rectangle2D.getMinX(),rectangle2D.getMinY(),rectangle2D.getWidth(),rectangle2D.getHeight());
    }

    private void drawCircle(GraphicsContext g,double x,double y ){
        g.fillOval(x- 5,y-5,10,10);
    }
}
