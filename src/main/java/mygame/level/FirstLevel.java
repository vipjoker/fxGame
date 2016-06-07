package mygame.level;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import mygame.Main;
import mygame.person.Knight;
import mygame.person.StandingState;

import java.util.Set;

/**
 * Created by Makhobey Oleh on 27.05.2016.
 email: tajcig@ya.ru
 */
public class FirstLevel {

    private final Image imageback = new Image(getClass().getResourceAsStream("/background.png"));
    private int position;
    private Canvas mCanvas;
    private GraphicsContext gc;
    private Set<KeyCode> buttons;
    private Knight mKnight;

    public FirstLevel(Canvas canvas) {
        this.mCanvas = canvas;
        init();
    }

    public void init() {

        gc = mCanvas.getGraphicsContext2D();
        mKnight = new Knight(gc, new StandingState(), 1000);
        render();

    }

    private void render() {
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                handleButtons();
                gc.drawImage(imageback, position       , 0, 1280, 720);
                gc.drawImage(imageback, position + 1280, 0, 1280, 720);
                gc.drawImage(imageback, position - 1280, 0, 1280, 720);
                mKnight.update(now);


            }

        }.start();
    }


    private void handleButtons() {

        if (buttons.contains(KeyCode.SHIFT)) {
            if (buttons.contains(KeyCode.A)) {
                position += 10;

                mKnight.run();
            } else if (buttons.contains(KeyCode.D)) {
                position -= 10;
                mKnight.run();
            }
        } else {
            if (buttons.contains(KeyCode.A)) {
                position += 5;
                mKnight.move();
            } else if (buttons.contains(KeyCode.D)) {
                position -= 5;
                mKnight.move();
            }
        }


        if (buttons.contains(KeyCode.S)) {
            mKnight.crouch();
        }
        if (buttons.contains(KeyCode.W)) {
            mKnight.jump();
        }


        if (buttons.contains(KeyCode.SPACE)) {
            mKnight.attack();
        }
        if (buttons.isEmpty()) {
            mKnight.standing();
        }


        if (position > 1280 || position < -1280) position = position % 1280;
    }

}


