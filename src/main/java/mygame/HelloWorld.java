package mygame;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloWorld extends Application{

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Canvas Example");

        Group root = new Group();

        Scene theScene = new Scene(root);

        primaryStage.setScene(theScene);

        Canvas canvas = new Canvas(400,200);

        root.getChildren().add(canvas);

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

        graphicsContext2D.setFill(Color.RED);
        graphicsContext2D.setStroke(Color.BLACK);

        graphicsContext2D.setLineWidth(2);

        Font font  = Font.font("Times New Roman", FontWeight.BOLD,48);
        graphicsContext2D.setFont(font);
        graphicsContext2D.fillText("Hello World",60,80);
        graphicsContext2D.strokeText("Hello World", 60,50);
        primaryStage.show();


        Interpolator easeIn = Interpolator.EASE_BOTH;






        final String content = "Lorem ipsum hello world";
        final Text text = new Text(10, 150, "");
        root.getChildren().add(text);
        final Animation animation = new Transition() {
                     {
                         setCycleDuration(Duration.millis(2000));

                     }

                         protected void interpolate(double frac) {
                         final int length = content.length();
                         final int n = Math.round(length * (float) frac);
                         text.setText(content.substring(0, n));
                     }

                     };

         animation.play();


        Rectangle rectangle = new Rectangle(100,100,50,50);
        root.getChildren().add(rectangle);

        Transition a = new Transition(){
            {
                setCycleDuration(Duration.millis(1000));
                setAutoReverse(true);
                setCycleCount(2);
                setInterpolator(Interpolator.SPLINE(0.170, 0.470, 0.170, 1.0));
            }

            @Override
            protected void interpolate(double frac) {
                rectangle.setX(frac*100);
            }
        };
        a.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        a.play();

    }


}
