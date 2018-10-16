package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mygame.editor.component.physics.FixtureDrawable;
import mygame.editor.util.Constants;

public class CcCircle extends CcNode{

    private boolean active;
    private CircleShape circleShape;
    Vector2 position;
    public CcCircle(CircleShape circleShape){
       this.circleShape  = circleShape;
    }




    @Override
    public void rasterize(GraphicsContext context) {
        context.setFill(Constants.RED.deriveColor(1, 1, 1, 0.5));
        context.fillOval(circleShape.getPosition().x- circleShape.getRadius(), circleShape.getPosition().y - circleShape.getRadius(), circleShape.getRadius() * 2, circleShape.getRadius() * 2);
        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.strokeOval(circleShape.getPosition().x- circleShape.getRadius(), circleShape.getPosition().y - circleShape.getRadius(), circleShape.getRadius() * 2, circleShape.getRadius() * 2);
        }
    }

    @Override
    public boolean contains(Point2D point) {
        try {

            Point2D center = new Point2D(circleShape.getPosition().x, circleShape.getPosition().y);
            double distance = point.distance(center);
            return distance < circleShape.getRadius();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}


