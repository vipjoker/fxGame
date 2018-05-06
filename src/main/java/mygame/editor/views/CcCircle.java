package mygame.editor.views;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.CircleShape;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mygame.editor.util.Constants;

public class CcCircle extends CcNode {

    private CircleShape mCircleShape;
    private double radius;

    public CcCircle(CircleShape circleShape){
        this.mCircleShape = circleShape;
        this.x = circleShape.getPosition().x *32;
        this.y = -circleShape.getPosition().y  * 32;

        this.radius = circleShape.getRadius() * 32;
    }

    public CcCircle(double x, double y, double radius) {
        this.x = x;
        this.y = y;

        this.radius = radius;

    }

    @Override
    public void rasterize(GraphicsContext context) {
        transform = context.getTransform();
        context.setFill(Constants.RED.deriveColor(1, 1, 1, 0.5));
        context.fillOval(this.x - radius, this.y - radius, radius * 2, radius * 2);
        if (active) {
            context.setLineWidth(2);
            context.setStroke(Constants.GREEN);
            context.strokeOval(this.x - radius, this.y - radius, radius * 2, radius * 2);
        }
    }

    @Override
    public boolean contains(double x, double y) {
        try {
            Point2D transform = this.transform.inverseTransform(x, y);
            Point2D center = new Point2D(this.x, this.y);
            double distance = transform.distance(center);
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


