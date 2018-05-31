package mygame.editor.actions;


import com.badlogic.gdx.physics.box2d.BodyDef;
import javafx.geometry.Point2D;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Resources;
import mygame.editor.views.CcBodyNode;
import mygame.editor.views.CcSprite;

public class CreateBodyAction extends Action implements CanvasRenderer.OnCanvasDragListener{

    public CreateBodyAction(CanvasRenderer renderer, NodeRepository repository) {
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

        BodyDef def = new BodyDef();
        def.position.x = (float) point.getX()/32;
        def.position.y = (float) point.getY()/32;
        CcBodyNode bodyNode= new CcBodyNode(def);
        mRenderer.addChild(bodyNode);
        mRenderer.update();
    }

    @Override
    public void onDrag(Point2D point) {

    }

    @Override
    public void onStopMove(Point2D point) {

    }
}
