package mygame.editor;

import com.badlogic.gdx.math.Vector2;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class PanListener {
    PanDelegateListener mListener;
    private double tX;
    private double tY;
    private double scale= 1;

    public double gettX() {
        return tX;
    }

    public double gettY() {
        return tY;
    }

    public double getScale() {
        return scale;
    }

    public PanListener(PanDelegateListener listener) {
        this.mListener = listener;
    }

    Vector2 startEvent;
    void panCanvasReleased(MouseEvent event) {
        if(startEvent!= null) {
            tX = (event.getX() - startEvent.x);
            tY = (event.getY() - startEvent.y);
            startEvent = null;
        }

    }

    void panCanvasPressed(MouseEvent event) {
        if(startEvent == null){
            startEvent = new Vector2((float) event.getX(),(float) event.getY());
        }
    }

    void panCanvasDragged(MouseEvent event) {
        if(startEvent != null){
            mListener.onPanMoved(tX + (event.getX() -startEvent.x),tY + (event.getY() -startEvent.y),scale);
        }
    }

    void onScroll(ScrollEvent event){
        System.out.println(event.getDeltaY());
    }


}