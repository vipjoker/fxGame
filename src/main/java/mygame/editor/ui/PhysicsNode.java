package mygame.editor.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import javafx.scene.layout.Pane;
import mygame.editor.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 17.04.18.
 */
public class PhysicsNode extends Pane implements Bodyeable {
    boolean active;
    private Body body;
    private BodyDef bodyDef;
    private Controller controller;
    private List<Fixtureable> fixtures = new ArrayList<>();
    public PhysicsNode(Controller controller){
        this.controller = controller;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        setWidth(50);
        setHeight(50);
    }

    public void toggleActive() {
        active = !active;
        if (active) setActive();
        else setNonActive();
    }

    public void setActive() {
        setStyle("-fx-border-color: #f0f; -fx-border-width: 5px;-fx-border-radius: 5px;");
    }

    public void setNonActive() {
        setStyle("-fx-border-color: none");
    }

    public void setPosition(float x ,float y){
        bodyDef.position.set(x/32f,y/32f);
        setLayoutX(x);
        setLayoutY(y);
    }

    public void addFixture(Fixtureable fixtureable){
        fixtures.add(fixtureable);
    }

    @Override
    public Body create(World world) {
        body = world.createBody(bodyDef);

        for (Fixtureable fixtureable : fixtures) {
            fixtureable.create(body);
        }

        return body;
    }



    @Override
    public void update() {
        Vector2 position = body.getPosition();
        setRotate(body.getAngle() * MathUtils.radDeg);
        setLayoutX(position.x * 32);
        setLayoutY(position.y * 32);
    }


}