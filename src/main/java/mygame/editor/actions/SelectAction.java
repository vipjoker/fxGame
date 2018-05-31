package mygame.editor.actions;

import javafx.geometry.Point2D;
import mygame.editor.component.SelectComponent;
import mygame.editor.controlers.InfoController;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcNode;

/**
 * Created by oleh on 3/27/17.
 */
public class SelectAction extends Action implements CanvasRenderer.OnCanvasDragListener{

    private InfoController mController;
    private CcNode root;
    private CcNode selected;
    public SelectAction(CanvasRenderer renderer, NodeRepository repository, InfoController controller) {
        super(renderer,repository);
        this.mController = controller;
    }

    @Override
    public void init() {
        root = mRepository.getRootNode();
        mRenderer.getNodes().clear();


        for (CcNode ccNode : root.getChildren()) {

            ccNode.addComponent(new SelectComponent());
        }

        mRenderer.addChild(root);
        mRenderer.update();
        mRenderer.setOnCanvasDragListener(this);

    }

    @Override
    public void finishDrawing() {
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasDragListener(null);
        mRepository.deleteAll();
        mRepository.save(root);
    }

    @Override
    public void onStartMove(Point2D point) {

        root.updateAll(c->c.setActive(false));
        selected = root.getSelected(point);
        if(selected!= null){


            mController.setNode(selected, mRenderer::update);


            mRenderer.update();
        }




    }

    @Override
    public void onDrag(Point2D point) {
        if(selected!=null){
            double newX = selected.getX() - point.getX();
            double newY = selected.getY() + point.getY();
            selected.setX(newX);
            selected.setY(newY);
        }
    }

    @Override
    public void onStopMove(Point2D point) {
        selected = null;
    }
}
