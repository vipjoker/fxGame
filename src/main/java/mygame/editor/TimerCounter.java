package mygame.editor;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/**
 * Created by oleh on 17.04.18.
 */
public class TimerCounter extends AnimationTimer{
    private final FrameRateCallback mCallback;
    private long last = -1;
    private long counter = 0;
    private long lastSecond = 0;
    private long lastMillis = 0;
    private long lastFrame = 0;
    public TimerCounter(FrameRateCallback callback) {
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

    public interface FrameRateCallback{
        default void update(long delta){}
        default void seconds(long seconds){}
        default void millis(long millis){}
    }
}
