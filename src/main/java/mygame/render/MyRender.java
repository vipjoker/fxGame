package mygame.render;


import mygame.demo.Picture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Makhobey Oleh on 6/8/16.
 * email: tajcig@ya.ru
 */
public class MyRender  {
    File file ;
    private int HEIGHT = 100;
    private int WIDTH = 100;
    String suffix = "jpg";
    BufferedImage image;
    public static void main(String[] args) {
        MyRender myRender = new MyRender();
        myRender.createImage();
        myRender.drawLine(0,0,50,50);
        myRender.save(myRender.file,myRender.image,myRender.suffix);
    }


    public void drawLine(int x1, int y1, int x2,int y2){
        int height = y2 - y1;
        int width = x2 - x1;
        for(int x =x1 ; x < x2 ; x++){

            int deltaY =    y1 +  height *(x - x2);

            drawDot(x,deltaY);
        }
    }

    public void drawDot(int x,int y){
        image.setRGB(x,y, Color.WHITE.getRGB());
    }

    public  void createImage(){
        String path = "myImage.jpg";

        try {
            file =  new File(path);
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("No such file");
            e.printStackTrace();
        }
        image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);

    }

    public  void save(File file,BufferedImage image,String suffix) {

            try {
                ImageIO.write(image, suffix, file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }



}
