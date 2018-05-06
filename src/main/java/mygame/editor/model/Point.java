package mygame.editor.model;

import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;


public class Point {


    private double x;
    private double y;
    public Point(double x,double y){
        this.x = x;
        this.y = y;
    }

    public Point(Point2D point2D){
        x = point2D.getX();
        y = point2D.getY();
    }

    public Point add(double x,double y){
        this.x+=x;
        this.y+=y;
        return this;
    }

    public void set(Point point){
        this.x = point.x;
        this.y = point.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Double distance(Point point){
        double deltaX  = point.x - x;
        double deltaY = point.y - y;
        return Math.sqrt(deltaX  * deltaX + deltaY * deltaY);
    }

    public Point clone(){

        return new Point(x,y);
    }

    public Point2D toPoint2D(){
        return new Point2D(x,y);
    }

    public Vector2 toVector2d(){
        return new Vector2((float)x,(float)y);
    }
    @Override
    public String toString(){
        return "Point( " + x + ", " + y  + " );";
    }

}
