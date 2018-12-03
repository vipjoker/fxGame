package mygame.editor.actions;

import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import mygame.editor.App;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Callback;
import mygame.editor.util.Constants;
import mygame.editor.views.CcNode;

import java.util.List;

public class NodeEditAction extends Action implements CanvasRenderer.OnCanvasDragListener {

    public NodeEditAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer, repository);
    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasDragListener(this);
        final List<CcNode> nodes = App.instance.repository.getNodes();
        mRenderer.getNodes().addAll(nodes);
        mRepository.listenForNodes(this::onNodesChanged);
        System.out.println("NODE EDIT ACTION");
    }

    private void onNodesChanged(ListChangeListener.Change<? extends CcNode> change) {
        mRenderer.getNodes().clear();

        mRenderer.getNodes().addAll(change.getList());
    }

    @Override
    public void finishDrawing() {

    }

    @Override
    public void onStartMove(Point2D point) {
        App.instance.selected.clear();
        for (CcNode ccNode : mRenderer.getNodes()) {

            traverse(ccNode,n->{
                final boolean contains = n.contains(point);
                if (contains) {
                    App.instance.selected.add(n);

                }
            });


        }
    }

    public void traverse(CcNode node, Callback<CcNode> action){
        action.call(node);
        for (CcNode child : node.getChildren()) {
            traverse(child,action);
        }
    }

    @Override
    public void onDrag(Point2D point) {
        System.out.println("onDrag size " + App.instance.selected.size());
        for (CcNode ccNode : App.instance.selected) {
            switch (mode) {
                case Constants.PARAM_MOVE:
                    ccNode.move(point);
                    break;
                case Constants.PARAM_ROTATE:
                    ccNode.rotate(point);
                    break;
            }

        }
    }

    @Override
    public void onStopMove(Point2D point) {

    }
}
