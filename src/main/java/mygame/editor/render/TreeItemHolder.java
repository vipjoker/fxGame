package mygame.editor.render;

import javafx.scene.control.cell.TextFieldTreeCell;
import mygame.editor.controlers.HierarchyController;
import mygame.editor.model.TreeFileHolder;
import mygame.editor.views.CcNode;

public class TreeItemHolder extends TextFieldTreeCell<HierarchyController.Holder> {
    @Override
    public void updateItem(HierarchyController.Holder item, boolean empty) {
        super.updateItem(item, empty);
        if(item!=null){
            setText(item.name);
        }
    }
}
