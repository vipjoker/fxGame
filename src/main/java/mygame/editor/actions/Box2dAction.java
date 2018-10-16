package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import mygame.editor.TimerCounter;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.parser.local.B2Parser;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcBodyNode;
import mygame.editor.views.CcBox2dDebugRenderer;

import java.util.List;

public class Box2dAction extends Action implements TimerCounter.FrameRateCallback{
    private TimerCounter counter;
    private World world;

    public Box2dAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);


    }

    @Override
    public void init() {
        world = new World(new Vector2(0,-1),true);
        List<B2Body> b2Bodies = mRepository.getBodies();
        List<Body> bodies = B2Parser.createBodies(b2Bodies, world);
        for (Body body : bodies) {
            final CcBodyNode bodyNode = new CcBodyNode(body);
            mRenderer.addChild(bodyNode);
        }


        counter = new TimerCounter(this);
        counter.start();
    }


    @Override
    public void update(long delta) {

        world.step(1f/60f,8,3);

        mRenderer.update();
    }

    @Override
    public void finishDrawing() {
        world = null;
        counter.stop();
        mRenderer.getNodes().clear();
        mRenderer.update();
    }
}
