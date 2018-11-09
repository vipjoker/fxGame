package mygame.editor.model.box2d;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import mygame.editor.interfaces.Editable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 17.04.18.
 */
public class B2Body implements Editable{
    private String name;
    private float angle;
    private float angularVelocity;
    private Boolean awake;
    private List<B2Fixture> fixture = new ArrayList<>();


    private B2Point linearVelocity;
    private B2Point position;
    private B2Type type;



    public B2Body(){

    }
    public B2Body (B2Type type, B2Point position){
        this.type = type;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public Boolean getAwake() {
        return awake;
    }

    public void setAwake(Boolean awake) {
        this.awake = awake;
    }

    public List<B2Fixture> getFixture() {
        return fixture;
    }

    public void setFixture(List<B2Fixture> fixture) {
        this.fixture = fixture;
    }

    public B2Point getPosition() {
        return position;
    }

    public void setPosition(B2Point position) {
        this.position = position;
    }

    public B2Type getType() {
        return type;
    }

    public void setType(B2Type type) {
        this.type = type;
    }


    public B2Point getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(B2Point linearVelocity) {


        this.linearVelocity = linearVelocity;
    }

    public void addFixture(B2Fixture fixture){
        this.fixture.add(fixture);
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

        System.out.println("Contains " + x + " " + y );

        final Rectangle rectangle = new Rectangle(0, 0, 10, 10);

        return rectangle.contains(x,y);
    }
}
