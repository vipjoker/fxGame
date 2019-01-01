package mygame.editor.model;

import javafx.scene.control.cell.TextFieldTreeCell;
import mygame.editor.views.NodeView;

public class TreeNodeHolder extends TextFieldTreeCell<NodeView> {
    @Override
    public void updateItem(NodeView item, boolean empty) {

        super.updateItem(item, empty);
        if(item!=null){
            setText(item.getName().getValue());
        }
    }
}
