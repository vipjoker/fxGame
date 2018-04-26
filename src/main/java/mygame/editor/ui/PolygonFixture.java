package mygame.editor.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Created by oleh on 23.04.18.
 */
public class PolygonFixture extends Polygon implements Fixtureable {
    PolygonShape shape = new PolygonShape();
    FixtureDef fixtureDef;
    public PolygonFixture(){
        setFill(Color.RED);

        fixtureDef = new FixtureDef();
        Material material = Material.DEFAULT();
        fixtureDef.density = material.getDensity();
        fixtureDef.friction = material.getFriction();
        fixtureDef.restitution = material.getRestitution();
        fixtureDef.shape = shape;
    }

    public void setPoints(float[] points){
        shape.set(points);
    }

    public void setRect(float width,float height){
        shape.setAsBox(width/(32) ,height/(32));
        for(int i= 0; i < shape.getVertexCount();i++){
            Vector2 vector2 = new Vector2();
            shape.getVertex(i,vector2);
            getPoints().addAll((double)vector2.x *32 ,(double)vector2.y *32);
        }



    }


    @Override
    public Fixture create(Body body) {
        return null;
    }

    @Override
    public void update() {

    }
}
