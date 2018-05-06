package mygame.editor.customShapes;

import com.badlogic.gdx.physics.box2d.Fixture;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import mygame.editor.model.Point;

/**
 * Created by oleh on 3/26/17.
 */
public class Circle implements Drawable,OnDragListener{
    Fixture fixture;
    double x,y,radius;
    Paint paint;

    public Circle(double x,double y,double radius){
        System.out.println("Circle created x " + " y " + y + " radius " + radius);
        this.x = x;
        this.y = y;
        this.radius = radius;
        paint = Color.rgb(0,100,0,.6);
    }

    @Override
    public void draw(GraphicsContext context,long time) {
        context.setFill(paint);
        context.fillOval(x * 32,y *32,radius *32,radius * 32);
    }

    @Override
    public void onDrag(Point point) {
        paint = Color.RED;
    }

    @Override
    public boolean intersect(double x, double y) {
        return radius > java.awt.geom.Point2D.distance(this.x,this.y,x,y);

    }
}

