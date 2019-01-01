package mygame.editor.render;

import javafx.scene.control.cell.TextFieldTreeCell;
import mygame.editor.controlers.HierarchyController;

public class TreeItemHolder extends TextFieldTreeCell<HierarchyController.Holder> {
    @Override
    public void updateItem(HierarchyController.Holder item, boolean empty) {
        super.updateItem(item, empty);
        if(item!=null){
            setText(item.name);
        }
    }
}
