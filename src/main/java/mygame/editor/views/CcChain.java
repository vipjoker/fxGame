package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.model.Point;

public class CcChain extends CcNode {

    private final ChainShape mShape;

    public CcChain(ChainShape chainShape) {
        this.mShape = chainShape;
    }

    @Override
    public void rasterize(GraphicsContext context) {

        boolean isFirstTime = true;
        context.setStroke(Color.GREEN);
        context.setLineWidth(3);
        int size = mShape.getVertexCount();
//        double[] xPoints = new double[size];
//        double[] yPoints = new double[size];
        for (int index = 0; index < size; index++) {
            Vector2 point = new Vector2();
            mShape.getVertex(index,point);
            if(isFirstTime){
                isFirstTime = false;
                context.moveTo(point.x *32 ,-point.y * 32);
            }else{
                context.lineTo(point.x * 32,-point.y * 32);
            }
        }






        context.stroke();

    }
}
