package mygame.editor.customShapes;

import mygame.editor.model.Point;

/**
 * Created by oleh on 3/26/17.
 */
public interface OnDragListener extends Intersectable{
    void onDrag(Point point);
}
