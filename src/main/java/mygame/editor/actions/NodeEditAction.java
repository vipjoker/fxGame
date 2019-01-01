package mygame.editor.actions;

import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import mygame.editor.App;
import mygame.editor.model.Node;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeModel;
import mygame.editor.util.Callback;
import mygame.editor.util.Constants;
import mygame.editor.views.NodeView;

import java.util.List;

public class NodeEditAction extends Action implements CanvasRenderer.OnCanvasDragListener {

    public NodeEditAction(CanvasRenderer renderer, NodeModel repository) {
        super(renderer, repository);
    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasDragListener(this);
        final List<Node> nodes = App.instance.repository.getNodes();

        Node n = new Node();
        n.setWidth(100);
        n.setHeight(100);
        n.getPosition().set(10,10);
        mRepository.save(n);
        for (Node node : nodes) {
            NodeView nodeView = new NodeView();
            node.getWidth().bindBidirectional(nodeView.getWidth());
            node.getHeight().bindBidirectional(nodeView.getHeight());
            node.getPosition().getX().bindBidirectional(nodeView.getX());
            node.getPosition().getY().bindBidirectional(nodeView.getY());
        mRenderer.getNodes().add(nodeView);

        }
        mRenderer.update();
        mRepository.listenForNodes(this::onNodesChanged);
        System.out.println("NODE EDIT ACTION");
    }

    private void onNodesChanged(ListChangeListener.Change<? extends Node> change) {
        mRenderer.getNodes().clear();
        for (Node node : change.getList()) {
            NodeView nodeView = new NodeView();

            node.getWidth().bindBidirectional(nodeView.getWidth());
            node.getHeight().bindBidirectional(nodeView.getHeight());
            node.getPosition().getX().bindBidirectional(nodeView.getX());
            node.getPosition().getY().bindBidirectional(nodeView.getY());
            mRenderer.getNodes().add(nodeView);

        }
    }

    @Override
    public void finishDrawing() {

    }
    @Override
    public void onStartMove(Point2D point) {
        App.instance.selected.clear();
        for (NodeView ccNode : mRenderer.getNodes()) {
                traverse(ccNode, n -> {
                    boolean contains = false;
                    if(n.getParent()!= null){

                        Point2D point2D = n.getParent().convertToLocalSpace(point);
                        contains = n.contains(point2D);
                    }else{
                        contains = n.contains(point);
                    }

                    if (contains) {
                        App.instance.selected.clear();
                        App.instance.selected.add(n);

                    }
                });



        }
    }

    public void traverse(NodeView node, Callback<NodeView> action){
        action.call(node);
        for (NodeView child : node.getChildren()) {

                traverse(child, action);

        }
    }

    @Override
    public void onDrag(Point2D point) {
        System.out.println("onDrag size " + App.instance.selected.size());
        for (NodeView ccNode : App.instance.selected) {
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
