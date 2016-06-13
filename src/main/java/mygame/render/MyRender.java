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
        myRender.drawLine(0,0,30,50);
        myRender.drawLine(30,50,10,50);
//        myRender.drawLine(10,50,0,0);
        myRender.save(myRender.file,myRender.image,myRender.suffix);
    }


    public void drawLine(int x1, int y1, int x2,int y2){
        int startPointX = Math.min(x1,x2);
        int startPointY = Math.min(y1,y2);
        int endPointX = Math.max(x1,x2);
        int endPointY = Math.max(y1,y2);

        for(int x = startPointX ; x < endPointX; x++){

            double width = endPointX - startPointX;
            double height =endPointY  - startPointY;

            double fraction = width / height;
            double fractionDelta = (x-startPointX) * fraction;

            double y =  startPointX + fractionDelta;

            if(y1 == y2)y = y1;
            drawDot(x,(int)y);
        }
    }

    public void drawDot(int x,int y){
        image.setRGB(x,y, Color.GREEN.getRGB());
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
