package mygame.editor.views;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.customShapes.Chain;
import mygame.editor.model.box2d.B2Body;

public class CcBodyNode extends CcNode {
    private Body body;

    public CcBodyNode(Body body) {
        this.body = body;
        for (Fixture fixture : body.getFixtureList()) {
            addFixture(fixture);
        }
    }


    @Override
    public void rasterize(GraphicsContext context) {

        update();

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

    }

    private void update() {


        x = body.getPosition().x * 32;
        y = body.getPosition().y * 32;
        setAngle(-body.getAngle() * MathUtils.radDeg);
    }


    public void addFixture(Fixture fixtureDef) {
        switch (fixtureDef.getShape().getType()) {
            case Polygon:
                PolygonShape polygonShape = (PolygonShape) fixtureDef.getShape();
                CcPolygon polygon = new CcPolygon(polygonShape);
                getChildren().add(polygon);

                break;
            case Chain:
                ChainShape chainShape = (ChainShape) fixtureDef.getShape();
                CcChain chain = new CcChain(chainShape);
                getChildren().add(chain);
                break;
            case Edge:
                break;
            case Circle:
                CircleShape circleShape = (CircleShape) fixtureDef.getShape();
                CcCircle ccCircle = new CcCircle(circleShape);
                getChildren().add(ccCircle);
                break;
        }
    }
}
