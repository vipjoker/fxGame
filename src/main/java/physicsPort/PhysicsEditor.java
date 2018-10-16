package physicsPort;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import physicsPort.body.Body;
import physicsPort.body.Joint;
import physicsPort.body.Shape;
import physicsPort.body.Vertex;

import java.util.List;


public class PhysicsEditor {


    private  String resourceDirectory;
    private  SceneManager sceneManager;
    public  ViewPort viewport;
    private  UIManager uiManager;
    private GameView gameView;

    public  PhysicsEditor(Canvas canvas, Button playButton,Button pauseBtn,Button stepButton) {
        this.sceneManager = new SceneManager();
        this.viewport = new ViewPort(canvas, this.sceneManager);
        this.uiManager = new UIManager(this.sceneManager,playButton,pauseBtn,stepButton);


        // directory containing textures
        this.resourceDirectory = "resources/";

        this.uiManager.initialize(this.viewport.getInputHandler());
        this.uiManager.playBackButton = playButton;

        // auto trace image shape generation paramters
//        this.autoTrace = {
//                xSpace : 1.0,
//                ySpace : 1.0,
//                concavity : 20
//	}

        // play back controls //
        PhysicsEditor ref = this;
        uiManager.playBackButton.setOnMouseClicked(event -> {
            if (ref.gameView  != null){
                ref.gameView = null;
                ref.viewport.getInputHandler().inGameMode = false;
              ////  $(this).removeClass("glyphicon-stop").addClass("glyphicon-play");
            }
            else {
                ref.gameView = new GameView(canvas, ref.viewport.getNavigator());
                ref.gameView.setup(ref.sceneManager.exportWorld(),false);
                ref.viewport.getInputHandler().inGameMode = true;
               // $(this).removeClass("glyphicon-play").addClass("glyphicon-stop");
            }
        });
        this.uiManager.pauseButton.setOnMouseClicked(e->{
            if (ref.gameView != null)
                ref.gameView.paused = !ref.gameView.paused;
        });
        this.uiManager.stepButton.setOnMouseClicked(e ->{
            if (ref.gameView != null && ref.gameView.paused)
                ref.gameView.update();
        });
        //////////////////////

        // view controls //

        ///////////////////

        // add event listeners to canvas
        canvas.setOnScroll(e->{
            ref.viewport.onMouseWheel(e);
        });

        canvas.setOnMousePressed(e->{

            ref.viewport.onMouseDown(e);
            ref.uiManager.onMouseDown(ref.viewport.getInputHandler());
        });
        canvas.setOnMouseDragged(e->{

            ref.viewport.onMouseMove(e);
            ref.uiManager.onMouseMove(ref.viewport.getInputHandler());
        });
        canvas.setOnMouseReleased(event -> {
            ref.viewport.onMouseUp(event);
            ref.uiManager.onMouseUp(ref.viewport.getInputHandler());
        });
    };

    public Body cloneBody (Body body){
        Body clone = body.clone();
        this.sceneManager.addBody(clone);
        return clone;
    };

    public Joint cloneJoint (Joint joint,Body cloneBodyA,Body cloneBodyB){
        Joint clone = joint.clone(cloneBodyA, cloneBodyB);
        if (cloneBodyA != null){
            this.sceneManager.addBody(clone.bodyA);
        }
        if (cloneBodyB != null){
            this.sceneManager.addBody(clone.bodyB);
        }
        this.sceneManager.addJoint(clone);
        return clone;
    }

    public List<Body> getSelectedBodies (){
        return this.sceneManager.selectedBodies;
    }

    public List<Joint>getSelectedJoints (){
        return this.sceneManager.selectedJoints;
    }

    public List<Shape>getSelectedShapes (){
        return this.sceneManager.selectedShapes;
    }

    List<Vertex>getSelectedVertices (){
        return this.sceneManager.selectedVertices;
    };

    List<Object>getCurrentSelection (){
        return this.viewport.inputHandler.selection;
    };

    public SceneManager getSceneManager (){
        return this.sceneManager;
    };

    public ViewPort getViewport (){
        return this.viewport;
    };

    public UIManager getUIManager (){
        return this.uiManager;
    };

    GameView getGameView (){
        return this.gameView;
    };

}
