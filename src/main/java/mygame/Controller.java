package mygame;

import javafx.animation.Animation;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import mygame.action.GameAction;
import mygame.person.*;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable,GameAction {
    public ImageView ivBackground,ivSprite;

    private final Image image = new Image(getClass().getResourceAsStream("/sprites.jpg"));
    private final Image imageback = new Image(getClass().getResourceAsStream("/background.png"));
    private SpriteAnimation animation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivSprite.setImage(image);
        ivBackground.setImage(imageback);
        PersonState state = new StandingState();
        animation = new SpriteAnimation(ivSprite, state, Duration.millis(500));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

    }


    @Override
    public void moveLeft() {
        animation.goLeft();
        ivSprite.setX(ivSprite.getX() - 5);
    }

    @Override
    public void moveRight() {
        animation.goRight();
        ivSprite.setX(ivSprite.getX() + 5);
    }

    @Override
    public void jump() {
        animation.setState(new JumpingState());
    }

    @Override
    public void crouch() {
        animation.setState(new AttackState());
    }

    @Override
    public void run() {
        animation.setState(new RunningState());
    }
}
