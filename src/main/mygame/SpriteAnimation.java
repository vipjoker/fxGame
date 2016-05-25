package main.mygame;

import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


/**
 * Created by oleh on 25.05.16.
 */
public class SpriteAnimation  extends Transition {

    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int height;
    private final int width;


    public SpriteAnimation(ImageView imageView,
                           Duration duration,
                           int count,
                           int columns,
                           int offsetX,
                           int offsetY,
                            int width,
                           int height){
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.height = height;
        this.width = width;
        setCycleDuration(duration);

    }


    @Override
    protected void interpolate(double k) {




        final int index = Math.min((int) Math.floor(k *count), count -1);
        final int x = (index%columns) * width + offsetX;
        final int y = (index/columns) * height + offsetY;
        imageView.setViewport(new Rectangle2D(x,y,width,height));
    }
}
