package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcNode;
import mygame.editor.views.CcVertex;

import java.util.ArrayList;
import java.util.List;

public class EditPointsAction extends Action implements CanvasRenderer.OnCanvasDragListener{

    private final List<Vector2> points = new ArrayList<>();
    private final List<CcNode> selected = new ArrayList<>();
    public EditPointsAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer, repository);
    }

    @Override
    public void init() {
        mRenderer.setOnCanvasDragListener(this);
        points.add(new Vector2(50,50));
        points.add(new Vector2(0,0));
        points.add(new Vector2(-50,20));
        points.add(new Vector2(100,-100));

        for (Vector2 point : points) {
            mRenderer.addChild(new CcVertex(point));
        }

    }



    @Override
    public void finishDrawing() {
        mRenderer.setOnCanvasDragListener(null);
        mRenderer.getNodes().clear();
    }

    @Override
    public void onStartMove(Point2D point) {
        selected.clear();
        for (CcNode ccNode : mRenderer.getNodes()) {
            if (ccNode.contains(point)) {
                selected.add(ccNode);
            }
            ccNode.setActive(selected.contains(ccNode));
        }


    }

    @Override
    public void onDrag(Point2D point) {
        for (CcNode ccNode : selected) {
            ccNode.move(point);
        }

    }

    @Override
    public void onStopMove(Point2D point) {

    }
}
