package mygame.editor.actions;


import com.badlogic.gdx.physics.box2d.BodyDef;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcBodyNode;

/**
 * Created by oleh on 3/21/17.
 */
public class MoverAction extends Action {


    public MoverAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);
    }

    @Override
    public void init() {
        mRenderer.setOnCanvasClickListener(point2D -> {

        });
    }

    @Override
    public void mouseMoved(Point position) {

    }

    @Override
    public void mousePressed(Point position) {

    }

    @Override
    public void mouseReleased(Point position) {

    }

    @Override
    public void finishDrawing() {
        mRenderer.setOnCanvasClickListener(null);
    }
}
