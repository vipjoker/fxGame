package mygame.editor.actions;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import javafx.geometry.Point2D;
import mygame.editor.component.PhysicsComponent;
import mygame.editor.mapper.CcNodeUtil;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Resources;
import mygame.editor.views.CcBodyNode;
import mygame.editor.views.CcCircle;
import mygame.editor.views.CcNode;
import mygame.editor.views.CcSprite;

public class CreateBodyAction extends Action implements CanvasRenderer.OnCanvasDragListener{

    private World world;
    public CreateBodyAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);
    }


    @Override
    public void init() {
        world = new World(new Vector2(10, 10), false);
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
        def.angle = (float) 45 * MathUtils.degRad;
        CcNode bodyNode= new CcNode();
        PhysicsComponent physicsComponent = new PhysicsComponent(def);

//        CcCircle circle = new CcCircle();
//        physicsComponent.addFixture(circle);
//
        bodyNode.addComponent(physicsComponent);
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
