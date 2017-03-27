package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;

import javafx.stage.Modality;
import javafx.stage.Stage;
import mygame.Constants;
import mygame.editor.DragHelper;
import mygame.editor.customShapes.Circle;
import mygame.editor.customShapes.Drawable;
import mygame.editor.customShapes.OnDragListener;
import mygame.editor.model.Point;
import mygame.editor.parser.Box2dParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/22/17.
 */
public class Box2dDialog {

    private World world;
    private Body body;
    boolean started;
    private Pane pane;
    GraphicsContext context;
    Canvas canvas;

    private AnimationTimer animationTimer;


    public void show() {



        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setOnCloseRequest(event -> animationTimer.stop());
        canvas = new Canvas();
        Group group = new Group(canvas);

        // pane.setBackground(new Background(new BackgroundFill(Constants.DARK, null, null)));
        Scene scene = new Scene(group, 600, 600);
        stage.setScene(scene);
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        context = canvas.getGraphicsContext2D();

//
//        canvas.setOnMouseDragged(event -> {
//            for(OnDragListener listener : listeners)
//                if(listener.intersect(event.getX(),event.getY()))
//                    listener.onDrag(new Point(event.getX(),event.getY()));
//
//        });

        canvas.setOnScroll(event -> {
            System.out.println(event.getDeltaY());

            canvas.getGraphicsContext2D().fillText("Delta " + event.getDeltaY(), 10, 10);
        });

        stage.show();
        initBox2d();
        startTimer();
    }


    private void initBox2d() {
        world = new World(new Vector2(0, 10), false);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 10);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f, .5f);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(1);



        body = world.createBody(bodyDef);

        bodyDef.position.set(15,10);
        body.createFixture(shape, .1f);
        world.createBody(bodyDef).createFixture(circleShape,1);

    }

    private void startTimer() {

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> handleHere(now));

            }
        };

        animationTimer.start();
    }

    private void handleHere(long now) {
        world.step(1f/60f,6,3);

        context.clearRect(0, 0, canvas.getWidth(),canvas.getHeight());

        for (Drawable drawable : Box2dParser.parseBox2d(world)) drawable.draw(context, now);



//        Array<Body> bodies = new Array<>();
//        world.step(1f / 60f, 6, 3);
//        world.getBodies(bodies);
//
//        // pnGrid.getChildren().clear();
//        for (Body body : bodies) {
//
//            Vector2 position = body.getPosition();
//
//            Circle circle = new Circle(position.x, position.y, 10, Color.BLUE);
//
//            //  pnGrid.getChildren().add(circle);
//
//        }

    }

    private void checkListeners() {

    }


}
