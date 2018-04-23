package mygame.editor.ui;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by oleh on 23.04.18.
 */
public interface Fixtureable extends Updateable{
    Fixture create(Body body);
}
