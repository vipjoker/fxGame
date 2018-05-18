package mygame.editor.util;

import javafx.scene.image.Image;

public abstract class Resources {
    public static Image imageIcon =  initImage("/icons/image.png");
    public static Image folderImage = initImage("/icons/foder_basic.png");
    public static Image crate = initImage("/background/Object/Crate.png");
    public static Image no_image = initImage("/no_image.png");

    private static  Image initImage(String path){
        return new Image(Resources.class.getResourceAsStream(path));
    }
}
