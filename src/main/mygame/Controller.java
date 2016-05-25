package main.mygame;

import javafx.animation.Animation;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import main.mygame.action.GameAction;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable,GameAction {
    public ImageView ivBackground,ivSprite;

    private final Image image = new Image(getClass().getResourceAsStream("sprites.jpg"));
    private final Image imageback = new Image(getClass().getResourceAsStream("background.png"));
    private static final int COLUMNS = 5;
    private static final int COUNT = 5;
    private static final int OFFSET_X = 25;
    private static final int OFFSET_Y = 450;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;


    Rectangle2D rect2d;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivSprite.setImage(image);
        ivBackground.setImage(imageback);
        rect2d = new Rectangle2D(OFFSET_X,OFFSET_Y,WIDTH,HEIGHT);
        ivSprite.setViewport(rect2d);

        final Animation animation = new SpriteAnimation(ivSprite,
                                                        Duration.millis(3000),
                COUNT,COLUMNS,OFFSET_X,OFFSET_Y,WIDTH,HEIGHT);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();






    }

    @Override
    public void moveLeft() {
        ivSprite.setX(ivSprite.getX() - 5);
    }

    @Override
    public void moveRight() {
        ivSprite.setX(ivSprite.getX() + 5);
    }

    @Override
    public void jump() {

    }

    @Override
    public void crouch() {

    }

    @Override
    public void run() {

    }
}
