package mygame.editor.actions;

import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import mygame.editor.App;
import mygame.editor.model.Node;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeModel;
import mygame.editor.util.Callback;
import mygame.editor.util.Constants;
import mygame.editor.util.ImageUtil;
import mygame.editor.views.NodeView;
import mygame.editor.views.SpriteView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeEditAction extends Action implements CanvasRenderer.OnCanvasDragListener {

    public NodeEditAction(CanvasRenderer renderer, NodeModel repository) {
        super(renderer, repository);
    }

    public final Map<Property, Property> propertyMap = new HashMap<>();

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasDragListener(this);
        final List<Node> nodes = App.instance.repository.getNodes();

        Node n = new Node();
        n.setWidth(100);
        n.setHeight(100);
        n.setPosition(100, 50);
        mRepository.save(n);


        Node n2 = new Node();
        n2.setHeight(500);
        n2.setWidth(200);
        n2.setPosition(200,200);
        mRepository.save(n2);

        Node n3 = new Node();
        n3.setHeight(400);
        n3.setWidth(400);
        n3.setPosition(0,400);
        mRepository.save(n3);


        for (Node node : nodes) {
            Image image = ImageUtil.getImage("earth.jpg");
            SpriteView nodeView = new SpriteView(image);
            nodeView.getWidth().bindBidirectional(node.getWidth());
            nodeView.getHeight().bindBidirectional(node.getHeight());
            nodeView.getX().bindBidirectional(node.getPosition().getX());
            nodeView.getY().bindBidirectional(node.getPosition().getY());

            propertyMap.put(nodeView.getWidth(),node.getWidth());
            propertyMap.put(nodeView.getHeight(),node.getHeight());
            propertyMap.put(nodeView.getX(),node.getPosition().getX());
            propertyMap.put(nodeView.getY(),node.getPosition().getY());

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


            propertyMap.put(nodeView.getWidth(),node.getWidth());
            propertyMap.put(nodeView.getHeight(),node.getHeight());
            propertyMap.put(nodeView.getX(),node.getPosition().getX());
            propertyMap.put(nodeView.getY(),node.getPosition().getY());
            mRenderer.getNodes().add(nodeView);

        }
    }

    @Override
    public void finishDrawing() {
        for (Property key : propertyMap.keySet()) {
            Property value = propertyMap.get(key);
            key.unbindBidirectional(value);
            value.unbindBidirectional(key);
        }
        mRenderer.getNodes().clear();
        propertyMap.clear();
    }

    @Override
    public void onStartMove(Point2D point) {
        App.instance.selected.clear();
        for (NodeView ccNode : mRenderer.getNodes()) {
            traverse(ccNode, n -> {
                boolean contains = false;
                if (n.getParent() != null) {

                    Point2D point2D = n.getParent().convertToLocalSpace(point);
                    contains = n.contains(point2D);
                } else {
                    contains = n.contains(point);
                }

                if (contains) {
                    App.instance.selected.clear();
                    App.instance.selected.add(n);

                }
            });


        }
    }

    public void traverse(NodeView node, Callback<NodeView> action) {
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
