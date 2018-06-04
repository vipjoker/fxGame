package mygame.editor.render;

import javafx.scene.control.cell.TextFieldTreeCell;
import mygame.editor.model.TreeFileHolder;
import mygame.editor.views.CcNode;

public class TreeItemCcNode extends TextFieldTreeCell<CcNode> {
    @Override
    public void updateItem(CcNode item, boolean empty) {
        super.updateItem(item, empty);
        if(item!=null){
            setText(item.name);
        }
    }
}
