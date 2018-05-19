package mygame.editor.actions;

import com.badlogic.gdx.physics.box2d.BodyDef;
import javafx.geometry.Point2D;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Resources;
import mygame.editor.views.CcBodyNode;
import mygame.editor.views.CcSprite;

/**
 * Created by oleh on 18.05.18.
 */
public class CreateSpriteAction extends Action {

    public CreateSpriteAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer, repository);
    }

    @Override
    public void init() {
        mRenderer.setOnCanvasClickListener(this::onCanvasClick);
    }

    private void onCanvasClick(Point2D point2D) {

        CcSprite bodyNode = new CcSprite(Resources.no_image);
        bodyNode.x = point2D.getX();
        bodyNode.y = point2D.getY();
        mRenderer.addChild(bodyNode);
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
