package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import mygame.editor.component.Component;
import mygame.editor.customShapes.Drawable;
import mygame.editor.model.Point;

import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class CcNode implements Drawable {
    public int id;
    public int layer;
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    public double scaleX = 1;
    public double scaleY = 1;
    private double angle;
    public Affine transform;
    public String name;
    protected BoundingBox bBox;
    public boolean active;
    private CcNode parent;
    private List<Component> components = new ArrayList<>();
    private Anchor anchor = Anchor.BOTTOM_LEFT;

    private List<CcNode> children = new ArrayList<>();

    public List<CcNode> getChildren() {
        return children;
    }

    public void addChild(CcNode node) {
        node.setParent(this);
        children.add(node);
    }

    public double getAngle(){
        return angle;
    }

    public void setAngle(double angle){

        this.angle = angle;
    }

    public void appendAngle(double deltaAngle){
        angle -= deltaAngle;
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

        transform = context.getTransform().clone();

    }


    public boolean contains(Point2D point2D){
        Rectangle2D rectangle2D = new Rectangle2D(x,y,width,height);
        return rectangle2D.contains(point2D);
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public void setPosition(Vector2 vector2){
        setX(vector2.x);
        setY(vector2.y);
    }

    public void move(Point2D point2D){
        this.x -= point2D.getX();
        this.y += point2D.getY();
    }

    public void addComponent(Component component) {
        component.setNode(this);
        components.add(component);
    }



    public <T extends Component> T getComponent(Component.Type type){
        Optional<Component> component = components.stream().filter(e -> e.getType() == type).findFirst();


        if(component.isPresent()){
            return (T)component.get();
        }else{
            return null;
        }
    }

    public final Point2D convertToLocalSpace(Point2D point){

        Affine affine = new Affine();
        affine.appendTranslation(x,y);
        affine.appendRotation(angle);
        affine.appendScale(scaleX,scaleY);
        if(getParent() != null){
            point = getParent().convertToLocalSpace(point);
        }
            try {
                Point2D point2D = affine.inverseTransform(point);
                return point2D;
            } catch (NonInvertibleTransformException e) {
                e.printStackTrace();
                return null;
            }

    }

     public CcNode getSelected(Point2D point2D){
         for (CcNode ccNode : getChildren()) {
             CcNode selected = ccNode.getSelected(point2D);
             if(selected != null){
                 return selected;
             }
         }
         Point2D localPoint = convertToLocalSpace(point2D);
         if (contains(point2D)) {
             setActive(true);
             return this;
         }else {
             return null;
         }
     }
     public void updateAll(Consumer<CcNode> function){
         for (CcNode ccNode : getChildren()) {
             ccNode.updateAll(function);
         }
         function.accept(this);
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
        bBox = new BoundingBox(0, -height, width, height);

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        updateBoundingBox();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;

        updateBoundingBox();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        updateBoundingBox();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        updateBoundingBox();
    }

    public void removeSelf(){
        parent.getChildren().remove(this);
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




