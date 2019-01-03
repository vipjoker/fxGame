package mygame.editor.actions;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
//amandani
public class NodeEditAction extends Action implements CanvasRenderer.OnCanvasDragListener {

    public NodeEditAction(CanvasRenderer renderer, NodeModel repository) {
        super(renderer, repository);
    }

    public final Map<NodeView, Node> propertyMap = new HashMap<>();

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasDragListener(this);



        for (Node node : mRepository.getNodes()) {
            Image image = ImageUtil.getImage("earth.jpg");
            SpriteView nodeView = new SpriteView(image);
            nodeView.setWidth(node.getWidth().doubleValue());
            nodeView.setHeight(node.getHeight().doubleValue());
            nodeView.setX(node.getPosition().getX().doubleValue());
            nodeView.setY(node.getPosition().getY().doubleValue());


            nodeView.getX().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                    node.setPosition(newValue.doubleValue(),node.getPosition().getY().doubleValue());
                }
            });

            nodeView.getY().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    node.setPosition(node.getPosition().getX().doubleValue(),newValue.doubleValue());
                }
            });


            nodeView.getWidth().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    node.setWidth(newValue.doubleValue());
                }
            });

            nodeView.getHeight().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    node.setHeight(newValue.doubleValue());
                }
            });
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
            mRenderer.getNodes().add(nodeView);

        }
    }

    @Override
    public void finishDrawing() {
//        for (Property key : propertyMap.keySet()) {
//            Property value = propertyMap.get(key);
//            key.unbindBidirectional(value);
//            value.unbindBidirectional(key);
//        }
        mRenderer.getNodes().clear();
//        propertyMap.clear();
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
