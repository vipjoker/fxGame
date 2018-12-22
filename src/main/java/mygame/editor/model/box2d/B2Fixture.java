package mygame.editor.model.box2d;

import com.badlogic.gdx.math.Vector2;
import mygame.editor.interfaces.Editable;
import physicsPort.triangulation.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 17.04.18.
 */
public class B2Fixture implements Editable{
    private String name;
    private float density = 1;
    private float friction;
    private float restitution;
    private List<Vector2> points = new ArrayList<>();
    private final B2FixtureType type;


    public B2Fixture(B2FixtureType type, Vector2... points){
        this.type = type;
        for (Vector2 point : points) {

            this.points.add(point);
        }

    }
public B2Fixture(B2FixtureType type, List<Vector2> points){
        this.type = type;
        for (Vector2 point : points) {

            this.points.add(point);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getRadius(){
        return points.get(0).dst(points.get(1));
    }

    public List<Vector2> getPoints() {
        return points;
    }

    public B2FixtureType getType() {
        return type;
    }

    public Vector2 getCenter(){
        return points.get(0);
    }

    @Override
    public void move(float x, float y) {

    }

    @Override
    public void rotate(float angle) {

    }

    @Override
    public void select() {

    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }
}
