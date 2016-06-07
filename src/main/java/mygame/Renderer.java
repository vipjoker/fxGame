package mygame;

import javafx.application.Platform;

/**
 * Created by Admin on 30.05.2016.
 */
public class Renderer extends Thread {

    private boolean IS_PAUSED;
    private Updatable mUpdatable;
    private Object pause = new Object();
    private Object resume = new Object();

    public Renderer (Updatable updatable){
        this.mUpdatable = updatable;
        start();

    }

    public boolean isPaused(){
        return IS_PAUSED;
    }

    public void pause(){
        IS_PAUSED = true;
    }

    public void  unpause(){
        IS_PAUSED = false;
    }

    @Override
    public void run() {
        long l = System.currentTimeMillis();
        while(true){

            try {
                Thread.sleep(1000/100);
                synchronized (this){
                    while (IS_PAUSED){
                      wait();
                    }
                }
                Platform.runLater(()->mUpdatable.mainLoop( System.currentTimeMillis() -l));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
