package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
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
import mygame.editor.model.box2d.B2Fixture;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Constants;
import mygame.editor.views.*;

import java.util.ArrayList;
import java.util.List;

public class FixtureEditAction extends Action implements CanvasRenderer.OnCanvasDragListener,KeyListener {



    private final List<CcFixtureNode> selected = new ArrayList<>();
    private final List<CcVertex> editablePoints = new ArrayList<>();
    private final List<CcVertex> selectedPoints = new ArrayList<>();

    private final List<CcEditBodyNode> editBodyNodes = new ArrayList<>();


    public FixtureEditAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer, repository);
    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        App.instance.addKeyListener(this);
        mode = Constants.PARAM_MOVE;
        for (B2Body body : mRepository.getBodies()) {
            CcEditBodyNode node = new CcEditBodyNode(body);
            mRenderer.addChild(node);
            editBodyNodes.add(node);
        }

        mRenderer.setOnCanvasDragListener(this);

    }

    @Override
    public void finishDrawing() {
        updateFixtures();
        mRenderer.getNodes().clear();
        mRenderer.setOnCanvasDragListener(null);
        App.instance.removeKeyListener(this);
        selected.clear();
    }

    private void updateFixtures() {
        for (CcEditBodyNode editBodyNode : editBodyNodes) {
            for (CcFixtureNode ccFixtureNode : editBodyNode.getFixtureNodes()) {
                ccFixtureNode.update();
            }
        }
    }

    @Override
    public void onStartMove(Point2D point) {

//        for (CcVertex editablePoint : editablePoints) {
//            editablePoint.move(point);
//        }

        switch (mode){
            case Constants.PARAM_MOVE:
                handleSelect(point);
                break;
            case Constants.PARAM_ADD_POINT:
                if(selected.size() == 1){
                    final CcFixtureNode ccFixtureNode = selected.get(0);
                    ccFixtureNode.addPoint(point);
                }else{

                   // mode = Constants.SELECT;
                }

        }




    }

    private void handleSelect(Point2D point) {
        for (CcVertex editablePoint : editablePoints) {
            Point2D parentPoint = editablePoint.convertToLocalSpace(point);

            if (editablePoint.contains(parentPoint)) {
                selectedPoints.add(editablePoint);
                editablePoint.setActive(true);
                return;
            }

        }
        selectedPoints.clear();

        if(!App.buttons.contains(KeyCode.SHIFT)){
            selected.clear();
        }
        for (CcVertex editablePoint : editablePoints) {
          editablePoint.removeSelf();
        }
        editablePoints.clear();
        for (CcEditBodyNode parent :editBodyNodes) {



            for (CcFixtureNode child : parent.getFixtureNodes()) {
                final Point2D convertToLocalSpace = child.convertToLocalSpace(point);

                if(child.contains(convertToLocalSpace)){
                    selected.add(child);
                    for (Vector2 vector2 : child.getPoints()) {
                        CcVertex ccVertex = new CcVertex(vector2);
                        editablePoints.add(ccVertex);
                        child.addChild(ccVertex);
                    }
                }
                child.setActive(selected.contains(child));
            }



        }
    }

    @Override
    public void onDrag(Point2D point) {

        switch (mode){
            case Constants.PARAM_MOVE:
                for (CcNode s : selected) {
                    double newX = s.getX().doubleValue() - point.getX();
                    double newY = s.getY().doubleValue() + point.getY();
                    s.setX(newX);
                    s.setY(newY);
                }
                break;

            case Constants.PARAM_EDIT_POINTS:
                for (CcVertex editablePoint : selectedPoints) {
                    editablePoint.move(point);
                }
                break;
        }





    }

    @Override
    public void onStopMove(Point2D point) {
        updateFixtures();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()){
            case T:
                mode =Constants.PARAM_MOVE;
                break;

            case V:
                mode = Constants.PARAM_EDIT_POINTS;
                break;
            case A:
                mode = Constants.PARAM_ADD_POINT;
                break;

        }
    }
}
