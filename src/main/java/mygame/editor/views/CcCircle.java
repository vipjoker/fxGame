package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mygame.editor.component.physics.FixtureDrawable;
import mygame.editor.model.box2d.B2Fixture;
import mygame.editor.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class CcCircle extends CcFixtureNode{

    private boolean active;
    Vector2 position;
    float radius;
    private final List<Vector2> points = new ArrayList<>();
    public CcCircle(CircleShape circleShape){

       this.position = circleShape.getPosition().cpy().scl(32);
       this.radius = circleShape.getRadius() * 32;
        points.add(position);
        points.add(position.cpy().add(radius,0));
    }

    public CcCircle(B2Fixture fixture){
        this.position = fixture.getCenter().cpy().scl(32);
        this.radius = fixture.getRadius() * 32;

        points.add(position);
        points.add(position.cpy().add(radius,0));
    }




    @Override
    public void rasterize(GraphicsContext context) {
        context.setFill(Constants.RED.deriveColor(1, 1, 1, 0.5));
        context.fillOval(position.x- radius, position.y - radius, radius * 2, radius * 2);
        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.strokeOval(position.x- radius, position.y - radius, radius * 2, radius * 2);
        }
    }

    @Override
    public boolean contains(Point2D point) {
        try {

            float distance = position.dst((float) point.getX(),(float) point.getY());
            return distance < radius;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Vector2> getPoints() {
        return points;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void update() {

    }
}


