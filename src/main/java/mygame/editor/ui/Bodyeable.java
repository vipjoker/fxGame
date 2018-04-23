package mygame.editor.ui;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by oleh on 23.04.18.
 */
public interface Bodyeable extends Updateable{
    Body create(World world);
}
