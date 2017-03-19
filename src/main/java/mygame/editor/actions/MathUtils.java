package mygame.editor.actions;

/**
 * Created by oleh on 3/18/17.
 */
public class MathUtils {
    public static int countDelta(int startPoint, int endPoint){
        return startPoint +(endPoint - startPoint);
    }
    public static float countDelta(float startPoint, float endPoint){
        return startPoint + (endPoint - startPoint);
    }
}
