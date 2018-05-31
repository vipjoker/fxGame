package mygame.editor.actions;


import javafx.geometry.Point2D;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;

/**
 * Created by oleh on 4/3/17.
 */
public class CreateJointAction extends Action implements CanvasRenderer.OnCanvasDragListener{
    public CreateJointAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);
    }

    @Override
    public void init() {
        mRenderer.setOnCanvasDragListener(this);
    }



    @Override
    public void finishDrawing() {

    }

    @Override
    public void onStartMove(Point2D point) {

    }

    @Override
    public void onDrag(Point2D point) {

    }

    @Override
    public void onStopMove(Point2D point) {

    }
}
