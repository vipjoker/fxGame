package mygame.demo;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.DrawMode;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import mygame.effects.ShadowEffect;

public class HelloWorld extends Application implements TimerCounter.FrameRateCallback{

    public static void main(String[] args) {
        launch(args);
    }
    private GraphicsContext g;
    private AnimationTimer timer;
    private Text text;
    private Text text2;
    private Text text3;
    private double x = 0;
    double offsetX = 0;
    double offsetY = 0;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Canvas Example");
        Group group = new Group();
        Scene scene = new Scene(group,800,800);
        scene.setFill(Color.WHITE);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(800,800);
        g = canvas.getGraphicsContext2D();

        timer = new TimerCounter(this);

        timer.start();
        g.setFill(Color.RED);
        g.stroke();
        text = new Text();
        text2 = new Text();
        text.setFill(Color.GRAY);
        text2.setFill(Color.GRAY);
        text.setX(10);
        text.setY(10);

        text2.setX(10);
        text2.setY(30);

        text3 = new Text();
        text3.setFill(Color.GRAY);
        text3.setX(10);
        text3.setY(50);
        Image image = new Image(getClass().getResourceAsStream("/earth.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setX(50);
        imageView.setY(50);
        Circle circle = new Circle(60, 60, 20);
        circle.setFill(Color.GREEN);
        circle.setOnMouseDragged(et->{
            ((Circle) et.getTarget()).setCenterX(et.getX());
            ((Circle) et.getTarget()).setCenterY(et.getY());

        });


        imageView.setOnMousePressed(e->{
            offsetX = e.getX();
            offsetY = e.getY();
            System.out.println("start");
        });

        imageView.setOnMouseDragged(e->{
            imageView.setX(-offsetX + e.getX() );
            imageView.setY(-offsetY + e.getY());
        });


        group.getChildren().addAll(canvas,text,text2,text3,circle,imageView);

        primaryStage.show();

    }

    @Override
    public void update(long delta){
        text3.setText("" + delta + " s");

        g.clearRect(0,0,800,800);
        g.fillOval(x += 0.1,50,10,10);
    }

    @Override
    public void seconds(long seconds) {
        text.setText("" + seconds + " s");
    }

    @Override
    public void millis(long millis) {
        text2.setText("" + millis + " s");
    }

    @Override
    public void stop() throws Exception {
        timer.stop();
        super.stop();
    }

    private void line(double x, double y, double x1, double y1){
        g.moveTo(x,y);
        g.lineTo(x1,y1);
    }

    private void circle(double x,double y,double radius){

    }

}
