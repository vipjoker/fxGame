package mygame.effects;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;
import mygame.Constants;
import mygame.editor.TimerCounter;
import mygame.editor.customShapes.Drawable;
import mygame.editor.model.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Admin on 12.06.2016.
 */
public class ColorCube extends Application implements TimerCounter.FrameRateCallback{
    private Image crate = new Image(getClass().getResourceAsStream("/background/Object/Crate.png"));
    private Image mushroom = new Image(getClass().getResourceAsStream("/background/Object/Mushroom_1.png"));
//    private Image background = new Image(getClass().getResourceAsStream("/background/BG/BG.png"));

    private Scene scene;
    private double scale = 1;
    private double scrollx = 400;
    private double scrolly = 400;
    private double translatex = 0;
    private double translatey = 0;


    private double width = 0;
    private double heigth = 0;
    private GraphicsContext graphicsContext;
    private TimerCounter counter;
    private Point2D lastPoint;
    Grid grid = new Grid();
    List<CcNode> nodes = new ArrayList<>();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cube java.sample");
        Canvas canvas = new Canvas(800, 800);



        graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.setLineWidth(.5);
        Group root = new Group(canvas);

        scene = new Scene(root, 800, 800);

        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        primaryStage.setScene(scene);


        counter = new TimerCounter(this);
        counter.start();

