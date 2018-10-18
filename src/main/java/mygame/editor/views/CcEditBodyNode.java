package mygame.editor.views;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.model.box2d.B2Fixture;

public class CcEditBodyNode extends CcNode{


    private B2Body editBody;
    private boolean isInSimulateMode;

    public CcEditBodyNode(B2Body body) {
        this.editBody = body;
        isInSimulateMode = true;
        for (B2Fixture fixture : body.getFixture()) {
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

    private void update(){


            x = editBody.getPosition().getX() * 32;
            y = editBody.getPosition().getY()* 32;
            setAngle(-editBody.getAngle() * MathUtils.radDeg);

    }


    public void addFixture(B2Fixture fixtureDef) {
        switch (fixtureDef.getType()){
            case POLYGON:

                CcPolygon polygon = new CcPolygon(fixtureDef);
                getChildren().add(polygon);

                break;
            case CHAIN:
                CcChain chain = new CcChain(fixtureDef);
                getChildren().add(chain);
                break;
            case EDGE:
                break;
            case CIRCLE:
                CcCircle ccCircle = new CcCircle(fixtureDef);
                getChildren().add(ccCircle);
                break;
        }
    }
}


