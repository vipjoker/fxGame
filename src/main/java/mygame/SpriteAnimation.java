package mygame;

import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import mygame.person.PersonState;


/**
 * Created by oleh on 25.05.16.
 */
public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private PersonState mState;
    public SpriteAnimation(ImageView imageView, PersonState state, Duration duration) {
        this.imageView = imageView;
        mState = state;
        setCycleDuration(duration);


    }

    public void setState(PersonState state){
        this.mState = state;
    }

    public void goLeft(){
        imageView.setScaleX(-1);
    }

    public void goRight(){
        imageView.setScaleX(1);
    }

    @Override
    protected void interpolate(double k) {
        mState.animate(k,imageView);
    }
}
