package mygame.editor.views;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import mygame.editor.component.Component;
import mygame.editor.customShapes.Drawable;

import java.util.*;

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
    private CcNode parent;
    private List<Component> components = new ArrayList<>();


    private List<CcNode> children = new ArrayList<>();

    public List<CcNode> getChildren() {
        return children;
    }

    public void addChild(CcNode node) {
        node.setParent(this);
        children.add(node);
    }

    public void setParent(CcNode node){
        this.parent = node;
    }

    public CcNode getParent() {
        return parent;
    }

    @Override
    public void draw(GraphicsContext context, long time) {
        context.save();
        context.translate(x, -y);
        context.rotate(angle);
        context.scale(scaleX, scaleY);
        rasterize(context);
        components.sort(Comparator.comparingInt(Component::getZorder));

        components.forEach(c->{
            c.update();
            c.draw(context);
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
        component.setNode(this);
        components.add(component);
    }

    public <T extends Component> T getCompnent(Component.Type type){
        Optional<Component> component = components.stream().filter(e -> e.getType() == type).findFirst();


        if(component.isPresent()){
            return (T)component.get();
        }else{
            return null;
        }
    }



    public CcNode findViewById(int id){
        return findViewById(this,id);
    }

    private CcNode findViewById(CcNode root, int id){
        if(root.id == id)return root;

        for (CcNode ccNode : root.getChildren()) {

            CcNode n = findViewById(ccNode,id);
                if(n != null)return n;

        }
        return null;
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




