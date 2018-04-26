package mygame.editor.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
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
        setStroke(Color.CYAN);
        setStrokeWidth(1);
    }

    @Override
    public Fixture create(Body body) {
        return body.createFixture(fixtureDef);
    }

    public void setPoints(Vector2... points) {
        chainShape.createChain(points);
        boolean first = true;
        for (Vector2 point : points) {
            if(first){
                getElements().add(new MoveTo(point.x * 32,point.y* 32));
                first = false;
            }else{
                getElements().add(new LineTo(point.x * 32, point.y* 32));
            }
        }

    }

    @Override
    public void update() {

    }
}
