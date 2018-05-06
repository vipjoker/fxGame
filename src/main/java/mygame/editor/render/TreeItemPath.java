package mygame.editor.render;

import javafx.scene.control.cell.TextFieldTreeCell;
import java.nio.file.Path;

public class TreeItemPath extends TextFieldTreeCell<Path>{
    @Override
    public void updateItem(Path item, boolean empty) {
        super.updateItem(item, empty);
        if(item!=null){
            setText(item.toFile().getName());
        }
    }
}
