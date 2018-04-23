package mygame.editor.ui;

import com.badlogic.gdx.physics.box2d.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by oleh on 23.04.18.
 */
public class CircleFixture extends Circle implements Fixtureable {
    CircleShape shape = new CircleShape();
    FixtureDef fixtureDef;
    public CircleFixture(){
        setFill(Color.WHEAT);
        fixtureDef = new FixtureDef();
        Material material = Material.DEFAULT();
        fixtureDef.density = material.getDensity();
        fixtureDef.friction = material.getFriction();
        fixtureDef.restitution = material.getRestitution();
        fixtureDef.shape = shape;
    }

    public void setFixtureRadius(float radius){
        setRadius(radius * 32);
        shape.setRadius(radius);
    }

    @Override
    public Fixture create(Body body) {
        return body.createFixture(fixtureDef);
    }

    @Override
    public void update() {

    }
}
