package mygame.level;

import javafx.scene.ParallelCamera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import mygame.Main;
import mygame.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Makhobey Oleh on 6/7/16.
 * email: tajcig@ya.ru
 */
public class SimpleLevel implements Renderable {

    private int screenWidth = 500;
    private int cameraX = 0;
    private int cameraY = 400;

    List<Image> images = new ArrayList<>();
    List<Tile> tiles;
    public SimpleLevel (){
        for(int i = 0 ; i < 18; i++)
            images.add(new Image(getClass().getResourceAsStream("/background/Tiles/" + (i + 1) + ".png")));

        initLevel();
    }


    @Override
    public void draw(GraphicsContext context) {

        //context.fillText(String.valueOf(counter++),100,100);

        if(Main.buttons.contains(KeyCode.D))cameraX-=3;
        if(Main.buttons.contains(KeyCode.A))cameraX+=3;


       for(Tile tile : tiles){
          // if(tile.getX() >  screenWidth + cameraX) break;
           context.drawImage(images.get(tile.getPosition()),
                   tile.getX() + cameraX,
                   tile.getY(),
                   tile.getWidth(),
                   tile.getHeight());


       }

    }

    private void initLevel(){
        tiles = new ArrayList<>();

        tiles.add(new Tile(  0,cameraY,100,100,0));
        tiles.add(new Tile(100,cameraY,100,100,1));
        tiles.add(new Tile(200,cameraY,100,100,1));
        tiles.add(new Tile(300,cameraY,100,100,1));
        tiles.add(new Tile(400,cameraY,100,100,1));
        tiles.add(new Tile(500,cameraY,100,100,1));
        tiles.add(new Tile(600,cameraY,100,100,1));
        tiles.add(new Tile(700,cameraY,100,100,1));
        tiles.add(new Tile(800,cameraY,100,100,1));
        tiles.add(new Tile(900,cameraY,100,100,2));
    }

}
