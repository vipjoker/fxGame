package mygame.editor.render;

import javafx.scene.control.cell.TextFieldTreeCell;
import mygame.editor.model.TreeFileHolder;


public class TreeItemPath extends TextFieldTreeCell<TreeFileHolder>{
    @Override
    public void updateItem(TreeFileHolder item, boolean empty) {
        super.updateItem(item, empty);
        if(item!=null){
            setText(item.getName());
        }
    }
}
