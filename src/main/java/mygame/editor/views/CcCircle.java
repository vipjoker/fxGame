package mygame.editor.views;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mygame.editor.component.physics.FixtureDrawable;
import mygame.editor.util.Constants;

public class CcCircle implements FixtureDrawable{
    FixtureDef fixtureDef ;
    Fixture fixture;
    private double radius;
    private boolean active;
    public CcCircle(float radius){
        fixtureDef = new FixtureDef();
        fixtureDef.shape = new CircleShape();
        fixtureDef.shape.setRadius(radius);
        this.radius = radius * 32;
    }

    private float getX(){
        return CircleShape.class.cast(fixtureDef.shape).getPosition().x *32;
    }

    private float getY(){

        return CircleShape.class.cast(fixtureDef.shape).getPosition().y  * -32;
    }


    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Constants.RED.deriveColor(1, 1, 1, 0.5));
        context.fillOval(getX() - radius, getY() - radius, radius * 2, radius * 2);
        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.strokeOval(getX()- radius,getY() - radius, radius * 2, radius * 2);
        }
    }

    @Override
    public boolean contains(Point2D point) {
        try {

            Point2D center = new Point2D(getX(), getY());
            double distance = point.distance(center);
            return distance < radius;
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


