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
public class FixtureEditAction extends Action implements CanvasRenderer.OnCanvasDragListener,KeyListener {

    private InfoController mController;

    enum Mode{
        SELECT,MOVE,ROTATE
    }
    private final List<CcNode> selected = new ArrayList<>();
    private Mode mode = Mode.SELECT;

    public FixtureEditAction(CanvasRenderer renderer, NodeRepository repository, InfoController controller) {
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

//        mRepository.save(root);
    }

    @Override
    public void onStartMove(Point2D point) {


        switch (mode){
            case SELECT:
                handleSelect(point);
                break;
            case MOVE:

                break;
        }




    }

    private void handleSelect(Point2D point) {
        if(!App.buttons.contains(KeyCode.SHIFT)){
            selected.clear();
        }
        for (CcNode parent :mRenderer.getNodes()) {
            final Point2D point2D = parent.convertToLocalSpace(point);

            for (CcNode child : parent.getChildren()) {
                Point2D local = child.convertToLocalSpace(point2D);
                if(child.contains(local)){
                    selected.add(child);

                }
                child.setActive(selected.contains(child));
            }



        }
    }

    @Override
    public void onDrag(Point2D point) {

        switch (mode){
            case MOVE:
                for (CcNode s : selected) {
                    double newX = s.getX() - point.getX();
                    double newY = s.getY() + point.getY();
                    s.setX(newX);
                    s.setY(newY);
                }
                break;
            case ROTATE:
                for (CcNode ccNode : selected) {
                    ccNode.setAngle(ccNode.getAngle() - point.getX() - point.getY());
                }
        }





    }

    @Override
    public void onStopMove(Point2D point) {
        // selected.clear();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()){
            case T:
                mode = Mode.MOVE;
                break;
            case R:
                mode = Mode.ROTATE;
                break;
            case Q:
                mode = Mode.SELECT;

        }
    }
}
