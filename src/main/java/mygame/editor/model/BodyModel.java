package mygame.editor.model;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;

import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class BodyModel {
    BodyDef bodyDef;
    List<FixtureDef> fixtures;
    List<JointDef> joints;

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public List<FixtureDef> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<FixtureDef> fixtures) {
        this.fixtures = fixtures;
    }

    public List<JointDef> getJoints() {
        return joints;
    }

    public void setJoints(List<JointDef> joints) {
        this.joints = joints;
    }
}
