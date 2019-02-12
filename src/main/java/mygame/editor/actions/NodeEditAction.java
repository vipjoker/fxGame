package mygame.editor.actions;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import mygame.editor.App;
import mygame.editor.model.Node;
import mygame.editor.model.Physics;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeModel;
import mygame.editor.util.Callback;
import mygame.editor.util.Constants;
import mygame.editor.util.ImageUtil;
import mygame.editor.util.TraverseUtil;
import mygame.editor.views.CcEditBodyNode;
import mygame.editor.views.NodeView;
import mygame.editor.views.SpriteView;

import java.util.HashMap;
import java.util.Map;

public class NodeEditAction extends Action implements CanvasRenderer.OnCanvasDragListener {

    public NodeEditAction(CanvasRenderer renderer, NodeModel repository) {
        super(renderer, repository);
    }

    public final Map<Node, NodeView> nodesMap = new HashMap<>();

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        nodesMap.clear();
//       lorem ipsum CindySinn

        mRenderer.setOnCanvasDragListener(this);


        for (Node node : mRepository.getNodes()) {
            Image image = ImageUtil.getImage("earth.jpg");
            SpriteView nodeView = new SpriteView(image);

            listenForChanges(nodeView, node);

            nodesMap.put(node, nodeView);
            mRenderer.getNodes().add(nodeView);

        }
        mRenderer.update();
        mRepository.listenForNodes(this::onNodesChanged);
        App.instance.selected.addListener(this::onSelectChanged);
        System.out.println("NODE EDIT ACTION");
    }

    private void onSelectChanged(ListChangeListener.Change<? extends Node> change) {
        final ObservableList<? extends Node> list = change.getList();
//        for (Node ccNode : this.nodes) {
//
//            traverse(ccNode, n -> {
//                final boolean contains = list.contains(n);
//
//                n.setActive(contains);
//            });


//        }
    }

    private void traverse(Node node, Callback<Node> action) {
        action.call(node);
        for (Node ccNode : node.getChildren()) {
            traverse(ccNode, action);
        }
    }


    private void onNodesChanged(ListChangeListener.Change<? extends Node> change) {
        mRenderer.getNodes().clear();
        nodesMap.clear();
        for (Node node : change.getList()) {
            Physics physics = new Physics();
            physics.setShape("circle");
            physics.setDensity(32);
            node.setPhysics(physics);
            Image image = ImageUtil.getImage("earth.jpg");
            SpriteView nodeView = new SpriteView(image);
            nodesMap.put(node, nodeView);
            listenForChanges(nodeView, node);
            mRenderer.getNodes().add(nodeView);
        }
    }


    private void listenForChanges(NodeView nodeView, Node node) {
        nodeView.setWidth(node.getWidth().doubleValue());
        nodeView.setHeight(node.getHeight().doubleValue());
        nodeView.setX(node.getPosition().getX().doubleValue());
        nodeView.setY(node.getPosition().getY().doubleValue());
        nodeView.setAngle(node.getAngle().doubleValue());

        nodeView.getX().addListener((observable, oldValue, newValue)
                -> node.setPosition(newValue.doubleValue(), node.getPosition().getY().doubleValue()));

        nodeView.getY().addListener((observable, oldValue, newValue)
                -> node.setPosition(node.getPosition().getX().doubleValue(), newValue.doubleValue()));

        nodeView.getWidth().addListener((observable, oldValue, newValue)
                -> node.setWidth(newValue.doubleValue()));

        nodeView.getHeight().addListener((observable, oldValue, newValue)
                -> node.setHeight(newValue.doubleValue()));

        nodeView.getAngle().addListener((observable, oldValue, newValue)
                -> node.setAngle(newValue.doubleValue()));




        nodeView.setClickListener(view -> {
            App.instance.selected.clear();
            App.instance.selected.add(node);

            TraverseUtil.action(mRenderer.getNodes(), nv -> {
                nv.setActive(false);
            });
            view.setActive(!view.active);
        });


        node.getPosition().addListener((observable, oldValue, newValue) -> {
            nodeView.setX(newValue.getX().doubleValue());
            nodeView.setY(newValue.getY().doubleValue());
        });

        node.getAngle().addListener((observable, oldValue, newValue) -> nodeView.setAngle(newValue.floatValue()));

        node.getWidth().addListener((observable, oldValue, newValue) -> nodeView.setWidth(newValue.doubleValue()));
        node.getHeight().addListener((observable, oldValue, newValue) -> nodeView.setHeight(newValue.doubleValue()));
        node.getAnchor().addListener(new ChangeListener<Point>() {
            @Override
            public void changed(ObservableValue<? extends Point> observable, Point oldValue, Point newValue) {
                nodeView.setAnchorX(newValue.getX().doubleValue());
                nodeView.setAnchorY(newValue.getY().doubleValue());
            }
        });
        if (node.getPhysics() != null) {
            switch (node.getPhysics().getShape().get()) {
                case Physics.CIRCLE:
                    nodeView.setEditBody(CcEditBodyNode.createCircle(node.getPhysics().getRadius().doubleValue()));
                    break;
                case Physics.CHAIN:
                    nodeView.setEditBody(CcEditBodyNode.createChain(node.getPhysics().getPoints()));
                    break;
                case Physics.RECT:
                    nodeView.setEditBody(CcEditBodyNode.createRect(node.getPhysics().getWidth().doubleValue(), node.getPhysics().getHeight().doubleValue()));
                    break;
            }

            node.getPhysics().addListener(new ChangeListener<Physics>() {
                @Override
                public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                    System.out.println("Changed");
                    switch (node.getPhysics().getShape().get()) {
                        case Physics.CIRCLE:
                            nodeView.setEditBody(CcEditBodyNode.createCircle(node.getPhysics().getRadius().doubleValue()));
                            break;
                        case Physics.CHAIN:
                            nodeView.setEditBody(CcEditBodyNode.createChain(node.getPhysics().getPoints()));
                            break;
                        case Physics.RECT:
                            nodeView.setEditBody(CcEditBodyNode.createRect(node.getPhysics().getWidth().doubleValue(), node.getPhysics().getHeight().doubleValue()));
                            break;
                    }
                }
            });


        }


    }

    @Override
    public void finishDrawing() {
//        for (Property key : propertyMap.keySet()) {
//            Property value = propertyMap.get(key);
//            key.unbindBidirectional(value);
//            value.unbindBidirectional(key);
//        }

        for (Node node : nodesMap.keySet()) {

        }
        mRenderer.getNodes().clear();
        nodesMap.clear();
    }

    @Override
    public void onStartMove(Point2D point) {
        App.instance.selected.clear();
        for (NodeView node : mRenderer.getNodes())
            traverse(node, n -> n.setActive(false));

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
        for (Node ccNode : App.instance.selected) {
            switch (mode) {
                case Constants.PARAM_MOVE:
                    nodesMap.get(ccNode).move(point);
//                    ccNode.move(point);
                    break;
                case Constants.PARAM_ROTATE:
                    nodesMap.get(ccNode).rotate(point);
                    break;
            }

        }
    }

    @Override
    public void onStopMove(Point2D point) {

    }
}
