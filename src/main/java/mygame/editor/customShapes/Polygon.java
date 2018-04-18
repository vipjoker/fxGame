package mygame.editor.customShapes;

import javafx.scene.canvas.GraphicsContext;
import mygame.editor.model.Point;

import java.util.List;

/**
 * Created by oleh on 3/26/17.
 */
public class Polygon implements Drawable {
    private List<Point> points;
    private double [] yPoints, xPoints;
    public Polygon(List<Point> points){

        System.out.println("Polygon created x " + points);
        this.points = points;
    }

    @Override
    public void draw(GraphicsContext context, long time) {
        if(xPoints == null || yPoints == null){
            xPoints = new double[points.size()];
            yPoints = new double [points.size()];
            for(int i = 0 ; i < points.size(); i ++ ){
                Point point = points.get(i);
                xPoints[i] = point.getX() *32;
                yPoints[i] = point.getY() *32;
            }
        }
        context.save();
        context.rotate(40);

        context.fillPolygon(xPoints,yPoints,points.size());
        context.restore();
    }


}
