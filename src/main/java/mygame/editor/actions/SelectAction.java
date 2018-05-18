package mygame.editor.actions;

import javafx.geometry.Point2D;
import mygame.editor.controlers.InfoController;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oleh on 3/27/17.
 */
public class SelectAction extends Action{

    private InfoController mController;
    public SelectAction(CanvasRenderer renderer, NodeRepository repository, InfoController controller) {
        super(renderer,repository);
        this.mController = controller;
    }

    @Override
    public void init() {

        //mRenderer.getNodes().clear();
        mRenderer.update();

        mRenderer.setOnCanvasClickListener(this::onCanvasClicked);

    }

    private void onCanvasClicked(Point2D point2D) {
        List<CcNode> collect = mRenderer.getNodes().stream().filter(ccNode -> ccNode.active).collect(Collectors.toList());
        if(collect.size() == 1){
            CcNode ccNode = collect.get(0);
            mController.setNode(ccNode, ()->{
                ccNode.updateBoundingBox();
                mRenderer.update();
            });
        }
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

    }
}
