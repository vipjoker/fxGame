package mygame.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/**
 * Created by oleh on 17.04.18.
 */
public class TimerCounter extends AnimationTimer{
    private final FrameRateCallback mCallback;
    long last = -1;
    long counter = 0;
    long lastSecond = 0;
    long lastMillis = 0;
    long lastFrame = 0;
    TimerCounter(FrameRateCallback callback) {
        this.mCallback = callback;
    }
    @Override
    public void handle(long now) {
        if(last != -1){
            long diff = now - last;
            counter += diff;

            long millis = counter / (1000 * 1000);
            long seconds = millis/ 1000;
            long frame = millis / (1000/60);


            if(millis != lastMillis){
                lastMillis = millis;
                Platform.runLater(()-> mCallback.millis(lastMillis));

            }

            if(lastSecond != seconds){
                lastSecond = seconds;

                Platform.runLater(() ->mCallback.seconds(lastSecond));
            }

            if(lastFrame != frame){
                lastFrame = frame;
                Platform.runLater(()->mCallback.update(lastFrame));
            }
        }

        last = now;

    }

    interface FrameRateCallback{
        void update(long delta);
        default void seconds(long seconds){}
        default void millis(long millis){}
    }
}
