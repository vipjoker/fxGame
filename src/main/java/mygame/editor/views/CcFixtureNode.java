package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;
import mygame.editor.model.box2d.B2Fixture;

import java.util.List;

public abstract class CcFixtureNode extends CcNode {

    public abstract List<Vector2> getPoints();
    public abstract void addPoint (Point2D points);
    public abstract void update();
    public abstract B2Fixture getFixture();
}
