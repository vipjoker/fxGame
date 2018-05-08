package mygame.editor.actions;

import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;

/**
 * Created by oleh on 3/27/17.
 */
public class SelectAction extends Action{


    public SelectAction(CanvasRenderer renderer) {
        super(renderer);
    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        mRenderer.update();
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
