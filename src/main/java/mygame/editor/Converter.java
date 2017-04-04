package mygame.editor;

import javafx.geometry.Point2D;



public abstract class Converter{
    public static double countXdistance(Point2D start,Point2D end){
        return Math.abs(start.getX() - end.getX());
    }

    public static double countYDistance(Point2D start, Point2D end){
        return Math.abs(start.getY()-end.getY());
    }
}