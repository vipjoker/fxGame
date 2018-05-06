package mygame.editor.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import mygame.editor.controlers.Controller;
import mygame.editor.controlers.InfoController;
import mygame.editor.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 17.04.18.
 */
public class PhysicsNode extends Pane implements Bodyeable {
    boolean active;
    private Body body;
    private BodyDef bodyDef;
    private Controller controller;
    private List<Fixtureable> fixtures = new ArrayList<>();
    public PhysicsNode(Controller controller){
        this.controller = controller;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        setWidth(50);
        setHeight(50);
        layoutXProperty().addListener(this::onLayoutChange);
        layoutYProperty().addListener(this::onLayoutChange);
        widthProperty().addListener(this::onLayoutChange);
        heightProperty().addListener(this::onLayoutChange);
    }

    public void toggleActive() {
        active = !active;
        if (active) setActive();
        else setNonActive();
    }

    public void setActive() {
        setStyle("-fx-border-color: #b8fff8; -fx-border-width: 1px;-fx-border-radius: 5px;");
    }

    public void setNonActive() {
        setStyle("-fx-border-color: none");
    }

    public void setPosition(float x ,float y){
        bodyDef.position.set(x/32f,y/32f);
        setLayoutX(x);
        setLayoutY(y);

    }


    private void onLayoutChange(ObservableValue<? extends Number> observable,Number oldValue,Number newValue){
        InfoController infoController = controller.getInfoController();
        infoController.setPositionInfo(new Point(getLayoutX(),getLayoutX()));
        infoController.setWidthInfo(getWidth());
        infoController.setHeightInfo(getHeight());
    }


    public void setType(BodyDef.BodyType type){
        bodyDef.type = type;
    }

    public void addFixture(Fixtureable fixtureable){
        fixtures.add(fixtureable);
    }

    @Override
    public Body create(World world) {
        body = world.createBody(bodyDef);

        for (Fixtureable fixtureable : fixtures) {
            fixtureable.create(body);
            getChildren().add((Node)fixtureable);
        }

        return body;
    }



    @Override
    public void update() {
        Vector2 position = body.getPosition();
        setRotate(body.getAngle() * MathUtils.radDeg);
        setLayoutX(position.x * 32);
        setLayoutY(position.y * 32);
    }


}