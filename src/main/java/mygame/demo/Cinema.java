package mygame.demo;

import javafx.application.Platform;
import javafx.scene.text.Text;

/**
 * Created by Admin on 30.05.2016.
 */
public class Cinema implements Runnable {
    private Text mText;


    public Cinema(Text text){
        mText=text;
    }
    int counter = 0;
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Platform.runLater(()->mText.setText(String.valueOf(counter++)));
        }
    }
}
