package mygame.editor.model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Physics implements ObservableValue<Physics> {
    private StringProperty type = new SimpleStringProperty("static");
    private StringProperty shape = new SimpleStringProperty("rect");

    private DoubleProperty restitution = new SimpleDoubleProperty(1);
    private DoubleProperty density = new SimpleDoubleProperty(1);
    private DoubleProperty friction = new SimpleDoubleProperty(1);

    private DoubleProperty width = new SimpleDoubleProperty(0);
    private final DoubleProperty height = new SimpleDoubleProperty(0);
    private final Point center = new Point(0,0);
    private DoubleProperty radius = new SimpleDoubleProperty(0);
    private ObservableList<Point> points = FXCollections.observableArrayList();

    private final List<ChangeListener<? super Physics>> listeners = new ArrayList<>();


    public Physics(){
        type.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                notifyChanges();
            }
        });

        shape.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                notifyChanges();
            }
        });
        restitution.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                notifyChanges();
            }
        });

        density.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                notifyChanges();
            }
        });

        friction.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                notifyChanges();
            }
        });

        width.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                notifyChanges();
            }
        });

        height.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                notifyChanges();
            }
        });

        center.addListener(new ChangeListener<Point>() {
            @Override
            public void changed(ObservableValue<? extends Point> observable, Point oldValue, Point newValue) {
                notifyChanges();
            }
        });

        radius.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                notifyChanges();
            }
        });

    }

    private void notifyChanges(){
        for (ChangeListener<? super Physics> listener : listeners) {
            listener.changed(Physics.this,Physics.this,Physics.this);
        }
    }

    public DoubleProperty getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
    }

    public StringProperty getType() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty getShape() {
        return shape;
    }


    public void setShape(String shape) {
        this.shape.set(shape);

        switch (shape){
            case "circle":
                radius.set(width.doubleValue()/2);
                break;
            case "rect":
                break;
            case "chain":
                points.clear();
                points.add(new Point(0,0));
                points.add(new Point(width.doubleValue(),0));
                points.add(new Point(width.doubleValue(),height.doubleValue()));
                break;
        }
    }

    public DoubleProperty getRestitution() {
        return restitution;
    }


    public void setRestitution(double restitution) {
        this.restitution.set(restitution);
    }

    public DoubleProperty getDensity() {
        return density;
    }



    public void setDensity(double density) {
        this.density.set(density);
    }

    public DoubleProperty getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction.set(friction);
    }

    public DoubleProperty getWidth() {
        return width;
    }


    public void setWidth(double width) {
        this.width.set(width);
    }

    public DoubleProperty getHeight() {
        return height;
    }


    public void setHeight(double height) {
        this.height.set(height);
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(double x,double y) {
        this.center.set(x,y);
    }

    public ObservableList<Point> getPoints() {
        return points;
    }

    public void setPoints(ObservableList<Point> points) {
        this.points = points;
    }

    @Override
    public void addListener(ChangeListener<? super Physics> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Physics> listener) {
        listeners.remove(listener);
    }

    @Override
    public Physics getValue() {
        return null;
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}
