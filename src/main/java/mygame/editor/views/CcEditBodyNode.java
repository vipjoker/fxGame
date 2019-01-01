package mygame.editor.views;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.model.box2d.*;
import mygame.editor.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class CcEditBodyNode extends NodeView {


    private B2Body editBody;
    private boolean isInSimulateMode;
    private final List<CcFixtureNode> fixtureNodes = new ArrayList<>();


    public static CcEditBodyNode create(String type,float x,float y){
        B2Body body = null;
        switch (type){
            case Constants.PARAM_SQUARE: {
                body = new B2Body(B2Type.DYNAMIC, new B2Point(x, y));
                B2Fixture fixture = new B2Fixture(B2FixtureType.POLYGON, Vector2.Zero, new Vector2(1, 0),new Vector2(1,1),new Vector2(0,1));
                body.addFixture(fixture);
            }
            break;
            case Constants.PARAM_CIRCLE: {
                body = new B2Body(B2Type.DYNAMIC, new B2Point(x, y));
                B2Fixture fixture = new B2Fixture(B2FixtureType.CIRCLE, Vector2.Zero, new Vector2(0, 1));
                body.addFixture(fixture);
            }
            break;
            case Constants.PARAM_CHAIN:{
                body = new B2Body(B2Type.STATIC, new B2Point(x, y));
                B2Fixture fixture = new B2Fixture(B2FixtureType.CHAIN, new Vector2(0,1), new Vector2(1, 0),new Vector2(2,0),new Vector2(3,1));
                body.addFixture(fixture);
            }
            break;
            case Constants.PARAM_EDGE:{

            }
            break;
        }
        CcEditBodyNode bodyNode = new CcEditBodyNode(body);
        return bodyNode;
    }

    public CcEditBodyNode(B2Body body) {
        this.editBody = body;
        isInSimulateMode = true;
        x.set(editBody.getPosition().getX() * 32);
        y.set(editBody.getPosition().getY()* 32);
        setAngle(-editBody.getAngle() * MathUtils.radDeg);

        x.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                editBody.getPosition().setX( (float) (newValue.doubleValue() /32.0));
            }
        });

        y.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                editBody.getPosition().setY((float) (newValue.doubleValue() /32.0));
            }
        });





        for (B2Fixture fixture : body.getFixture()) {
            addFixture(fixture);
        }


   }



    @Override
    public void setAngle(double angle) {
        super.setAngle(angle);
        editBody.setAngle((float) -angle * MathUtils.degRad);
    }

    @Override
    public void rasterize(GraphicsContext context) {

        update();
        context.beginPath();
        context.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.5));
        context.fillRect(-5, -5, 10, 10);
        context.fill();
        context.beginPath();
        context.setStroke(Color.GREEN);
        context.moveTo(-10, 0);
        context.lineTo(10, 0);
        context.closePath();
        context.stroke();

        context.beginPath();
        context.setStroke(Color.RED);
        context.moveTo(0, -10);
        context.lineTo(0, 10);
        context.closePath();
        context.stroke();

        if(active){
            context.beginPath();
            context.setStroke(Color.CYAN);
            context.strokeRect(-10,-10,20,20);
            context.stroke();
        }

    }

    private void update(){
        x.setValue(editBody.getPosition().getX() * 32);
        y.setValue(editBody.getPosition().getY()* 32);
        setAngle(-editBody.getAngle() * MathUtils.radDeg);
    }

    @Override
    public boolean contains(Point2D point2D) {

        final Rectangle2D rectangle2D = new Rectangle2D(-5, -5, 10, 10);
        return rectangle2D.contains(point2D);
    }

    public List<CcFixtureNode> getFixtureNodes() {
        return fixtureNodes;
    }

    public void addFixture(B2Fixture fixtureDef) {
        CcFixtureNode fixtureNode = null;
        switch (fixtureDef.getType()){
            case POLYGON:

               fixtureNode = new CcPolygon(fixtureDef);

                break;
            case CHAIN:
               fixtureNode = new CcChain(fixtureDef);

                break;
            case EDGE:
                break;
            case CIRCLE:
               fixtureNode = new CcCircle(fixtureDef);
                break;
        }

        if (fixtureNode != null){
            addChild(fixtureNode);
            fixtureNodes.add(fixtureNode);
        }
    }

    public void save(){

    }

    public B2Body getB2EditBody(){
        return editBody;
    }
}


