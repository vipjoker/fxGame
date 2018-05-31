package mygame.editor.actions;

import javafx.geometry.Point2D;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;

/**
 * Created by oleh on 3/21/17.
 */
public class RotateAction extends Action implements CanvasRenderer.OnCanvasDragListener {

    public RotateAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);
    }

    @Override
    public void init() {

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

    @Override
    public void finishDrawing() {

    }
}
