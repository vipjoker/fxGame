package mygame.editor.actions;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import mygame.editor.App;
import mygame.editor.controlers.InfoController;
import mygame.editor.interfaces.KeyListener;
import mygame.editor.model.Node;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeModel;
import mygame.editor.views.CcEditBodyNode;
import mygame.editor.views.NodeView;

/**
 * Created by oleh on 3/27/17.
 */
public class BodyEditAction extends Action implements CanvasRenderer.OnCanvasDragListener, KeyListener {

    private InfoController mController;

    enum Mode {
      MOVE, ROTATE
    }

    private Mode mode = Mode.MOVE;

    public BodyEditAction(CanvasRenderer renderer, NodeModel repository, InfoController controller) {
        super(renderer, repository);
        this.mController = controller;
    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        App.instance.addKeyListener(this);
//        for (B2Body body : mRepository.getBodies()) {
//            mRenderer.addChild(new CcEditBodyNode(body));
//        }
        mRenderer.update();
        mRenderer.setOnCanvasDragListener(this);
    }

    @Override
    public void finishDrawing() {
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasDragListener(null);
        App.instance.removeKeyListener(this);
    }

    @Override
    public void onStartMove(Point2D point) {
        handleSelect(point);

    }

    private void handleSelect(Point2D point) {

        final ObservableList<Node> selected = App.instance.selected;
        if (!App.buttons.contains(KeyCode.SHIFT)) {
            selected.clear();
        }

        for (NodeView node : mRenderer.getNodes()) {
            final Point2D point2D = node.convertToLocalSpace(point);
            if (node.contains(point2D) && !selected.contains(node)) {

//                selected.add(node);
                break;
            }
        }

        for (NodeView node : mRenderer.getNodes()) {
            node.setActive(selected.contains(node));
        }
    }

    @Override
    public void onDrag(Point2D point) {
        final ObservableList<Node> selected = App.instance.selected;

        if (mode == Mode.ROTATE) {

            for (Node ccNode : selected) {
                ccNode.setAngle(ccNode.getAngle().doubleValue() - point.getX() - point.getY());
            }
        } else {
            for (Node s : selected) {
                double newX = s.getPosition().getX().doubleValue() - point.getX();
                double newY = s.getPosition().getY().doubleValue() + point.getY();
                s.getPosition().set(newX,newY);

            }
        }
    }


    @Override
    public void onStopMove(Point2D point) {
        // selected.clear();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case E:
                mode = Mode.MOVE;
                break;
            case R:
                mode = Mode.ROTATE;
                break;

        }
    }
}
