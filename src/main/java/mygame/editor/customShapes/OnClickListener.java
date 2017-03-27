package mygame.editor.customShapes;

import mygame.editor.model.Point;

/**
 * Created by oleh on 3/26/17.
 */
public interface OnClickListener extends Intersectable {
    void onClick(Point point);
}
