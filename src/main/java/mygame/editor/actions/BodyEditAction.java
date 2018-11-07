package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import mygame.editor.App;
import mygame.editor.component.SelectComponent;
import mygame.editor.controlers.InfoController;
import mygame.editor.interfaces.Editable;
import mygame.editor.interfaces.KeyListener;
import mygame.editor.model.Point;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcBodyNode;
import mygame.editor.views.CcEditBodyNode;
import mygame.editor.views.CcNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/27/17.
 */
public class BodyEditAction extends Action implements CanvasRenderer.OnCanvasDragListener, KeyListener {

    private InfoController mController;

    enum Mode {
      MOVE, ROTATE
    }

    private final List<CcNode> selected = new ArrayList<>();
    private Mode mode = Mode.MOVE;

    public BodyEditAction(CanvasRenderer renderer, NodeRepository repository, InfoController controller) {
        super(renderer, repository);
        this.mController = controller;
    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        App.instance.addKeyListener(this);
        for (B2Body body : mRepository.getBodies()) {
            mRenderer.addChild(new CcEditBodyNode(body));
        }
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
        if (!App.buttons.contains(KeyCode.SHIFT)) {
            selected.clear();
        }

        for (CcNode node : mRenderer.getNodes()) {
            final Point2D point2D = node.convertToLocalSpace(point);
            if (node.contains(point2D) && !selected.contains(node)) {
                selected.add(node);
                break;
            }
        }

        for (CcNode node : mRenderer.getNodes()) {
            node.setActive(selected.contains(node));
        }
    }

    @Override
    public void onDrag(Point2D point) {


        if (mode == Mode.ROTATE) {

            for (CcNode ccNode : selected) {
                ccNode.setAngle(ccNode.getAngle() - point.getX() - point.getY());
            }
        } else {
            for (CcNode s : selected) {
                double newX = s.getX() - point.getX();
                double newY = s.getY() + point.getY();
                s.setX(newX);
                s.setY(newY);
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
