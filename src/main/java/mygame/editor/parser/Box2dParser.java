package mygame.editor.parser;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import mygame.editor.customShapes.Circle;
import mygame.editor.customShapes.Drawable;
import mygame.editor.customShapes.Polygon;
import mygame.editor.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/26/17.
 */
public class Box2dParser {

    public static List<Drawable> parseBox2d(World world){
        List<Drawable> list = new ArrayList<>();
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies)
           for (Fixture fixture : body.getFixtureList())
                list.add(getDrawableFromFixture(fixture));


        return list;
    }

    private static Drawable getDrawableFromFixture(Fixture fixture){
        Shape shape  = fixture.getShape();

        switch (shape.getType()){
            case Circle:
                return createCircle(fixture.getBody(),(CircleShape) shape);
            case Polygon:
                return createPolygon(fixture.getBody(),(PolygonShape)shape);
            case Chain:
                break;

            case Edge:
                break;

        }

        throw new RuntimeException("Not implemented");

    }

    private static Circle createCircle(Body body , CircleShape circleShape){
        float radius = circleShape.getRadius();
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        return new Circle(x,y,radius);
    }

    private static Polygon createPolygon(Body body , PolygonShape polygonShape){
        List<Point> points = new ArrayList<>();
        for(int i = 0 ; i < polygonShape.getVertexCount();i++){
            Vector2 vector = new Vector2();
            polygonShape.getVertex(i,vector);
            points.add(new Point(vector.x + body.getPosition().x,vector.y + body.getPosition().y));
        }
        return new Polygon(points);
    }



}
