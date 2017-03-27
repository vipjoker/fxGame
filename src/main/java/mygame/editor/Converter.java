package mygame.editor;

import javafx.geometry.Point2D;

/**
 * Created by oleh on 3/19/17.
 */

public abstract class Converter{
    public static double countXdistance(Point2D start,Point2D end){
        return Math.abs(start.getX() - end.getX());
    }

    public static double countYDistance(Point2D start, Point2D end){
        return Math.abs(start.getY()-end.getY());
    }
}