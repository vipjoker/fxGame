package mygame;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Admin on 30.05.2016.
 */
public class Cube implements Renderable {

    private int width,height,x,y;
    public Cube(int width,int height, int x, int y){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;


    }

    @Override
    public void draw(GraphicsContext context) {

        context.fillRoundRect(x,y,width,height ,10,10);
        x++;
    }
}
