package mygame.util;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 * Created by oleh on 23.04.18.
 */
public class ColorUtil {
    public static Background background(String color){
        Background background = new Background(new BackgroundFill(Color.valueOf(color), null, null));
        return background;
    }

}
