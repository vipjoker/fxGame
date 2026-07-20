package mygame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Editable JavaFX TreeView with drag-and-drop node reordering AND
 * reparenting.
 *
 * Unlike Swing's JTree, JavaFX has no built-in DropMode / automatic drop
 * indicator painting, so this is done manually inside a custom TreeCell:
 *
 *  - Hovering over the TOP quarter of a cell  -> insert ABOVE it as a
 *    sibling -> a highlighted LINE is drawn at the top border of the cell
 *    (your "reordering" case).
 *  - Hovering over the BOTTOM quarter of a cell -> insert BELOW it as a
 *    sibling -> a highlighted LINE is drawn at the bottom border
 *    (also "reordering").
 *  - Hovering over the MIDDLE half of a cell -> drop INTO that node as a
 *    new child -> the whole cell is highlighted with a background color
 *    (your "reparenting" case).
 */
public class Test extends Application {

    @Override
    public void start(Stage stage) {
        TreeItem<String> root = buildSampleTree();
        root.setExpanded(true);

        TreeView<String> treeView = new TreeView<>(root);
        treeView.setEditable(true);
        treeView.setCellFactory(tv -> new DnDCell());

        BorderPane pane = new BorderPane(treeView);
        pane.setPadding(new Insets(8));

        stage.setScene(new Scene(pane, 420, 480));
        stage.setTitle("Editable Drag & Drop TreeView");
        stage.show();
    }

    private TreeItem<String> buildSampleTree() {
        TreeItem<String> root = new TreeItem<>("Root");
        TreeItem<String> a = new TreeItem<>("Branch A");
        TreeItem<String> b = new TreeItem<>("Branch B");
        a.getChildren().addAll(new TreeItem<>("A-1"), new TreeItem<>("A-2"));
        b.getChildren().add(new TreeItem<>("B-1"));
        root.getChildren().addAll(a, b, new TreeItem<>("Leaf C"));
        a.setExpanded(true);
        b.setExpanded(true);
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * TreeCell that supports inline editing (via TextFieldTreeCell) plus
     * drag-and-drop move/reorder/reparent within the same TreeView.
     */
    static class DnDCell extends TextFieldTreeCell<String> {

        // Shared across all cells of this TreeView during a single drag
        // gesture. JavaFX Dragboard content must be Strings/files/etc, so we
        // just track the actual TreeItem being dragged via this reference
        // instead of trying to serialize it through the Dragboard.
        private static TreeItem<String> draggedItem;

        private static final String STYLE_ABOVE =
                "-fx-border-color: #2196F3; -fx-border-width: 2 0 0 0;";
        private static final String STYLE_BELOW =
                "-fx-border-color: #2196F3; -fx-border-width: 0 0 2 0;";
        private static final String STYLE_INTO =
                "-fx-background-color: #BBDEFB;";

        DnDCell() {
            setOnDragDetected(this::onDragDetected);
            setOnDragOver(this::onDragOver);
            setOnDragExited(e -> clearDropStyle());
            setOnDragDropped(this::onDragDropped);
            setOnDragDone(e -> {
                clearDropStyle();
                draggedItem = null;
            });
        }

        private void onDragDetected(MouseEvent event) {
            if (getItem() == null || getTreeItem() == null) return;
            draggedItem = getTreeItem();

            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(getItem()); // required for the OS to start a drag
            db.setContent(content);
            event.consume();
        }

        private void onDragOver(DragEvent event) {
            TreeItem<String> target = getTreeItem();
            if (draggedItem == null || target == null || !isValidTarget(draggedItem, target)) {
                clearDropStyle();
                return;
            }
            event.acceptTransferModes(TransferMode.MOVE);

            double h = getHeight();
            double y = event.getY();
            if (y < h * 0.25) {
                setStyle(STYLE_ABOVE);
            } else if (y > h * 0.75) {
                setStyle(STYLE_BELOW);
            } else {
                setStyle(STYLE_INTO);
            }
            event.consume();
        }

        private void onDragDropped(DragEvent event) {
            TreeItem<String> target = getTreeItem();
            boolean success = false;

            if (draggedItem != null && target != null && isValidTarget(draggedItem, target)) {
                double h = getHeight();
                double y = event.getY();

                TreeItem<String> oldParent = draggedItem.getParent();
                if (oldParent != null) {
                    oldParent.getChildren().remove(draggedItem);
                }

                if (y < h * 0.25) {
                    insertRelativeTo(target, draggedItem, 0);   // above
                } else if (y > h * 0.75) {
                    insertRelativeTo(target, draggedItem, 1);   // below
                } else {
                    target.setExpanded(true);
                    target.getChildren().add(draggedItem);      // into, as last child
                }
                success = true;
            }

            event.setDropCompleted(success);
            clearDropStyle();
            draggedItem = null;
            event.consume();
        }

        /** offset 0 = insert before target, offset 1 = insert after target. */
        private void insertRelativeTo(TreeItem<String> target, TreeItem<String> node, int offset) {
            TreeItem<String> parent = target.getParent();
            if (parent == null) {
                // target is the root; there's nothing to be a sibling of, so
                // fall back to dropping the node into the root instead.
                target.setExpanded(true);
                target.getChildren().add(node);
                return;
            }
            int idx = parent.getChildren().indexOf(target);
            parent.getChildren().add(idx + offset, node);
        }

        private boolean isValidTarget(TreeItem<String> dragged, TreeItem<String> target) {
            if (dragged == target) return false;
            for (TreeItem<String> p = target; p != null; p = p.getParent()) {
                if (p == dragged) return false; // can't drop a node into its own subtree
            }
            return true;
        }

        private void clearDropStyle() {
            setStyle("");
        }
    }
}