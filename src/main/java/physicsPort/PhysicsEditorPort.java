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
    PhysicsEditor editor;
    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        editor = new PhysicsEditor();
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






    function init(){
        var canvas = document.getElementById("canvas");
        canvas.width = window.innerWidth * 0.8;
        canvas.height = window.innerHeight * 0.8;

        // create instance of physics editor
        Editor = new PhysicsEditor(canvas);

        // cached variables
        var viewport = Editor.getViewport(),
                lastElementSelected;

        // to avoid viewport events while editing selection properties
        document.addEventListener("mousedown", function(e){
            lastElementSelected = e.target;
        });

        // key events
        window.addEventListener("keydown", function(e){
            if (lastElementSelected == viewport.canvas)
                viewport.onKeyDown(e);
        });
        window.addEventListener("keyup", function(e){
            if (lastElementSelected == viewport.canvas)
                viewport.onKeyUp(e)
        });
        window.addEventListener("resize", function(e){
            // canvas.width = window.innerWidth * 0.8;
            // canvas.height = window.innerHeight * 0.8;
            // Editor.viewport.getRenderer().setStageWidthHeight(canvas.width, canvas.height);
        });
        window.onbeforeunload = function(){
            return "All Unsaved Changes Would Be Lost";
        }
    }

    // update loop
    function render() {
        Editor.viewport.draw(Editor.getGameView());
        setTimeout(render, 1000.0 / 60.0);		// update at 60 fps
    }
//-------------------------------------------//

    // launch the editor
    init();
    setTimeout(render, 1000.0 / 60.0);

}
