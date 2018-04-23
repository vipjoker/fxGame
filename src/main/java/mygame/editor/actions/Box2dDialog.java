package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mygame.Constants;
import mygame.editor.Controller;
import mygame.editor.TimerCounter;
import mygame.editor.customShapes.Drawable;
import mygame.editor.ui.ChainFixture;
import mygame.editor.ui.CircleFixture;
import mygame.editor.ui.PhysicsNode;
import mygame.editor.parser.Box2dParser;
import mygame.editor.ui.PolygonFixture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/22/17.
 */
public class Box2dDialog implements TimerCounter.FrameRateCallback {

    private World world;
    boolean started;
    private Pane pane;
    GraphicsContext context;
    Canvas canvas;
    List<PhysicsNode> nodes = new ArrayList<>();
    private AnimationTimer animationTimer;
    Controller controller;

    public Box2dDialog(Controller controller) {
        this.controller = controller;
    }

    public void show() {


        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setOnCloseRequest(event -> animationTimer.stop());
        canvas = new Canvas();
        Pane group = new Pane(canvas);
        group.setBackground(new Background(new BackgroundFill(Constants.BACKGROUND, null, null)));
        Scene scene = new Scene(group, 600, 600);
        stage.setScene(scene);
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        context = canvas.getGraphicsContext2D();


        canvas.setOnScroll(event -> {
            System.out.println(event.getDeltaY());

            canvas.getGraphicsContext2D().fillText("Delta " + event.getDeltaY(), 10, 10);
        });

        stage.show();
        initBox2d();
        startTimer();
    }


    private void initBox2d() {
        world = new World(new Vector2(0, 1), false);
        PhysicsNode node = new PhysicsNode(controller);
        node.setActive();

        node.setPosition(40,0);
        PolygonFixture fixture = new PolygonFixture();
        fixture.setRect(50,50);
        node.addFixture(fixture);


        PhysicsNode node2 = new PhysicsNode(controller);
        CircleFixture circleShape = new CircleFixture();
        circleShape.setFixtureRadius(1);
        circleShape.setRadius(1);
        node.addFixture(circleShape);

        ChainFixture chainFixture = new ChainFixture();


        chainFixture.setPoints(
                        new Vector2(0, 0),
                        new Vector2(0, 15),
                        new Vector2(10, 10),
                        new Vector2(17, 12),
                        new Vector2(17, 0)

        );

        PhysicsNode node3 = new PhysicsNode(controller);
        node.addFixture(chainFixture);
        node3.setPosition(15,30);




    }


    @Override
    public void update(long delta) {
        handleHere(delta);
    }

    private void startTimer() {

        animationTimer = new TimerCounter(this);

        animationTimer.start();
    }

    private void handleHere(long now) {
        world.step(1f / 60f, 6, 3);
        context.setFill(Color.rgb(255, 0, 0, .6));
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Drawable drawable : Box2dParser.parseBox2d(world))
            drawable.draw(context, now);



    }


}
