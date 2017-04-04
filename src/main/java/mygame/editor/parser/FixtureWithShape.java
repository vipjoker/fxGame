package mygame.editor.parser;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import mygame.editor.model.AbstractModel;

/**
 * Created by oleh on 4/3/17.
 */
public class FixtureWithShape extends FixtureDef{
    AbstractModel shape;

    public AbstractModel getShape() {
        return shape;
    }

    public void setShape(AbstractModel shape) {
        this.shape = shape;
    }
}
