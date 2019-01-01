package mygame.editor.component;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.component.physics.FixtureDrawable;
import mygame.editor.views.NodeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 17.05.18.
 */
public class PhysicsComponent extends Component {
    private BodyDef mBodyDef;

    private List<FixtureDrawable> fixtureDrawables = new ArrayList<>();
    public PhysicsComponent(BodyDef bodyDef) {
        this.mBodyDef = bodyDef;
    }

    @Override
    public void setNode(NodeView node) {
        this.owner = node;
    }

    @Override
    public void update() {
        owner.setX(mBodyDef.position.x * 32);
        owner.setY(mBodyDef.position.y * 32);
        owner.setAngle(mBodyDef.angle * MathUtils.degRad) ;
    }

    public void addFixture(FixtureDrawable drawable){
        fixtureDrawables.add(drawable);
    }

    @Override
    public void draw(GraphicsContext g) {


        g.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.5));
        g.fillRect(-5, -5, 10, 10);
        g.fill();
        g.beginPath();
        g.setStroke(Color.GREEN);
        g.moveTo(-10, 0);
        g.lineTo(10, 0);
        g.closePath();
        g.stroke();
        g.beginPath();
        g.setStroke(Color.RED);
        g.moveTo(0, -10);
        g.lineTo(0, 10);
        g.closePath();
        g.stroke();

    }

    @Override
    public int getZorder() {
        return 0;
    }

    @Override
    public Type getType() {
        return Type.PHYSICS;
    }
}
