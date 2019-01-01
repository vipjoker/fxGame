package mygame.editor.model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Node implements ObservableValue<Node> {
    protected final List<ChangeListener<? super Node>> listeners = new ArrayList<>();
    private Node parent;
    public int id;
    public int layer;
    protected final StringProperty name = new SimpleStringProperty("Node");
    protected final Point position = new Point(0,0);
    protected final Point scale = new Point(1,1);
    protected final Point anchor = new Point(0,0);
    protected final DoubleProperty width = new SimpleDoubleProperty(0);
    protected final DoubleProperty height = new SimpleDoubleProperty(0);
    protected final DoubleProperty angle = new SimpleDoubleProperty(0);
    protected final ObservableList<Node> children = FXCollections.observableArrayList();
    protected Physics physics;

    public Node(){
        name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                notifyChanges();
            }
        });

        position.addListener(new ChangeListener<Point>() {
            @Override
            public void changed(ObservableValue<? extends Point> observable, Point oldValue, Point newValue) {
                notifyChanges();
            }
        });

        scale.addListener(new ChangeListener<Point>() {
            @Override
            public void changed(ObservableValue<? extends Point> observable, Point oldValue, Point newValue) {
                notifyChanges();
            }
        });
        anchor.addListener(new ChangeListener<Point>() {
            @Override
            public void changed(ObservableValue<? extends Point> observable, Point oldValue, Point newValue) {
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

        angle.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                notifyChanges();
            }
        });

        children.addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> c) {
                notifyChanges();
            }
        });


    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public StringProperty getName() {
        return name;
    }


    public void setName(String name) {
        this.name.set(name);
    }

    public Point getPosition() {
        return position;
    }

    public Point getScale() {
        return scale;
    }

    public Point getAnchor() {
        return anchor;
    }

    public DoubleProperty getWidth() {
        return width;
    }

    public DoubleProperty getHeight() {
        return height;
    }

    public DoubleProperty getAngle() {
        return angle;
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public void setAngle(double angle) {
        this.angle.set(angle);
    }

    public Physics getPhysics() {
        return physics;
    }

    public void setPhysics(Physics editBody) {
        this.physics = editBody;
        physics.getWidth().bindBidirectional(width);
        physics.getHeight().bindBidirectional(height);
        notifyChanges();
    }

    public void addChild(Node node){
        children.add(node);
    }


    public List<Node> getChildren(){
        return children;
    }

    public void removeSelf() {
        if(parent != null){
            parent.getChildren().remove(this);
        }
    }

    private void notifyChanges(){
        for (ChangeListener<? super Node> listener : listeners) {
            listener.changed(Node.this,Node.this,Node.this);
        }
    }

    @Override
    public void addListener(ChangeListener<? super Node> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Node> listener) {
        this.listeners.remove(listener);
    }

    @Override
    public Node getValue() {
        return this;
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}
