package mygame.editor.customShapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.model.Point;

/**
 * Created by oleh on 17.04.18.
 */
public class Chain implements Drawable {

    private Point[] mPoints;
    public Chain(Point[] points) {
        this.mPoints = points;
    }

    @Override
    public void draw(GraphicsContext context, long time) {
        context.setStroke(Color.WHEAT);
        context.setLineWidth(3);

        boolean isFirstTime = true;
        for (Point p : mPoints){
            if(isFirstTime){
                isFirstTime = false;
                context.moveTo(p.getX() *32 ,p.getY() * 32);
            }else{
                context.lineTo(p.getX() * 32,p.getY() * 32);
            }
        }

        context.stroke();

    }
}
