package physicsPort;

import com.badlogic.gdx.math.Vector2;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import mygame.editor.TimerCounter;
import mygame.editor.util.Constants;
import physicsPort.screens.ScreenOne;

import java.io.InputStream;

public class PhysicsEditorPort extends Application implements TimerCounter.FrameRateCallback {

    private GraphicsContext context;
    private int width = 800;
    private int height = 800;
    Vector2 pos = new Vector2(10, 10);
    private long lastTime;
    private long targetTime;
    double[] lastPoint = {0,0};
    private Screen screen = new ScreenOne();
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        Canvas canvas = new Canvas();
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        scene.widthProperty().addListener((observable, oldValue, newValue) -> width = newValue.intValue());
        scene.heightProperty().addListener((observable, oldValue, newValue) -> height = newValue.intValue());

        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());


        context = canvas.getGraphicsContext2D();
        context.setTextAlign(TextAlignment.LEFT);
        InputStream resourceAsStream = PhysicsEditor.class.getResourceAsStream("/assets/Fonts/neuropol.ttf");
        Font loadFont = Font.loadFont(resourceAsStream, 30);
        context.setFont(loadFont);



        scene.setOnKeyPressed(e -> {

        });

        scene.setOnKeyReleased(e -> {

        });


        canvas.setOnMousePressed(e -> {
            screen.touchX = (float) e.getX();
            screen.touchY = (float) e.getY();
            screen.isTouched = true;
        });

        canvas.setOnMouseReleased(e->{
            screen.touchX = (float) e.getX();
            screen.touchY = (float) e.getY();
            screen.isTouched = false;
        });

        TimerCounter timerCounter = new TimerCounter(this);
        timerCounter.start();
        screen.init();
        primaryStage.show();


    }

    @Override
    public void update(long delta) {
        context.clearRect(0, 0, width, height);
        context.setFill(Constants.BACKGROUND);
        context.fillRect(0, 0, width, height);
        context.setFill(Constants.WHITE);
        context.fillText("Delta : " + delta, 20, 20 + 30);
        screen.setSize(width,height);
        screen.draw(context);
        lastTime = delta;
    }
}
