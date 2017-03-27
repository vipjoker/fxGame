package mygame.editor.model;

import javafx.geometry.Point2D;

/**
 * Created by oleh on 3/26/17.
 */
public class Point {
    private double x,y;
    public Point(double x,double y){
        this.x = x;
        this.y = y;
    }

    public Point(Point2D point2D){
        this(point2D.getX(),point2D.getY());
    }

    public void set(double x, double y){
        setX(x);
        setY(y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


}
