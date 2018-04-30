package mygame.effects;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import mygame.editor.customShapes.Drawable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12.06.2016.
 */
public class ColorCube extends Application {
    private Image crate = new Image(getClass().getResourceAsStream("/background/Object/Crate.png"));
    private Scene scene;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cube java.sample");
        Canvas canvas = new Canvas(800,800);
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();


        Group root = new Group(canvas);

        scene = new Scene(root,800,800,true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setScene(scene);
        draw(graphicsContext2D,800,800);

        scene.widthProperty().addListener(this::onChangeWidth);
        scene.heightProperty().addListener(this::onChangeHeight);
        primaryStage.show();

    }


    private void onChangeWidth(ObservableValue<? extends Number> observable,Number old,Number newValue){

    }

    private void onChangeHeight(ObservableValue<? extends Number> observable,Number old,Number newValue){

    }

    private void draw(GraphicsContext g,double width,double height) {
        g.setLineWidth(1);

        g.translate(20,20);

        for(int i = 20; i < 500; i+= 20){

                g.moveTo(0,i);
                g.lineTo(500,i);
                g.moveTo(i,0);
                g.lineTo(i,500);
                g.setFont(Font.font(10));
                g.setTextAlign(TextAlignment.CENTER);
                g.setTextBaseline(VPos.CENTER);
                g.fillText(String.valueOf(i),-10,i);
                g.fillText(String.valueOf(i),i,515);
            }
        g.stroke();



      new Circle(0,0,20).draw(g,0);
      new Circle(0,50,20).draw(g,0);
        CcSprite ccSprite = new CcSprite(crate,40,40);
        ccSprite.draw(g,0);

        CcSprite ccSprite2 = new CcSprite(crate,60,60);
        ccSprite2.x = 100;

        ccSprite2.draw(g,0);
    }

    class Circle implements Drawable{
        private final double radius;
        private final double y;
        private final double x  ;

        public Circle(double x, double y, double radius){
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

       public  void draw(GraphicsContext context,long time){
            context.fillOval(x,y,radius,radius);
            context.fill();
            context.fillText("1",0,0);
        }
    }

    class CcNode implements Drawable{

        double x;
        double y;
        double width;
        double height;
        double scaleX  = 1;
        double scaleY  = 1;
        double angle;
        List<CcNode> children = new ArrayList<>();

        public void addChild(CcNode node){
            children.add(node);
        }
        @Override
        public void draw(GraphicsContext context, long time) {
            context.save();
            context.translate(x, y);
            context.rotate(angle);
            context.scale(scaleX,scaleY);
            rasterize(context);
            context.restore();

        }

        public void rasterize(GraphicsContext context){

        }
    }

    class CcSprite extends CcNode{
        private Image image;
        public CcSprite(Image image){
            this.image = image;
            width =  image.getWidth();
            height = image.getHeight();
        }

        public CcSprite(Image image,double width,double height){
            this.image = image;
            this.width =  width;
            this.height = height;
        }

        @Override
        public void rasterize(GraphicsContext context) {
            context.drawImage(image,0,0,width,height);
        }
    }
    class Grid implements Drawable{
        @Override
        public void draw(GraphicsContext context, long time) {

        }
    }

}
