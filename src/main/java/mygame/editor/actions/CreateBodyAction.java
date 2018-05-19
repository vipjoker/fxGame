package mygame.editor.actions;


import com.badlogic.gdx.physics.box2d.BodyDef;
import javafx.geometry.Point2D;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Resources;
import mygame.editor.views.CcBodyNode;
import mygame.editor.views.CcSprite;

public class CreateBodyAction extends Action {

    public CreateBodyAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);
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
    public void init() {
        mRenderer.setOnCanvasClickListener(this::onCanvasClicked);
    }

    private void onCanvasClicked(Point2D point2D){


        BodyDef def = new BodyDef();
        def.position.x = (float) point2D.getX()/32;
        def.position.y = (float) point2D.getY()/32;
        CcBodyNode bodyNode= new CcBodyNode(def);
        mRenderer.addChild(bodyNode);
        mRenderer.update();

    }

    @Override
    public void finishDrawing() {

    }
}
