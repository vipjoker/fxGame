package mygame.editor;

import com.badlogic.gdx.math.Vector2;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class PanListener {
    final PanDelegateListener mListener;
    final ScaleListener scaleListener;




    public PanListener(PanDelegateListener listener,ScaleListener scaleListener) {
        this.mListener = listener;
        this.scaleListener = scaleListener;
    }

    Vector2 startEvent;
    Vector2 lastEvent;
    void panCanvasReleased(MouseEvent event) {


        startEvent = null;
        lastEvent = null;

    }

    void panCanvasPressed(MouseEvent event) {
        if(startEvent == null){
            startEvent = new Vector2((float) event.getX(),(float) event.getY());
        }
    }

    void panCanvasDragged(MouseEvent event) {
        if(lastEvent != null){
            mListener.onPanMoved( (event.getX() -lastEvent.x), (event.getY() -lastEvent.y));
            lastEvent.set((float)event.getX(),(float)event.getY());
        }else{
            lastEvent = new Vector2((float) event.getX(),(float) event.getY());
        }
    }

    void onScroll(ScrollEvent event){
        scaleListener.onScale(event.getDeltaY());
    }


}