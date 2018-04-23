package mygame.editor.ui;

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
        setFill(Color.valueOf("#ff00ff"));

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
        shape.setAsBox(width/(2*32) ,height/(2*32));
    }


    @Override
    public Fixture create(Body body) {
        return null;
    }

    @Override
    public void update() {

    }
}
