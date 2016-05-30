package mygame.level;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import mygame.Main;
import mygame.person.Knight;
import mygame.action.GameAction;
import mygame.person.*;

import java.util.Set;

/**
 * Created by Admin on 27.05.2016.
 */
public class FirstLevel {


    private int position;
    private Canvas mCanvas;
    private GraphicsContext gc;
    private Set<KeyCode> buttons;
    private final Image imageback = new Image(getClass().getResourceAsStream("/background.png"));
    private Knight mKnight;


    public FirstLevel(Canvas canvas) {
        this.mCanvas = canvas;
        init();
    }



    public void init() {

        gc = mCanvas.getGraphicsContext2D();
        mKnight = new Knight(gc,new StandingState(),1000);
        render();

    }

    private void render() {
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                handleButtons();
                mCanvas.getGraphicsContext2D().drawImage(imageback, position, 0, 1280, 720);
                mCanvas.getGraphicsContext2D().drawImage(imageback, position + 1280, 0, 1280, 720);
                mCanvas.getGraphicsContext2D().drawImage(imageback, position - 1280, 0, 1280, 720);
                mKnight.update(now);
            }
        }.start();
    }


    boolean isRunning = false;
    private void handleButtons(){

        if(buttons.contains(KeyCode.SHIFT)){
            mKnight.run();
            isRunning = true;
        }else isRunning = false;

        if (buttons.contains(KeyCode.A)) {
            position += (5 * (isRunning ? 2:1));
            mKnight.moveLeft();
        }
        if (buttons.contains(KeyCode.D)) {
            position -= (5 * (isRunning ? 2:1));
            mKnight.moveRight();
        }
        if(buttons.contains(KeyCode.S)){
            mKnight.crouch();
        }
        if(buttons.contains(KeyCode.W)){
            mKnight.jump();
        }


        if(buttons.contains(KeyCode.SPACE)){
            mKnight.attack();
        }



        if (position > 1280 ) position = position%1280;
    }

}


