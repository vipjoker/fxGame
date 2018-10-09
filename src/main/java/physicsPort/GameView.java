package physicsPort;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import mygame.editor.util.FileUtil;
import physicsPort.body.Body;
import physicsPort.body.Box2dDebugDraw;
import physicsPort.body.WorldLoader;


import java.io.File;
import java.io.IOException;


public class GameView {

public static float mouseX, mouseY;
    public static Vector2 mousePVec;
    public static Object _navigator;
    public static boolean isMouseDown;
    public static MouseJoint mouseJoint;
    public static com.badlogic.gdx.physics.box2d.Body selectedBody;
    WorldLoader worldLoader;

    private boolean hasLoaded;
    private boolean paused;
    private Canvas canvas;
    private GraphicsContext context;
    private World world;

    public GameView(Canvas canvas, Object navigator) {
        this.canvas = canvas;
        this.context = canvas.getGraphicsContext2D();
        _navigator = navigator;

        this.worldLoader =new WorldLoader();

        this.hasLoaded = false;
        this.paused = false;
    }

/* SETUP */
    public void setup (String scenePath, boolean fromFile){

            if (fromFile){
                String s = FileUtil.loadString(new File(scenePath));
                init(s);

            }
            else {
                this.init(scenePath);
            }

    }

/* RESCALE */
    public void rescale (){
        // canvas.setAttribute('width', window.innerWidth);
        // canvas.setAttribute('height', window.innerHeight);
        // this.draw();
    };

/* INIT */
    public void init (String scene){
        // add event listeners
        this.canvas.setOnMousePressed(e->{
            isMouseDown = true;
            handleMouseMove(e);
        });

        this.canvas.setOnMouseDragged(this::handleMouseMove);
        this.canvas.setOnMouseReleased(e->{
            mouseX = -1;
            mouseY = -1;
            isMouseDown = false;
            handleMouseMove(e);
        });


        // create physics world
        this.world = new World(new Vector2(0, 10), true);



        // load scene
        try {
            this.worldLoader.loadJsonScene(scene, this.world);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.hasLoaded = true;
    }

/* DRAW */
    public void draw (){
        Box2dDebugDraw.draw(world,context);

        this.world.clearForces();

        for (var i = 0; i < this.worldLoader.gameObjects.length; i++){
            // handle sprite rotation and translation
            var gameObject = this.worldLoader.gameObjects[i];
            this.context.save();
            this.context.translate(gameObject.body.GetPosition().x * 30, gameObject.body.GetPosition().y * 30);
            this.context.rotate(gameObject.body.GetAngle());

            if (gameObject.spriteData.length > 0){
                var spriteData  = gameObject.spriteData,
                        sourceX     = spriteData[0],
                        sourceY     = spriteData[1],
                        sourceW     = spriteData[2],
                        sourceH     = spriteData[3],
                        imageW      = spriteData[4],
                        imageH      = spriteData[5];
                this.context.drawImage(gameObject.sprite, sourceX, sourceY, sourceW, sourceH, -imageW / 2, -imageH / 2, imageW, imageH);

            }
            else {
                var imageW = gameObject.sprite.width, imageH = gameObject.sprite.height;
                this.context.drawImage(gameObject.sprite, -imageW / 2, -imageH / 2, imageW, imageH);
            }
            this.context.restore();
        }
    };

/* UPDATE */
    public void update (){
        if(isMouseDown && (mouseJoint == null)) {
            com.badlogic.gdx.physics.box2d.Body body = getBodyAtMouse(world);
            if(body != null) {
                MouseJointDef md = new MouseJointDef();
                md.bodyA = this.world.GetGroundBody();
                md.bodyB = body;
                md.target.Set(mouseX, mouseY);
                md.collideConnected = true;
                md.maxForce = 300.0 * body.GetMass();
                mouseJoint = this.world.CreateJoint(md);
                body.SetAwake(true);
            }
        }

        if(mouseJoint) {
            if(isMouseDown) {
                mouseJoint.setTarget(new Vector2(mouseX, mouseY));
            } else {
                this.world.destroyJoint(mouseJoint);
                mouseJoint = null;
            }
        }

        this.world.step(1 / 60, 10, 10);
    };

    public void dispose (){

    };

    public void updateView (){
        if (!this.paused){
            this.update();
        }
        this.draw();
    }

    public void handleMouseMove(MouseEvent e) {
        mouseX = _navigator.screenPointToWorld(e.getX(), e.getY())[0] / 30;
        mouseY = _navigator.screenPointToWorld(e.getX(), e.getY())[1] / 30;
    }

    public com.badlogic.gdx.physics.box2d.Body getBodyAtMouse(World world) {
        mousePVec = new Vector2(mouseX, mouseY);
//        var aabb = new b2AABB();
//        aabb.lowerBound.Set(mouseX - 0.001, mouseY - 0.001);
//        aabb.upperBound.Set(mouseX + 0.001, mouseY + 0.001);



        // Query the world for overlapping shapes.
        selectedBody = null;

        world.QueryAABB(this::getBodyCB, mouseX - 0.001f, mouseY - 0.001f, mouseX + 0.001f, mouseY + 0.001f);
        return selectedBody;
    }

    public boolean getBodyCB(Fixture fixture) {
        if(fixture.getBody().getType() != BodyDef.BodyType.StaticBody) {
            if(fixture.getShape().TestPoint(fixture.GetBody().GetTransform(), mousePVec)) {
                selectedBody = fixture.getBody();
                return false;
            }
        }
        return true;
    }
}
