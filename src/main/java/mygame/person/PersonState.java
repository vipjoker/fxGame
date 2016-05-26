package mygame.person;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Admin on 27.05.2016.
 */
public abstract class PersonState {
    protected Image[] frames;

    public PersonState (String... paths){
        frames = new Image[paths.length];

        for(int i = 0 ; i < paths.length; i++)
            frames[i] =new Image(getClass().getResourceAsStream(paths[i]));
    }

   public final  void animate (double anim , ImageView imageView){

           double factor = 1.0/(frames.length -1);
           int index =(int) Math.floor(anim/factor) ;

           imageView.setImage(frames[index]);
       }
}
