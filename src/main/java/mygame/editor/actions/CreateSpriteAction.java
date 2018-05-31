package mygame.editor.actions;

import com.badlogic.gdx.physics.box2d.BodyDef;
import javafx.geometry.Point2D;
import mygame.editor.component.SpriteComponent;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Resources;
import mygame.editor.views.CcBodyNode;
import mygame.editor.views.CcNode;
import mygame.editor.views.CcSprite;

/**
 * Created by oleh on 18.05.18.
 */
public class CreateSpriteAction extends Action implements CanvasRenderer.OnCanvasDragListener {

    public CreateSpriteAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer, repository);
    }
    private CcNode rootNode;
    @Override
    public void init() {
        mRenderer.setOnCanvasDragListener(this);

        rootNode  = mRepository.getRootNode();
        if(rootNode == null){
            rootNode = new CcNode();
            rootNode.id = 1;

        }
        mRenderer.addChild(rootNode);

        mRenderer.update();
    }



    private int getNextId(CcNode node){
        int id = 1;
        while(node.findViewById(id) != null){
            id++;
        }
        return id;
    }

    @Override
    public void finishDrawing() {
        mRepository.deleteAll();
        mRepository.save(rootNode);
    }

    @Override
    public void onStartMove(Point2D point) {
        CcNode bodyNode = new CcNode();

//        CcSprite bodyNode = new CcSprite(Resources.no_image);
        SpriteComponent component = new SpriteComponent(Resources.NO_IMAGE);

        bodyNode.addComponent(component);
        bodyNode.id = getNextId(rootNode);
        bodyNode.setX(point.getX());
        bodyNode.setY(point.getY());
        bodyNode.setWidth(100);
        bodyNode.setHeight(100);
        rootNode.addChild(bodyNode);
        mRenderer.update();
    }

    @Override
    public void onDrag(Point2D point) {

    }

    @Override
    public void onStopMove(Point2D point) {

    }
}
