package mygame.editor.model;

import javafx.scene.control.cell.TextFieldTreeCell;
import mygame.editor.views.CcNode;

public class TreeNodeHolder extends TextFieldTreeCell<CcNode> {
    @Override
    public void updateItem(CcNode item, boolean empty) {

        super.updateItem(item, empty);
        if(item!=null){
            setText(item.getName().getValue());
        }
    }
}
