package mygame.person;

import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import mygame.person.PersonState;


/**
 * Created by oleh on 25.05.16.
 */
public class Knight {

    private PersonState ATTACK = new AttackState();
    private PersonState DEAD = new DeadState();
    private PersonState JUMP_ATTACK = new JumpAttackState();
    private PersonState JUMP = new  JumpingState();
    private PersonState RUNNING = new RunningState();
    private PersonState STANDING = new StandingState();
    private PersonState WALKING = new WalkState();

    private GraphicsContext gc;
    private PersonState mState;


    final long nanotime = System.nanoTime();

    private int duration;

    public Knight(GraphicsContext gc, PersonState state, int duration) {
        this.gc = gc;
        mState = state;
        this.duration = duration;
    }

    public void update(long time) {

        double t = (time - nanotime) / 1000_000.0;
        double state = (t % duration) / duration;
        mState.animate(state, gc);
    }

    public void attack() {
        mState = ATTACK;
    }

    public void moveLeft() {
        mState = WALKING;
    }

    public void moveRight() {
        mState = WALKING;
    }

    public void crouch() {

    }

    public void jump() {
        mState = JUMP;
    }

    public void run() {
        mState = RUNNING;
    }
}
