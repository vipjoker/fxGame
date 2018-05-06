package mygame.editor;

import javafx.scene.canvas.GraphicsContext;

import mygame.editor.customShapes.Drawable;
import mygame.editor.util.Constants;
import mygame.editor.views.Global;

/**
 * Created by Makhobey Oleh on 6/7/16.
 * email: tajcig@ya.ru
 */
public class SimpleFPS implements Drawable {

    private long initTime;
    private long lastTime;

    public SimpleFPS (){
        initTime = System.currentTimeMillis();

    }
    @Override
    public void draw(GraphicsContext context,long time) {
        long tmp = System.currentTimeMillis();

        long l = tmp - initTime;
        long result  = tmp - lastTime;
        context.fillText("FPS "  +1000 /(result ==0 ? 1 : result), Global.getHeight() - 70, 50);
        context.fillText("Time " + (l / 1000), Global.getWidth() - 70, 80);
        lastTime = tmp;
    }
}
