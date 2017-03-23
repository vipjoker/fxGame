package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mygame.Constants;
import mygame.editor.DragHelper;

/**
 * Created by oleh on 3/22/17.
 */
public class Box2dDialog {

    private World world;
    private Body body;
    boolean started;
    private AnimationTimer animationTimer;


    public  void show(){

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Pane pane = new Pane();


            pane.setBackground(new Background(new BackgroundFill(Constants.DARK,null,null)));
            Scene scene = new Scene(pane, 600, 600);
            stage.setScene(scene);

            setChildren(pane,scene);
            stage.show();
        }


    private  void setChildren(Pane pane,Scene scene){}

    private  void initChildren(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 10);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f, .5f);
//        if (!started) {
//            animationTimer.start();
//            started = true;
//        } else {
//            animationTimer.stop();
//            started = false;
//        }
        world = new World(new Vector2(0, 10), false);
        body = world.createBody(bodyDef);
        body.createFixture(shape, .1f);


        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> handleHere(now));

            }
        };

    }

    private void handleHere(long now) {
        Array<Body> bodies = new Array<>();
        world.step(1f / 60f, 6, 3);
        world.getBodies(bodies);

        // pnGrid.getChildren().clear();
        for (Body body : bodies) {

            Vector2 position = body.getPosition();

            Circle circle = new Circle(position.x, position.y, 10, Color.BLUE);

            //  pnGrid.getChildren().add(circle);

        }

    }


}
