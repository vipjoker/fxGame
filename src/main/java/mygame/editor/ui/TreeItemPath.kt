package mygame.editor.ui

import javafx.scene.control.TreeItem
import javafx.scene.control.cell.TextFieldTreeCell
import java.nio.file.Path


class TreeItemPath : TextFieldTreeCell<Path>(){
    override fun updateItem(item: Path?, empty: Boolean) {
        super.updateItem(item, empty)
        text = item?.toFile()?.name
    }
}
