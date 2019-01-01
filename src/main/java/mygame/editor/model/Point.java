package mygame.editor.model;

import com.badlogic.gdx.math.Vector2;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;


public class Point implements ObservableValue<Point> {

    private List<ChangeListener<? super Point>> mListeners = new ArrayList<>();
    private final DoubleProperty x;
    private final DoubleProperty y;

    public Point(double x, double y) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        listenForProperties();
    }


    public Point add(double x, double y) {
        this.x.setValue(this.x.doubleValue() + x);
        this.y.setValue(this.y.doubleValue() + y);
        return this;
    }

    private void listenForProperties() {
        x.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                for (ChangeListener<? super Point> listener : mListeners) {
                    listener.changed(Point.this, Point.this, Point.this);
                }
            }
        });

        y.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                for (ChangeListener<? super Point> listener : mListeners) {
                    listener.changed(Point.this,Point.this,Point.this);
                }
            }
        });
    }


    public DoubleProperty getX() {
        return x;
    }

    public DoubleProperty getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x.set(x);
        this.y.set(y);
    }

    public Double distance(Point point) {
        double deltaX = point.x.subtract(x).doubleValue();
        double deltaY = point.y.subtract(y).doubleValue();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public Point clone() {

        return new Point(x.doubleValue(), y.doubleValue());
    }

    public Point2D toPoint2D() {
        return new Point2D(x.doubleValue(), y.doubleValue());
    }

    public Vector2 toVector2d() {
        return new Vector2(x.floatValue(), y.floatValue());
    }

    @Override
    public String toString() {
        return "Point( " + x + ", " + y + " );";
    }

    @Override
    public void addListener(ChangeListener<? super Point> listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Point> listener) {
        mListeners.remove(listener);
    }

    @Override
    public Point getValue() {
        return null;
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}
