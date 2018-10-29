package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public abstract class CcFixtureNode extends CcNode {

    public abstract List<Vector2> getPoints();
    public abstract void update();
}
