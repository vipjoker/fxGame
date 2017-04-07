package mygame.editor.parser;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import mygame.editor.view.AbstractView;

/**
 * Created by oleh on 4/3/17.
 */
public class FixtureWithShape extends FixtureDef{
    AbstractView shape;

    public AbstractView getShape() {
        return shape;
    }

    public void setShape(AbstractView shape) {
        this.shape = shape;
    }
}
