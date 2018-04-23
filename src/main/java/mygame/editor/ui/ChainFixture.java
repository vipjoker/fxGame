package mygame.editor.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import javafx.scene.shape.Path;

/**
 * Created by oleh on 23.04.18.
 */
public class ChainFixture extends Path implements Fixtureable {
    private ChainShape chainShape = new ChainShape();
    private FixtureDef fixtureDef;
    public ChainFixture(){
        fixtureDef = new FixtureDef();
        Material material = Material.DEFAULT();
        fixtureDef.shape = chainShape;
        fixtureDef.restitution = material.getRestitution();
        fixtureDef.friction = material.getFriction();
        fixtureDef.density = material.getDensity();
    }

    @Override
    public Fixture create(Body body) {
        return null;
    }

    public void setPoints(Vector2... points) {
        for (Vector2 point : points) {
            chainShape.setNextVertex(point);
        }
    }

    @Override
    public void update() {

    }
}
