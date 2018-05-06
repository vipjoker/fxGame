package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import mygame.editor.TimerCounter;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.ui.ChainFixture;
import mygame.editor.views.CcBodyNode;

public class Box2dAction extends Action implements TimerCounter.FrameRateCallback{
    private TimerCounter counter;
    private World world;

    public Box2dAction(CanvasRenderer renderer) {
        super(renderer);


    }

    @Override
    public void init() {
        world = new World(new Vector2(0,-10),true);
        createBody();
        for(int i = 0 ; i < 10; i++){
            createSquare(i);
        }
        createGround();
        counter = new TimerCounter(this);
        counter.start();
    }

    private void createSquare(int x) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x,5);

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);

        CcBodyNode bodyNode = new CcBodyNode(body);


        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.density = 1;
        fixtureDef2.restitution = 0.5f;
        fixtureDef2.friction = 0.5f;


        CircleShape circleShape = new CircleShape();
        circleShape.setPosition(new Vector2(0,0));
        circleShape.setRadius(1);


        fixtureDef2.shape = circleShape;
        bodyNode.addFixture(fixtureDef2);

        mRenderer.addChild(bodyNode);



    }

    private void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(1,10);

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);
        CcBodyNode bodyNode = new CcBodyNode(body);
        mRenderer.addChild(bodyNode);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(new Vector2[]{
            new Vector2(-.5f,-.2f),
            new Vector2(.5f,-.2f),
            new Vector2(0,0.2f)
        });

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.shape = polygonShape;
        bodyNode.addFixture(fixtureDef);
    }


    private void createGround(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0,-1);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body groundBody = world.createBody(bodyDef);

        CcBodyNode bodyNode1 = new CcBodyNode(groundBody);
        mRenderer.addChild(bodyNode1);

        ChainShape chainFixture = new ChainShape();
        chainFixture.createChain(new Vector2[]{
                        new Vector2(-15, 7),
                        new Vector2(-10, 5),
                        new Vector2(10, 0),
                        new Vector2(15, 5),

                }
        );

        FixtureDef fixtureDef3 = new FixtureDef();
        fixtureDef3.shape = chainFixture;
        bodyNode1.addFixture(fixtureDef3);
    }

    @Override
    public void update(long delta) {

        world.step(1f/60f,8,3);

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
        world = null;
        counter.stop();
        mRenderer.getNodes().clear();
    }
}
