package mygame.editor.util;

import com.badlogic.gdx.math.Vector2;

public class Box2dUtil {

    public static Vector2 fromBox2d(Vector2 point){
        final Vector2 copy = point.cpy();
        copy.y *= -1;
        copy.scl(32);
        return copy;
    }

    public static Vector2 toBox2d(Vector2 point){
        final Vector2 copy = point.cpy();
        copy.y *= -1;
        copy.scl(1/32.0f);
        return copy;
    }

}
