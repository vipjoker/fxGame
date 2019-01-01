package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import mygame.editor.TimerCounter;
import mygame.editor.model.Node;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.parser.local.B2Parser;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeModel;
import mygame.editor.views.CcBodyNode;

import java.util.List;

public class Box2dAction extends Action implements TimerCounter.FrameRateCallback{
    private TimerCounter counter;
    private World world;

    public Box2dAction(CanvasRenderer renderer, NodeModel repository) {
        super(renderer,repository);


    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        world = new World(new Vector2(0,-1),true);
        List<Node> b2Bodies = mRepository.getNodes();
//        List<Body> bodies = B2Parser.createBodies(b2Bodies, world);
//        for (Body body : bodies) {
//            final CcBodyNode bodyNode = new CcBodyNode(body);
//            mRenderer.addChild(bodyNode);
//        }


        counter = new TimerCounter(this);
        counter.start();
    }


    @Override
    public void update(long delta) {
        world.step(1f/60f,8,3);
    }

    @Override
    public void finishDrawing() {
        world = null;
        counter.stop();
        mRenderer.getNodes().clear();
        mRenderer.update();
    }
}
