package mygame.editor.parser;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.util.List;

/**u
 * Created by oleh on 4/3/17.
 */
public class BodyWithFixture extends BodyDef {
    private List<FixtureWithShape> fixtures;
    public List<FixtureWithShape> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<FixtureWithShape> fixtures) {
        this.fixtures = fixtures;
    }



}
