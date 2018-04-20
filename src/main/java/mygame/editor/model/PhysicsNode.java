package mygame.editor.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import javafx.scene.layout.Pane;
import mygame.editor.Controller;

/**
 * Created by oleh on 17.04.18.
 */
public class PhysicsNode extends Pane {
    boolean active;
    Body body;
    Controller controller;
    public PhysicsNode(Controller controller){
        this.controller = controller;
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

    public void update() {
        Vector2 position = body.getPosition();
        setRotate(body.getAngle() * MathUtils.radDeg);
        setLayoutX(position.x * 32);
        setLayoutY(position.y * 32);
    }


}