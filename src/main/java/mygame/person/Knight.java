package mygame.person;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import mygame.Renderable;
import mygame.person.PersonState;
import mygame.util.Constants;


/**
 * Created by oleh on 25.05.16.
 */
public class Knight implements Renderable{

    private PersonState ATTACK = new AttackState();
    private PersonState DEAD = new DeadState();
    private PersonState JUMP_ATTACK = new JumpAttackState();
    private PersonState JUMP = new  JumpingState();
    private PersonState RUNNING = new RunningState();
    private PersonState STANDING = new StandingState();
    private PersonState WALKING = new WalkState();

    private PersonState mState;


    private long initTime;
    private long lastTime;
    private int duration;
    private int jump = 0 ;
    private boolean isJumping;


    public Knight() {
        mState = new StandingState();
        initTime = System.currentTimeMillis();
        this.duration = 1000;
    }

    public void update(long time) {

    }


    public void attack() {
        if(!isJumping) mState = ATTACK;
        else mState = JUMP_ATTACK;
    }

    public void move() {
        if(!isJumping)mState = WALKING;
    }


    public void crouch() {
        if(!isJumping)mState =DEAD;
    }

    public void standing(){
        if(!isJumping)mState  = STANDING;
    }

    public void jump() {

        if(!isJumping){
            isJumping = true;
            animateJump();
        }
        mState = JUMP;
    }

    @Override
    public void draw(GraphicsContext context) {




        long tmp = System.currentTimeMillis();

        long result  = tmp - lastTime;
        context.fillText("FPS "  +1000 /(result ==0 ? 1 : result), Constants.WIDTH - 70, 50);
        lastTime = tmp;




        mState.animate(state, 400 - jump, context);
    }

    private void animateJump(){
        Transition a = new Transition(){
            {
                setCycleDuration(Duration.millis(1000));
                setAutoReverse(true);
                setCycleCount(2);
                setInterpolator(Interpolator.SPLINE(0.170, 0.470, 0.170, 1.0));
            }

            @Override
            protected void interpolate(double frac) {
                jump =(int) (frac*100);
                System.out.println("works" + jump);
            }
        };
        a.setOnFinished(event -> isJumping = false);
        a.play();

    }

    public void run() {
        mState = RUNNING;
    }
}
