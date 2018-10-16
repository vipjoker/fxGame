package mygame.editor.model.box2d;

/**
 * Created by oleh on 17.04.18.
 */
public class B2Point {
    private float x;
    private float y;

    public B2Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }


    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

