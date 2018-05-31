package mygame.editor.actions;


import com.badlogic.gdx.physics.box2d.BodyDef;
import javafx.geometry.Point2D;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcBodyNode;

/**
 * Created by oleh on 3/21/17.
 */
public class MoverAction extends Action implements CanvasRenderer.OnCanvasDragListener{


    public MoverAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);
    }

    @Override
    public void init() {
        mRenderer.setOnCanvasDragListener(this);
    }


    @Override
    public void finishDrawing() {
        mRenderer.setOnCanvasDragListener(null);
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
