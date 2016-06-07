package mygame.util;

import javafx.scene.image.Image;

/**
 * Created by Makhobey Oleh on 6/7/16.
 * email: tajcig@ya.ru
 */
public class ImageUtil {

    public static Image getImage(String path){
       return new Image(ImageUtil
               .class
               .getResourceAsStream("/" + path));
    }
}
