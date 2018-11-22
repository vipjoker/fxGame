package mygame.editor.model;

import javafx.beans.property.SimpleDoubleProperty;

public class Rect {
    private SimpleDoubleProperty x = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty y = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty width = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty height = new SimpleDoubleProperty(0);

    public Rect(SimpleDoubleProperty x, SimpleDoubleProperty y, SimpleDoubleProperty width, SimpleDoubleProperty height) {
        this.x.bind(x);
        this.y.bind(y);
        this.width = width;
        this.height = height;
    }

    public boolean contains(double x,double y){
        boolean xMatch = x >= this.x.doubleValue() && x<= this.x.doubleValue() + this.width.doubleValue();
        boolean yMatch = y >= this.y.doubleValue() && y <= this.y.doubleValue() + this.height.doubleValue();
        return xMatch && yMatch;

    }


}
