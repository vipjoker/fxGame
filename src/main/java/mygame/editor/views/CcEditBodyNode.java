package mygame.editor.views;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.model.box2d.B2Fixture;

public class CcEditBodyNode extends CcNode{


    private B2Body editBody;
    private boolean isInSimulateMode;

    public CcEditBodyNode(B2Body body) {
        this.editBody = body;
        isInSimulateMode = true;
        x = editBody.getPosition().getX() * 32;
        y = editBody.getPosition().getY()* 32;
        setAngle(-editBody.getAngle() * MathUtils.radDeg);

        for (B2Fixture fixture : body.getFixture()) {
            addFixture(fixture);
        }


   }


    @Override
    public void setX(double x) {
        super.setX(x);
        editBody.getPosition().setX((float) x /32.0f);

    }

    @Override
    public void setY(double y) {
        super.setY(y);
        editBody.getPosition().setY((float) y /32.0f);

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
            for (CcNode ccNode : getChildren()) {

            }
            context.strokeRect(-10,-10,20,20);
            context.stroke();
        }

    }

    private void update(){


            x = editBody.getPosition().getX() * 32;
            y = editBody.getPosition().getY()* 32;
            setAngle(-editBody.getAngle() * MathUtils.radDeg);

    }

    @Override
    public Point2D convertToLocalSpace(Point2D point) {
        final Affine affine = new Affine();
        affine.appendTranslation(x,y);
        affine.appendRotation(getAngle());
        affine.appendScale(scaleX,scaleY);
        try {
            return affine.inverseTransform(point);
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean contains(Point2D point2D) {

        final Rectangle2D rectangle2D = new Rectangle2D(-5, -5, 10, 10);
        return rectangle2D.contains(point2D);
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


