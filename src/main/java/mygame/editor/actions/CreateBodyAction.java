package mygame.editor.actions;


import javafx.geometry.Point2D;
import mygame.editor.model.box2d.*;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeModel;
import mygame.editor.util.Constants;
import mygame.editor.views.CcEditBodyNode;

import java.util.List;

public class CreateBodyAction extends Action implements CanvasRenderer.OnCanvasDragListener{



    public CreateBodyAction(CanvasRenderer renderer, NodeModel repository) {
        super(renderer,repository);
        this.mode = Constants.PARAM_SQUARE;
    }



    @Override
    public void init() {
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasDragListener(this);
//        final List<B2Body> bodies = mRepository.getBodies();
//        for (B2Body body : bodies) {
//            mRenderer.addChild(new CcEditBodyNode(body));
//        }
//        mRenderer.update();

    }

    @Override
    public void finishDrawing() {

    }

    @Override
    public void onStartMove(Point2D point) {

        float x = (float) point.getX()/32;
        float y = (float) point.getY()/32;



        CcEditBodyNode bodyNode =  CcEditBodyNode.create(mode,x,y);

//        body.setName("Body " + mRepository.getBodies().size());
//        mRepository.saveBody(body);



        mRenderer.addChild(bodyNode);
        mRenderer.update();
    }

    @Override
    public void onDrag(Point2D point) {

    }

    @Override
    public void onStopMove(Point2D point) {

    }

}
