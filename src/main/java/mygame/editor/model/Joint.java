package mygame.editor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Joint {
    private int bodyA;
    private int bodyB;
    private Point anchorA;
    private Point anchorB;
    private StringProperty type;


    public int getBodyA() {
        return bodyA;
    }

    public void setBodyA(int bodyA) {
        this.bodyA = bodyA;
    }

    public int getBodyB() {
        return bodyB;
    }

    public void setBodyB(int bodyB) {
        this.bodyB = bodyB;
    }

    public Point getAnchorA() {
        return anchorA;
    }

    public void setAnchorA(Point anchorA) {
        this.anchorA = anchorA;
    }

    public Point getAnchorB() {
        return anchorB;
    }

    public void setAnchorB(Point anchorB) {
        this.anchorB = anchorB;
    }

    public StringProperty getType() {
        return type;
    }


    public void setType(String type) {
        this.type.set(type);
    }
}
