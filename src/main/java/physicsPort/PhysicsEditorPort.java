package physicsPort;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mygame.editor.TimerCounter;
import mygame.editor.util.Constants;

public class PhysicsEditorPort extends Application implements TimerCounter.FrameRateCallback {

    private GraphicsContext context;
    private int width = 800;
    private int height = 800;

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(width, height);
        primaryStage.setScene(new Scene(new HBox(canvas)));

        context = canvas.getGraphicsContext2D();

        TimerCounter timerCounter = new TimerCounter(this);
        timerCounter.start();
        primaryStage.show();
    }

    @Override
    public void update(long delta) {
        context.clearRect(0, 0, width, height);
        context.setFill(Constants.BACKGROUND);
        context.fillRect(0, 0, width, height);
        context.setFill(Constants.WHITE);
        context.fillOval(100,100,100,100);

    }
}
