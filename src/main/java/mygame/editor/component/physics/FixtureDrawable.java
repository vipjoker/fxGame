package mygame.editor.component.physics;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mygame.editor.util.Constants;

/**
 * Created by oleh on 01.06.18.
 */
public interface FixtureDrawable {

    void draw(GraphicsContext context);

    boolean contains(Point2D point);

    void setActive(boolean active);
}
