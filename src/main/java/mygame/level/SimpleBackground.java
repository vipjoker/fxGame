package mygame.level;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import mygame.Main;
import mygame.Renderable;
import mygame.util.Constants;
import mygame.util.ImageUtil;

/**
 * Created by Makhobey Oleh on 6/7/16.
 * email: tajcig@ya.ru
 */
public class SimpleBackground implements Renderable {
    private int x;
    Image mBackground;
    public SimpleBackground(){
        mBackground = ImageUtil.getImage("background.png");
    }

    @Override
    public void draw(GraphicsContext context) {
        if(Main.buttons.contains(KeyCode.D))x--;
        if(Main.buttons.contains(KeyCode.A))x++;
        if(x <= -Constants.WIDTH || x >= Constants.WIDTH)x = 0;

        context.drawImage(mBackground,
                x,
                0,
                Constants.WIDTH,
                Constants.HEIGHT);


        context.drawImage(mBackground,
                            x<0? x+ Constants.WIDTH : x-Constants.WIDTH,
                            0,
                            Constants.WIDTH,
                            Constants.HEIGHT);


    }
}
