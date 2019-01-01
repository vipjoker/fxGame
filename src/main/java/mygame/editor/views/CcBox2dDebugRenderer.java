package mygame.editor.views;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CcBox2dDebugRenderer extends NodeView {

    private final World world;
    private Array<Body> buffer = new Array<>();

    public CcBox2dDebugRenderer(World world) {
        this.world = world;
    }

    @Override
    public void rasterize(GraphicsContext context) {
        super.rasterize(context);
        world.getBodies(buffer);
        for (Body body : buffer) {


            context.save();


            float x = body.getPosition().x * 32;
            float y = body.getPosition().y * 32;
            float angle = -body.getAngle() * MathUtils.radDeg;
            context.translate(x, y);
            context.rotate(angle);

            context.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.5));
            context.fillRect(-5, -5, 10, 10);
            context.fill();
            context.beginPath();
            context.setStroke(Color.GREEN);
            context.moveTo(-10, 0);
            context.lineTo(10, 0);
            context.closePath();
            context.stroke();

            context.beginPath();
            context.setStroke(Color.RED);
            context.moveTo(0, -10);
            context.lineTo(0, 10);
            context.closePath();
            context.stroke();
            context.restore();
        }


        ;
    }
}
