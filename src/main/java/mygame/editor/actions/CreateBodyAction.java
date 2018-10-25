package mygame.editor.actions;


import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;
import mygame.editor.model.box2d.*;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcEditBodyNode;

import java.util.List;

public class CreateBodyAction extends Action implements CanvasRenderer.OnCanvasDragListener{

    private final Mode mode;

    public CreateBodyAction(CanvasRenderer renderer, NodeRepository repository, Mode mode) {
        super(renderer,repository);
        this.mode = mode;
    }


    @Override
    public void init() {

        mRenderer.setOnCanvasDragListener(this);
        final List<B2Body> bodies = mRepository.getBodies();
        for (B2Body body : bodies) {
            mRenderer.addChild(new CcEditBodyNode(body));
        }
        mRenderer.update();

    }

    @Override
    public void finishDrawing() {

    }

    @Override
    public void onStartMove(Point2D point) {

        float x = (float) point.getX()/32;
        float y = (float) point.getY()/32;




        B2Body body = null;
        switch (mode){
            case SQUARE: {
                body = new B2Body(B2Type.DYNAMIC, new B2Point(x, y));
                B2Fixture fixture = new B2Fixture(B2FixtureType.POLYGON, Vector2.Zero, new Vector2(1, 0),new Vector2(1,1),new Vector2(0,1));
                body.addFixture(fixture);
            }
                break;
            case CIRCLE: {
                body = new B2Body(B2Type.DYNAMIC, new B2Point(x, y));
                B2Fixture fixture = new B2Fixture(B2FixtureType.CIRCLE, Vector2.Zero, new Vector2(0, 1));
                body.addFixture(fixture);
            }
                break;
            case CHAIN:{
                body = new B2Body(B2Type.STATIC, new B2Point(x, y));
                B2Fixture fixture = new B2Fixture(B2FixtureType.CHAIN, Vector2.Zero, new Vector2(1, 1),new Vector2(2,0),new Vector2(3,1));
                body.addFixture(fixture);
            }
                break;
            case EDGE:{

            }
                break;
        }







        CcEditBodyNode bodyNode = new CcEditBodyNode(body);


        mRepository.saveBody(body);
        mRenderer.addChild(bodyNode);
        mRenderer.update();
    }

    @Override
    public void onDrag(Point2D point) {

    }

    @Override
    public void onStopMove(Point2D point) {

    }
    public enum Mode{
        SQUARE,CIRCLE,EDGE,CHAIN
    }
}
