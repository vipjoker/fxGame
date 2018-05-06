package mygame.editor;

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
import mygame.editor.util.Constants;
import mygame.editor.controlers.Controller;
import mygame.editor.ui.ChainFixture;
import mygame.editor.ui.CircleFixture;
import mygame.editor.ui.PhysicsNode;
import mygame.editor.ui.PolygonFixture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/22/17.
 */
public class  Box2dDialog implements TimerCounter.FrameRateCallback {

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
        initBox2d(group);
        startTimer();
    }


    private void initBox2d(Pane group) {
        world = new World(new Vector2(0, 1), false);
        //first
        PhysicsNode node = new PhysicsNode(controller);
        node.setActive();

        node.setPosition(100, 0);
        PolygonFixture fixture = new PolygonFixture();
        fixture.setRect(50, 50);
        node.addFixture(fixture);

        //second
        PhysicsNode second = new PhysicsNode(controller);
        second.setPosition(50,0);
        CircleFixture circleShape = new CircleFixture();
        circleShape.setFixtureRadius(1);

        second.addFixture(circleShape);

        //third
        PhysicsNode node3 = new PhysicsNode(controller);


        ChainFixture chainFixture = new ChainFixture();


        chainFixture.setPoints(
                new Vector2(0, 0),
                new Vector2(0, 15),
                new Vector2(10, 10),
                new Vector2(17, 12),
                new Vector2(17, 0)

        );

        node3.addFixture(chainFixture);
        node3.setType(BodyDef.BodyType.StaticBody);
        node3.setPosition(15, 30);
        group.getChildren().addAll(node, second, node3);
        nodes.add(node);
        nodes.add(second);
        nodes.add(node3);
        nodes.forEach(n -> {
            n.setActive();
            n.create(world);
        });


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

        nodes.forEach(PhysicsNode::update);


    }
}
