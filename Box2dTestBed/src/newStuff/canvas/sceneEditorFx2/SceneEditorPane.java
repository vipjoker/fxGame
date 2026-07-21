package newStuff.canvas.sceneEditorFx2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

// ===== Main editor pane =====
class SceneEditorPane extends BorderPane {
    private final CanvasView canvas = new CanvasView();
    private final Label status = new Label("Ready");
    private TreeView<Object> tree;
    private TreeItem<Object> objectsNode;
    private boolean suppressTreeSelection = false;
    private TreeItem<Object> draggedItem;
    // Property fields
    private final TextField posX = new TextField("0");
    private final TextField posY = new TextField("0");
    private final TextField rotDeg = new TextField("0");
    private final TextField scaleX = new TextField("1");
    private final TextField scaleY = new TextField("1");

    SceneEditorPane() {
        setPadding(new Insets(6));
        setTop(createToolbar());
        setCenter(createMainSplit());
        setBottom(createStatusBar());

        canvas.setSceneChangedListener(this::rebuildTree);
        canvas.setSelectionChangedListener(this::onSelectionChanged);


        loadJson();
        rebuildTree();
    }

    public String loadJson() {

        try {
            canvas.objects.add(new RectObj(0, 0, 100, 100, Color.BLUE));
            RectObj red = new RectObj(50, 50, 100, 100, Color.RED);
            canvas.objects.add(red);
            RectObj yellow = new RectObj(20,20, 50,50,Color.YELLOW);
            red.addChild(yellow);
            return Files.readString(Paths.get("scene.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveJson(String json) {
        try {
            Files.writeString(Paths.get("scene.json"), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private ToolBar createToolbar() {
        ComboBox<SceneEditorFx2.Tool> toolBox = new ComboBox<>();
        toolBox.getItems().addAll(SceneEditorFx2.Tool.values());
        toolBox.setValue(SceneEditorFx2.Tool.SELECT_MOVE);
        toolBox.setOnAction(e -> {
            SceneEditorFx2.Tool t = toolBox.getValue();
            canvas.setTool(t);
            status.setText("Tool: " + t);
        });

        Button loadSprite = new Button("Load Sprite");
        loadSprite.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File f = fc.showOpenDialog(getScene().getWindow());
            if (f != null) {
                try {
                    Image img = new Image(f.toURI().toString());
                    if (!img.isError()) {
                        canvas.setPendingSprite(img, f.getName());
                        canvas.setTool(SceneEditorFx2.Tool.ADD_SPRITE);
                        toolBox.setValue(SceneEditorFx2.Tool.ADD_SPRITE);
                        status.setText("Sprite loaded: " + f.getName() + " (click to place)");
                    }
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR,
                            "Failed to load image: " + ex.getMessage()).showAndWait();
                }
            }
        });

        Button addGroup = new Button("Add Container");
        addGroup.setOnAction(e -> {
            canvas.addContainer(new Point2D(0, 0));
            status.setText("Container added");
        });

        Button parentToSel = new Button("Group Selected → New Container");
        parentToSel.setOnAction(e -> {
            SceneObject sel = canvas.getSelected();
            if (sel != null) {
                GroupObj g = canvas.addContainer(sel.getWorldAnchor());
                canvas.reparent(sel, g);
                canvas.setSelected(g);
                status.setText("Wrapped selection into container");
            }
        });

        Button clear = new Button("Clear");
        clear.setOnAction(e -> {
            canvas.clearScene();
            status.setText("Scene cleared");
        });

        Button save = new Button("Save");
        save.setOnAction(e -> {


            List<SceneObject> objectsView = canvas.getObjectsView();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(objectsView);
            System.out.println(json);
//                saveJson();
        });

        Button load = new Button("Load");
        load.setOnAction(e -> {
            loadJson();
        });

        return new ToolBar(new Label("Tool:"), toolBox, loadSprite, clear, addGroup, parentToSel, save, load,
                new Label("  Tips: wheel=zoom, right/middle-drag=pan, drag object=move"));
    }

    private SplitPane createMainSplit() {
        SplitPane rightSplit = new SplitPane(canvas, createPropertiesPanel());
        rightSplit.setOrientation(Orientation.HORIZONTAL);
        rightSplit.setDividerPositions(0.78);

        SplitPane mainSplit = new SplitPane(createTreePanel(), rightSplit);
        mainSplit.setOrientation(Orientation.HORIZONTAL);
        mainSplit.setDividerPositions(0.20);
        return mainSplit;
    }

    private TitledPane createTreePanel() {
        TreeItem<Object> root = new TreeItem<>("Scene");
        objectsNode = new TreeItem<>("Objects");
        root.getChildren().add(objectsNode);
        root.setExpanded(true);
        objectsNode.setExpanded(true);

        tree = new TreeView<>(root);
        tree.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> {
            if (suppressTreeSelection) return;
            if (b != null && b.getValue() instanceof ObjectItem oi) {
                canvas.setSelected(oi.object);
                status.setText("Selected: " + oi.label);
            } else {
                canvas.setSelected(null);
            }
        });


        tree.setCellFactory(tv -> new TreeCell<>() {
            {
                setOnDragDetected(e -> {
                    if (getItem() instanceof ObjectItem) {
                        Dragboard db = startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent cc = new ClipboardContent();
                        cc.putString("node");
                        db.setContent(cc);
                        draggedItem = getTreeItem();
                        e.consume();
                    }
                });
                setOnDragOver(e -> {
                    if (draggedItem != null && e.getGestureSource() != this) {
                        e.acceptTransferModes(TransferMode.MOVE);
                    }
                    e.consume();
                });
                setOnDragDropped(e -> {
                    if (draggedItem == null) return;
                    SceneObject dragged = ((ObjectItem) draggedItem.getValue()).object;
                    SceneObject target = null;
                    TreeItem<Object> ti = getTreeItem();
                    if (ti != null && ti.getValue() instanceof ObjectItem oi) target = oi.object;
                    else if (ti == objectsNode) target = null; // drop on "Objects" = root
                    canvas.reparent(dragged, target);
                    rebuildTree();
                    e.setDropCompleted(true);
                    draggedItem = null;
                    e.consume();
                });
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });


        TitledPane pane = new TitledPane("Hierarchy", tree);
        pane.setCollapsible(false);
        return pane;
    }

    private TitledPane createPropertiesPanel() {
        GridPane form = new GridPane();
        form.setHgap(6);
        form.setVgap(6);
        form.setPadding(new Insets(8));

        int r = 0;
        form.add(new Label("Position X"), 0, r);
        form.add(posX, 1, r++);
        form.add(new Label("Position Y"), 0, r);
        form.add(posY, 1, r++);
        form.add(new Label("Rotation°"), 0, r);
        form.add(rotDeg, 1, r++);
        form.add(new Label("Scale X"), 0, r);
        form.add(scaleX, 1, r++);
        form.add(new Label("Scale Y"), 0, r);
        form.add(scaleY, 1, r++);

        Button apply = new Button("Apply");
        apply.setOnAction(e -> applyProperties());
        form.add(apply, 1, r);

        posX.setOnAction(e -> applyProperties());
        posY.setOnAction(e -> applyProperties());
        rotDeg.setOnAction(e -> applyProperties());
        scaleX.setOnAction(e -> applyProperties());
        scaleY.setOnAction(e -> applyProperties());

        TitledPane pane = new TitledPane("Properties", form);
        pane.setCollapsible(false);
        return pane;
    }

    private void applyProperties() {
        SceneObject s = canvas.getSelected();
        if (s == null) return;
        try {
            double nx = Double.parseDouble(posX.getText().trim());
            double ny = Double.parseDouble(posY.getText().trim());
            Point2D a = s.getAnchor();
            s.translate(nx - a.getX(), ny - a.getY());

            s.setRotation(Math.toRadians(Double.parseDouble(rotDeg.getText().trim())));

            double sx = Double.parseDouble(scaleX.getText().trim());
            double sy = Double.parseDouble(scaleY.getText().trim());
            if (sx != 1.0 || sy != 1.0) {
                s.scaleFromAnchor(sx, sy);
                scaleX.setText("1");
                scaleY.setText("1");
            }
            canvas.redraw();
        } catch (NumberFormatException ignored) {
            status.setText("Invalid number in properties");
        }
    }

    private HBox createStatusBar() {
        HBox box = new HBox(status);
        box.setPadding(new Insets(4, 8, 4, 8));
        return box;
    }

    private void rebuildTree() {
        if (objectsNode == null) return;
        objectsNode.getChildren().clear();
        int[] counter = {1};
        for (SceneObject o : canvas.getObjectsView()) {
            objectsNode.getChildren().add(buildTreeItem(o, counter));
        }
        objectsNode.setExpanded(true);
    }

    private TreeItem<Object> buildTreeItem(SceneObject o, int[] counter) {
        String label = (counter[0]++) + " - " + o.getClass().getSimpleName();
        TreeItem<Object> item = new TreeItem<>(new ObjectItem(o, label));
        item.setExpanded(true);
        for (SceneObject child : o.children) {
            item.getChildren().add(buildTreeItem(child, counter));
        }
        return item;
    }

    private void onSelectionChanged(SceneObject target) {
        suppressTreeSelection = true;
        try {
            if (target == null) tree.getSelectionModel().clearSelection();
            else {
                TreeItem<Object> found = findItem(objectsNode, target);
                if (found != null) tree.getSelectionModel().select(found);
            }
        } finally {
            suppressTreeSelection = false;
        }
        if (target != null) {
            Point2D targetAnchor = target.getAnchor();
            posX.setText(String.format(Locale.US, "%.2f", targetAnchor.getX()));
            posY.setText(String.format(Locale.US, "%.2f", targetAnchor.getY()));
            rotDeg.setText(String.format(Locale.US, "%.2f", Math.toDegrees(target.getRotation())));
            scaleX.setText("1");
            scaleY.setText("1");
        }
    }

    private TreeItem<Object> findItem(TreeItem<Object> node, SceneObject target) {
        if (node.getValue() instanceof ObjectItem oi && oi.object == target) return node;
        for (TreeItem<Object> c : node.getChildren()) {
            TreeItem<Object> r = findItem(c, target);
            if (r != null) return r;
        }
        return null;
    }
}
