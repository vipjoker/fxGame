package mygame.editor.actions;


import javafx.geometry.Point2D;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;

/**
 * Created by oleh on 4/3/17.
 */
public class CreateJointAction extends Action{
    public CreateJointAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);
    }

    @Override
    public void init() {
        mRenderer.setOnCanvasClickListener(new CanvasRenderer.OnCanvasClickListener() {
            @Override
            public void onClick(Point2D point2D) {
                System.out.println("Create joint " + point2D);
            }
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

    }
}
