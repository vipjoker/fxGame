package mygame.level;

import javafx.scene.canvas.GraphicsContext;
import mygame.Renderable;
import mygame.util.Constants;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 * Created by Makhobey Oleh on 6/7/16.
 * email: tajcig@ya.ru
 */
public class SimpleFPS implements Renderable {

    private long initTime;
    private long lastTime;

    public SimpleFPS (){
        initTime = System.currentTimeMillis();

    }
    @Override
    public void draw(GraphicsContext context) {
        long tmp = System.currentTimeMillis();

        long l = tmp - initTime;
        long result  = tmp - lastTime;
        context.fillText("FPS "  +1000 /(result ==0 ? 1 : result), Constants.WIDTH - 70, 50);
        context.fillText("Time " + (l / 1000), Constants.WIDTH - 70, 80);
        lastTime = tmp;
    }
}
