package mygame.editor.views;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import mygame.editor.component.Component;
import mygame.editor.customShapes.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CcNode implements Drawable {
    public int id;
    public int layer;
    public double x;
    public double y;
    public double width;
    public double height;
    public double scaleX = 1;
    public double scaleY = 1;
    public double angle;
    public Affine transform;
    public String name;
    protected BoundingBox bBox;
    public boolean active;
    private Map<Class,Component> components = new HashMap<>();


    private List<CcNode> children = new ArrayList<>();

    public List<CcNode> getChildren() {
        return children;
    }

    public void addChild(CcNode node) {
        children.add(node);
    }

    @Override
    public void draw(GraphicsContext context, long time) {
        context.save();
        context.translate(x, -y);
        context.rotate(angle);
        context.scale(scaleX, scaleY);
        rasterize(context);
        components.forEach((k,v)->{
            v.update();
            v.draw(context);
        });
        children.forEach(n -> n.draw(context, time));
        context.restore();

    }

    public void rasterize(GraphicsContext context) {

        transform = context.getTransform();

    }

    public boolean contains(double x, double y) {
        return false;
    }

    public void setActive(boolean isActive) {

    }

    public void addComponent(Component component) {
        Class<? extends Component> aClass = component.getClass();
        components.put(aClass, component);
    }

    public <T extends Component> T getCompnent(Class<T> clazz){
        Component component = components.get(clazz);
        if(component!= null){
            return (T)component;
        }else{
            return null;
        }
    }

    public void setAnchor(Anchor anchor) {
        switch (anchor) {
            case MIDDLE:
                bBox = new BoundingBox(-width / 2, -height / 2, width, height);
                break;
            case BOTTOM_RIGHT:
                bBox = new BoundingBox(-width, -height, width, height);
                break;
            case BOTTOM_LEFT:
                bBox = new BoundingBox(0, -height, width, height);
                break;

            case TOP_LEFT:
                bBox = new BoundingBox(0, 0, width, height);
                break;

            case TOP_RIGHT:
                bBox = new BoundingBox(-width, 0, width, height);
                break;

        }
    }

    public enum Anchor {
        MIDDLE,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    public void updateBoundingBox(){

    }

    @Override
    public String toString() {
        return "CcNode{" +
                "id=" + id +
                ", layer=" + layer +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", scaleX=" + scaleX +
                ", scaleY=" + scaleY +
                ", angle=" + angle +
                ", transform=" + transform +
                ", name='" + name + '\'' +
                ", bBox=" + bBox +
                ", active=" + active +
                ", components=" + components +
                ", children=" + children +
                '}';
    }
}




