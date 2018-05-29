package mygame.editor.views;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import mygame.editor.util.Constants;
import mygame.editor.customShapes.Drawable;

public class Grid implements Drawable {

    private int gridStep = 100;
    private int padding = 40;
    private Affine lastTransform;

    @Override
    public void draw(GraphicsContext g, long time) {
        g.beginPath();
        g.setTextAlign(TextAlignment.RIGHT);
        g.setTextBaseline(VPos.CENTER);


        Affine transform = g.getTransform();
        lastTransform = transform.clone();
        try {

            Point2D zero = new Point2D(50, -50);
            Point2D leftUp = new Point2D(Global.getWidth(), Global.getHeight());

            Point2D begin = transform.inverseTransform(zero);
            Point2D end = transform.inverseTransform(leftUp);

            double scale = begin.distance(end) / zero.distance(leftUp);
            int scaleFactor = (int) (scale * 5);

            g.setFont(Font.font(10 * scale));
            int step = gridStep * scaleFactor;
            step = 100;

//            if (scaleFactor < 2) {
//                step = 5;
//            } else if (scaleFactor < 5) {
//                step = 10;
//            } else if (scaleFactor < 10) {
//                step = 20;
//            } else if (scaleFactor < 15) {
//                step = 50;
//            }
            padding = (int) (40 * scale);
            padding = 40;
            double beginx = begin.getX() - begin.getX() % step;
            double beginy = begin.getY() - begin.getY() % step;
            g.beginPath();
            //horizontal

            Color white = Constants.WHITE.deriveColor(1, 1, 1, 0.3);

            for (int i = (int) beginy ; i < end.getY() ; i += step) {

                g.beginPath();

                if (i == 0) {
                    g.setStroke(Color.WHITE);
                } else {
                    g.setStroke(white);
                }

                if (i % 100 == 0) {
                    g.setLineWidth(1);
                    g.fillText(String.valueOf(-i), begin.getX() , i);
                } else {
                    g.setLineWidth(.2);
                }

                g.moveTo( begin.getX(), i);
                g.lineTo(end.getX(), i);
                g.closePath();
                g.stroke();


            }
            //vertical
            for (int i = (int) beginx ; i < end.getX(); i += step) {
                g.beginPath();
                if (i == 0) {
                    g.setStroke(Color.WHITE);
                } else {
                    g.setStroke(white);
                }


                if (i % 100 == 0) {
                    g.setLineWidth(1);
                    g.fillText(String.valueOf(i), i, end.getY() - 15);
                } else {
                    g.setLineWidth(.2);
                }

                g.moveTo(i, begin.getY());
                g.lineTo(i, end.getY() );
                g.closePath();
                g.stroke();

            }


        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
    }

    public Point2D transformPoint(MouseEvent event){

        return transformPoint(event.getX(),event.getY());
    }

    public Point2D transformPoint(double x,double y){
        Point2D point2D = null;
        try {
            point2D = lastTransform.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
        return point2D;
    }
}



