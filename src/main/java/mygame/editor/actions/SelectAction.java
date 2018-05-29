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
public class SelectAction extends Action{

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
        mRenderer.setOnCanvasClickListener(this::onCanvasClicked);
        mRenderer.setmOnCanvasDragListener(this::onCanvasDragged);

    }

    private void onCanvasDragged(double deltaX, double deltaY, boolean isEnd) {
        if(isEnd){

            selected = null;
            return ;
        }

        if(selected!=null){
            double newX = selected.getX() - deltaX;
            double newY = selected.getY() + deltaY;
            selected.setX(newX);
            selected.setY(newY);
        }
    }

    private void onCanvasClicked(Point2D point2D) {
        root.updateAll(c->c.setActive(false));
        selected = root.getSelected(point2D);
        if(selected!= null){


            mController.setNode(selected, mRenderer::update);


            mRenderer.update();
        }
        System.out.println(point2D + " " +selected);

//        List<CcNode> collect = mRenderer.getNodes().stream().filter(ccNode -> ccNode.active).collect(Collectors.toList());
//        if(collect.size() == 1){
//            CcNode ccNode = collect.get(0);
//
//        }
    }



    @Override
    public void mouseMoved(Point position) {

    }

    @Override
    public void mousePressed(Point position) {

    }

    @Override
    public void mouseReleased(Point position) {

    }

    @Override
    public void finishDrawing() {
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasClickListener(null);
        mRenderer.setmOnCanvasDragListener(null);
        mRepository.deleteAll();
        mRepository.save(root);
    }
}
