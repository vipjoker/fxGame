package physicsPort;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import javafx.application.Application;
import javafx.event.EventTarget;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mygame.editor.TimerCounter;
import mygame.editor.util.Constants;

public class PhysicsEditorPort extends Application implements TimerCounter.FrameRateCallback {

    private GraphicsContext context;
    private int width = 800;
    private int height = 800;
    private float duration  = 200;
    Vector2 pos = new Vector2(10,10);
    Vector2 target = new Vector2(10,10);
    AnimationTask task ;
    private long lastTime;
    private long targetTime;
    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        // create instance of physics editor
        Button playBtn = new Button("Play");
        Button pauseBtn = new Button("Pause");
        Button stepBtn = new Button("Step");


        HBox hBox = new HBox( playBtn,pauseBtn,stepBtn);
        Canvas canvas = new Canvas(width, height);
        Scene scene = new Scene(new VBox(hBox,canvas));
        primaryStage.setScene(scene);

        context = canvas.getGraphicsContext2D();




        // cached variables


        // to avoid viewport events while editing selection properties

        scene.setOnKeyPressed(e->{

        });

        scene.setOnKeyReleased(e->{

        });



        canvas.setOnMousePressed(e->{
            target.x = (float) e.getX();
            target.y = (float) e.getY();
            task = new AnimationTask(Interpolator.EASEINOUTCIRC,2000);
        });

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
        context.fillText("Delta : " + delta,100,100);


        if(task!= null && !task.isFinished()){
            final float animate = task.animate();
            context.fillText("Delta : " + animate,width/2,height/2);

            pos.x = animate * 100;
            pos.y = animate * 100;
            context.fillOval(pos.x - 25,pos.y - 25,50,50);
        }

        lastTime = delta;



    }


}
