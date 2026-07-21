package org.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DraggableTreeViewApp extends Application {

    // Custom DataFormat to store the dragged TreeItem reference identifier
    private static final DataFormat TREE_ITEM_FORMAT = new DataFormat("application/x-java-treeitem");
    private static TreeItem<String> draggedItem;

    @Override
    public void start(Stage primaryStage) {
        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);

        TreeItem<String> branchA = new TreeItem<>("Branch A");
        TreeItem<String> branchB = new TreeItem<>("Branch B");

        branchA.getChildren().addAll(new TreeItem<>("Item A1"), new TreeItem<>("Item A2"));
        branchB.getChildren().addAll(new TreeItem<>("Item B1"), new TreeItem<>("Item B2"));
        root.getChildren().addAll(branchA, branchB);

        TreeView<String> treeView = new TreeView<>(root);

        // Apply the custom cell factory to enable drag-and-drop edits
        treeView.setCellFactory(tv -> new DraggableTreeCell());

        StackPane pane = new StackPane(treeView);
        primaryStage.setScene(new Scene(pane, 400, 500));
        primaryStage.setTitle("Editable JavaFX TreeView");
        primaryStage.show();
    }

    // Custom TreeCell class that handles drag-and-drop events
    private static class DraggableTreeCell extends TreeCell<String> {

        public DraggableTreeCell() {
            // 1. DRAG DETECTED: Triggered when user starts dragging a cell
            setOnDragDetected(event -> {
                if (getItem() == null || getTreeItem() == getTreeView().getRoot()) {
                    return; // Don't allow dragging empty cells or the root node
                }

                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                // Track the item being dragged globally
                draggedItem = getTreeItem();
                content.put(TREE_ITEM_FORMAT, getItem());
                db.setContent(content);

                event.consume();
            });

            // 2. DRAG OVER: Visual cues and setting accepted transfer modes
            setOnDragOver(event -> {
                if (event.getDragboard().hasContent(TREE_ITEM_FORMAT) && draggedItem != null) {
                    TreeItem<String> targetItem = getTreeItem();

                    // Prevent dropping an item onto itself or into its own descendants
                    if (targetItem != draggedItem && !isDescendant(draggedItem, targetItem)) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                event.consume();
            });

            // 3. DRAG DROPPED: Re-structure the hierarchy and order
            setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasContent(TREE_ITEM_FORMAT) && draggedItem != null) {
                    TreeItem<String> targetItem = getTreeItem();

                    // Remove dragged item from its current parent
                    draggedItem.getParent().getChildren().remove(draggedItem);

                    if (targetItem == null) {
                        // Drop at the root level if dropped on empty space
                        getTreeView().getRoot().getChildren().add(draggedItem);
                    } else if (targetItem.isExpanded() && !targetItem.getChildren().isEmpty()) {
                        // If the target is an expanded branch, add it as the first child
                        targetItem.getChildren().add(0, draggedItem);
                    } else {
                        // Otherwise, insert it right next to (below) the target item
                        TreeItem<String> parent = targetItem.getParent();
                        int index = parent.getChildren().indexOf(targetItem);
                        parent.getChildren().add(index + 1, draggedItem);
                    }

                    // Select the newly moved item
                    getTreeView().getSelectionModel().select(draggedItem);
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            });

            // Clean up reference after drag is finished
            setOnDragDone(DragEvent::consume);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item);
            }
        }

        // Helper method to prevent circular hierarchies (dropping a parent inside its child)
        private boolean isDescendant(TreeItem<String> parent, TreeItem<String> child) {
            if (child == null) return false;
            if (child.getParent() == parent) return true;
            return isDescendant(parent, child.getParent());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}