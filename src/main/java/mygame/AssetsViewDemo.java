package mygame;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Game-editor "Assets" panel: a SplitPane with a folder tree on the left and
 * a grid of selectable thumbnails on the right. Image files are rendered as
 * actual thumbnails; other resource types (audio, models, scripts, ...) get
 * a generic glyph icon. Click to select, Ctrl/Cmd-click to multi-select.
 *
 * AssetsView is the reusable component; AssetsViewDemo just wires it up in
 * a window and generates a throwaway sample folder so the demo has
 * something to show without needing real project assets.
 */
public class AssetsViewDemo extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        File demoRoot = generateDemoAssets();

        AssetsView assetsView = new AssetsView(demoRoot);

        Scene scene = new Scene(assetsView, 780, 520);
        stage.setScene(scene);
        stage.setTitle("Assets");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /** Builds a small folder tree with real PNG thumbnails plus placeholder
     *  files for other resource types, just so the demo is self-contained. */
    private File generateDemoAssets() throws IOException {
        Path root = Files.createTempDirectory("assets_demo");

        Path textures = Files.createDirectories(root.resolve("Textures"));
        Path audio = Files.createDirectories(root.resolve("Audio"));
        Path models = Files.createDirectories(root.resolve("Models"));
        Path scripts = Files.createDirectories(root.resolve("Scripts"));
        Files.createDirectories(textures.resolve("UI"));

        writePng(textures.resolve("player.png"), new java.awt.Color(210, 90, 90));
        writePng(textures.resolve("grass.png"), new java.awt.Color(90, 180, 100));
        writePng(textures.resolve("sky.png"), new java.awt.Color(100, 150, 230));
        writePng(textures.resolve("UI").resolve("button.png"), new java.awt.Color(200, 200, 90));

        Files.createFile(audio.resolve("jump.wav"));
        Files.createFile(audio.resolve("theme.mp3"));
        Files.createFile(models.resolve("enemy.fbx"));
        Files.createFile(models.resolve("crate.obj"));
        Files.createFile(scripts.resolve("PlayerController.java"));
        Files.createFile(scripts.resolve("EnemyAI.java"));

        return root.toFile();
    }

    private void writePng(Path path, java.awt.Color color) throws IOException {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 64, 64);
        g.dispose();
        ImageIO.write(img, "png", path.toFile());
    }

    // ------------------------------------------------------------------
    // Reusable component
    // ------------------------------------------------------------------

    public static class AssetsView extends SplitPane {

        private static final Set<String> IMAGE_EXT = Set.of("png", "jpg", "jpeg", "gif", "bmp");
        private static final Set<String> AUDIO_EXT = Set.of("wav", "mp3", "ogg", "flac");
        private static final Set<String> MODEL_EXT = Set.of("fbx", "obj", "gltf", "glb", "dae");
        private static final Set<String> SCRIPT_EXT = Set.of("java", "kt", "js", "py", "cs");

        private final FlowPane grid = new FlowPane(12, 12);
        private final Set<AssetCell> selectedCells = new LinkedHashSet<>();

        public AssetsView(File rootFolder) {
            TreeView<File> tree = buildTree(rootFolder);

            grid.setPadding(new Insets(12));
            grid.setStyle("-fx-background-color: #2b2b2b;");

            ScrollPane scroll = new ScrollPane(grid);
            scroll.setFitToWidth(true);
            scroll.setStyle("-fx-background: #2b2b2b;");

            getItems().addAll(tree, scroll);
            setDividerPositions(0.28);

            tree.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
                if (sel != null) populateGrid(sel.getValue());
            });
            tree.getSelectionModel().select(tree.getRoot());
        }

        private TreeView<File> buildTree(File root) {
            LazyDirectoryTreeItem rootItem = new LazyDirectoryTreeItem(root);
            rootItem.setExpanded(true);
            TreeView<File> tv = new TreeView<>(rootItem);
            tv.setShowRoot(true);
            tv.setCellFactory(v -> new FolderTreeCell());
            tv.setStyle("-fx-background-color: #313335;");
            return tv;
        }

        private void populateGrid(File folder) {
            selectedCells.clear();
            grid.getChildren().clear();

            File[] files = folder.listFiles(File::isFile);
            if (files == null) return;
            Arrays.sort(files, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));

            for (File f : files) {
                grid.getChildren().add(new AssetCell(f, this::onCellClicked));
            }
        }

        private void onCellClicked(AssetCell cell, MouseEvent event) {
            boolean multi = event.isControlDown() || event.isShortcutDown();

            if (!multi) {
                selectedCells.forEach(c -> c.setSelected(false));
                selectedCells.clear();
                selectedCells.add(cell);
                cell.setSelected(true);
            } else if (selectedCells.contains(cell)) {
                selectedCells.remove(cell);
                cell.setSelected(false);
            } else {
                selectedCells.add(cell);
                cell.setSelected(true);
            }
        }

        // ---- tree: lazy folder-only nodes ----

        private static class LazyDirectoryTreeItem extends TreeItem<File> {
            private boolean loaded = false;

            LazyDirectoryTreeItem(File dir) {
                super(dir);
            }

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (!loaded) {
                    loaded = true;
                    super.getChildren().setAll(buildChildren());
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                File[] subdirs = getValue().listFiles(File::isDirectory);
                return subdirs == null || subdirs.length == 0;
            }

            private List<TreeItem<File>> buildChildren() {
                File[] dirs = getValue().listFiles(File::isDirectory);
                List<TreeItem<File>> items = new ArrayList<>();
                if (dirs != null) {
                    Arrays.sort(dirs, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
                    for (File d : dirs) items.add(new LazyDirectoryTreeItem(d));
                }
                return items;
            }
        }

        private static class FolderTreeCell extends TreeCell<File> {
            @Override
            protected void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);
                if (empty || file == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String name = file.getName();
                    setText(name.isEmpty() ? file.getPath() : name);
                    Label icon = new Label("\uD83D\uDCC1"); // folder glyph
                    setGraphic(icon);
                    setTextFill(Color.web("#dddddd"));
                }
            }
        }

        // ---- grid: one thumbnail/icon cell per asset file ----

        private static class AssetCell extends VBox {
            private static final double THUMB = 84;
            private boolean selected = false;

            AssetCell(File file, BiConsumer<AssetCell, MouseEvent> onClick) {
                setAlignment(Pos.TOP_CENTER);
                setSpacing(4);
                setPadding(new Insets(6));
                setPrefWidth(THUMB + 16);

                StackPane thumbHolder = new StackPane();
                thumbHolder.setMinSize(THUMB, THUMB);
                thumbHolder.setPrefSize(THUMB, THUMB);
                thumbHolder.setMaxSize(THUMB, THUMB);
                thumbHolder.setStyle("-fx-background-color: #3c3f41; -fx-background-radius: 4;");

                String ext = extensionOf(file.getName());
                if (IMAGE_EXT.contains(ext)) {
                    ImageView iv = new ImageView(new Image(
                            file.toURI().toString(), THUMB - 8, THUMB - 8, true, true, true));
                    thumbHolder.getChildren().add(iv);
                } else {
                    Label glyph = new Label(glyphFor(ext));
                    glyph.setFont(Font.font(30));
                    glyph.setTextFill(Color.web("#bbbbbb"));
                    thumbHolder.getChildren().add(glyph);
                }

                Label name = new Label(file.getName());
                name.setMaxWidth(THUMB + 8);
                name.setTextOverrun(OverrunStyle.ELLIPSIS);
                name.setTextFill(Color.web("#dddddd"));
                name.setStyle("-fx-font-size: 11px;");
                Tooltip.install(this, new Tooltip(file.getName()));

                getChildren().addAll(thumbHolder, name);

                setOnMouseClicked(e -> onClick.accept(this, e));
                refreshStyle();
            }

            void setSelected(boolean selected) {
                this.selected = selected;
                refreshStyle();
            }

            private void refreshStyle() {
                setStyle(selected
                        ? "-fx-background-color: #2f65ca55; -fx-background-radius: 6; "
                        + "-fx-border-color: #4d90fe; -fx-border-radius: 6; -fx-border-width: 1.5;"
                        : "-fx-background-color: transparent; -fx-border-color: transparent;");
            }

            private String glyphFor(String ext) {
                if (AUDIO_EXT.contains(ext)) return "\uD83D\uDD0A";   // speaker
                if (MODEL_EXT.contains(ext)) return "\uD83D\uDCE6";   // 3D box
                if (SCRIPT_EXT.contains(ext)) return "{ }";
                return "\uD83D\uDCC4";                                // generic file
            }
        }

        private static String extensionOf(String name) {
            int dot = name.lastIndexOf('.');
            return dot < 0 ? "" : name.substring(dot + 1).toLowerCase(Locale.ROOT);
        }
    }
}