        scene.widthProperty().addListener(this::onChangeWidth);
        scene.heightProperty().addListener(this::onChangeHeight);
        scene.setOnScroll(this::onScroll);
        canvas.setOnMouseDragged(this::onDrag);
        canvas.setOnMouseReleased(this::onMouseReleased);
        canvas.setOnMouseClicked(this::onMouseClicked);
        initNodes();
        draw(graphicsContext, 800, 800);
        primaryStage.show();

    }

    private void onMouseClicked(MouseEvent event) {


        Deque<List<CcNode>> lists = new LinkedList<>();

        lists.push(nodes);
        boolean isPressed = false;
        while (lists.peek()!=null){

            List<CcNode> pop = lists.pop();
            for(CcNode n : pop){
                if(!n.children.isEmpty()){
                    lists.push(n.children);
                }
                if(n.contains(event.getX(),event.getY()) && !isPressed) {
                    n.setActive(true);
                    isPressed = true;
                }else{
                    n.setActive(false);
                }

            }
        }





        draw(graphicsContext,width,heigth);
    }

    private void initNodes(){

        CcSprite small = new CcSprite(crate);
        CcSprite mush = new CcSprite(mushroom, 20, 20);
        CcSprite mush2 = new CcSprite(mushroom);
        CcCircle circle = new CcCircle(0, 0, 100);
        mush2.x = crate.getWidth();

        CcSprite ccSprite2 = new CcSprite(crate, 60, 60);
        ccSprite2.x = 100;
        ccSprite2.y = 100;

        ccSprite2.addChild(mush);
        ccSprite2.addChild(mush2);
//        nodes.add(new CcSprite(background));
        nodes.add(small);
        nodes.add(ccSprite2);
        nodes.add(circle);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        counter.stop();
    }

    private void onMouseReleased(MouseEvent event) {
        lastPoint = null;
    }


    private void onDrag(MouseEvent event) {
        if(event.getButton() != MouseButton.SECONDARY)return;
        double x = event.getX();
        double y = event.getY();
        Point2D p = new Point2D(x,y);
        if(lastPoint != null){
            Point2D subtract = lastPoint.subtract(p);
            translatex -= subtract.getX();
            translatey -= subtract.getY();
            draw(graphicsContext, width, heigth);
        }

        lastPoint = p;
    }

    private void onScroll(ScrollEvent scrollEvent) {
        if(scrollEvent.getDeltaY() < 0 && scale < 0.2)return;
        if(scrollEvent.getDeltaY() > 0 && scale > 5)return;
        scale += (scrollEvent.getDeltaY() / 1000);
        scrollx= scrollEvent.getX();
        scrolly= scrollEvent.getY();
        draw(graphicsContext, width, heigth);


    }


    private void onChangeWidth(ObservableValue<? extends Number> observable, Number old, Number newValue) {
        width = (double)newValue;
        draw(graphicsContext,width,heigth);
    }

    private void onChangeHeight(ObservableValue<? extends Number> observable, Number old, Number newValue) {
        heigth = (double)newValue;
        draw(graphicsContext,width,heigth);
    }

    private void draw(GraphicsContext g, double width, double height) {
        g.clearRect(0,0,width,height);
        g.setFill(Constants.BACKGROUND);
        g.fillRect(0,0,width,height);
        g.setFill(Constants.WHITE);
        g.save();
        g.transform(new Affine(Affine.translate(translatex,translatey)));
        g.transform(new Affine(Affine.scale(this.scale, scale,scrollx,scrolly)));


        g.translate(20, height - 20);
        for (CcNode node : nodes) {
            node.draw(g,0);
        }
        grid.draw(g,0);
        g.restore();
    }

    @Override
    public void update(long delta) {
//        nodes.forEach(e->e.angle++);
//        draw(graphicsContext, 800, 800);
    }

    @Override
    public void seconds(long seconds) {



    }

    class CcNode implements Drawable {
        public int layer  = 0;
        public double x;
        public double y;
        public double width;
        public double height;
        public double scaleX = 1;
        public double scaleY = 1;
        public double angle;
        public Affine transform;
        public boolean active;

        List<CcNode> children = new ArrayList<>();

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
            children.forEach(n->n.draw(context,time));
            context.restore();

        }

        public void rasterize(GraphicsContext context) {
            transform = context.getTransform();

        }

        public boolean contains(double x,double y){
            return false;
        }

        public void setActive(boolean isActive){

        }
    }

    class CcSprite extends CcNode {
        private Image image;

        public CcSprite(Image image) {
            this.image = image;
            width = image.getWidth();
            height = image.getHeight();
        }

        public CcSprite(Image image, double width, double height) {
            this.image = image;
            this.width = width;
            this.height = height;
        }

        @Override
        public void rasterize(GraphicsContext context) {
            transform = context.getTransform();
            context.drawImage(image, 0, -(height ), width, height);

            if(active){
                context.setLineWidth(2);
                context.setStroke(Constants.GREEN);
                context.strokeRect(0, -(height ), width, height);
            }
        }
        @Override
        public boolean contains(double x,double y){
            try {
                Point2D point2D = this.transform.inverseTransform(x, y);
                Bounds bounds = new BoundingBox(0, -(height), width, height);
                return bounds.contains(point2D);
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        public void setActive(boolean isActive){
            this.active = isActive;
        }
    }

    class CcCircle extends CcNode{

        private double radius;
        public CcCircle(double x,double y,double radius){
            this.x = x;
            this.y = y;

            this.radius = radius;

        }

        @Override
        public void rasterize(GraphicsContext context) {
            transform = context.getTransform();
            context.setFill(Constants.RED);
            context.fillOval(this.x - radius,this.y - radius,radius*2,radius*2);
            if(active){
                context.setLineWidth(2);
                context.setStroke(Constants.GREEN);
                context.strokeOval(this.x - radius,this.y - radius,radius*2,radius*2);
            }
        }
        @Override
        public boolean contains(double x,double y){
            try {
                Point2D transform = this.transform.inverseTransform(x, y);
                Point2D center = new Point2D(this.x, this.y);
                double distance = transform.distance(center);
                return distance < radius;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void setActive(boolean active){
            this.active = active;
        }
    }

    class Grid implements Drawable {

        private int gridStep = 10;
        @Override
        public void draw(GraphicsContext g, long time) {
            g.beginPath();
            g.setTextAlign(TextAlignment.CENTER);
            g.setTextBaseline(VPos.CENTER);


            Affine transform = g.getTransform();
            try {

                Point2D zero = new Point2D(0, 0);
                Point2D leftUp = new Point2D(width, heigth);

                Point2D begin = transform.inverseTransform(zero);
                Point2D end = transform.inverseTransform(leftUp);

                double scale =  begin.distance(end) / zero.distance(leftUp);
                int scaleFactor = (int)(scale*5);
                System.out.println(scale + " " + scaleFactor);

                g.setFont(Font.font(10 * scale));
                int step = gridStep * scaleFactor;
                step = 10;
                System.out.println(step);
                double beginx = begin.getX() - begin.getX()%step ;
                double beginy = begin.getY() - begin.getY()%step;
                g.beginPath();
                //horizontal
                for (int i = (int)beginy + 40; i < end.getY() -40; i += step) {

                    g.beginPath();

                    if(i == 0){
                        g.setStroke(Color.RED);
                    }else{
                        g.setStroke(Constants.WHITE);
                    }

                    if(i % 100 == 0){
                        g.setLineWidth(1);
                        g.fillText(String.valueOf(-i), begin.getX() +15, i);
                    }else{
                        g.setLineWidth(.2);
                    }

                    g.moveTo( 40 + begin.getX(), i);
                    g.lineTo(end.getX() , i);
                    g.closePath();
                    g.stroke();


                }
                //vertical
                for(int i = (int)beginx + 40;i< end.getX(); i += step){
                    g.beginPath();
                    if(i == 0){
                        g.setStroke(Color.RED);
                    }else{
                        g.setStroke(Constants.WHITE);
                    }


                    if(i % 100 == 0){
                        g.setLineWidth(1);
                        g.fillText(String.valueOf(i), i, end.getY() - 15);
                    }else{
                        g.setLineWidth(.2);
                    }

                    g.moveTo(i, begin.getY()  );
                    g.lineTo(i,  end.getY() - 40);
                    g.closePath();
                    g.stroke();

                }



            } catch (NonInvertibleTransformException e) {
                e.printStackTrace();
            }





        }
    }
}
