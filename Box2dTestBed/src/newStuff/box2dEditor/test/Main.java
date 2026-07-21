package newStuff.box2dEditor.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minimal sprite-placement editor:
 *   - Bottom: a horizontally-scrolling grid gallery of draggable sprite thumbnails.
 *   - Center: a Canvas you drop sprites onto. While dragging over the canvas,
 *     a translucent "ghost" preview follows the cursor exactly (snapped to a
 *     grid) so you can see where the sprite will land before you release.
 */
public class Main extends Application {

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int GRID_SIZE = 32;

    /** name -> sprite image, looked up again on drop since Dragboard images can lose fidelity. */
    private final Map<String, Image> spriteRegistry = new ConcurrentHashMap<>();

    private Canvas baseCanvas;   // permanently placed sprites (+ grid)
    private Canvas ghostCanvas;  // transient drag preview, cleared on drop/exit

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setBottom(buildGallery());
        root.setCenter(buildCanvasArea());

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Sprite Editor");
        stage.setScene(scene);
        stage.show();
    }

    // ---------------------------------------------------------------- gallery

    private static final double GALLERY_BAR_HEIGHT = 190;
    private static final double THUMB_SIZE = 88;

    private VBox buildGallery() {
        Label header = new Label("SPRITES");
        header.setTextFill(Color.web("#b5bac1"));
        header.setFont(Font.font("System", FontWeight.BOLD, 12));
        header.setPadding(new Insets(10, 0, 6, 14));

        // FlowPane in VERTICAL orientation fills a column top-to-bottom and only
        // wraps into a new column once it hits prefWrapLength — i.e. a proper grid
        // of rows/columns that grows sideways, which is exactly what a horizontally
        // scrolling bottom tray needs.
        FlowPane grid = new FlowPane(Orientation.VERTICAL, 14, 14);
        grid.setPadding(new Insets(4, 14, 14, 14));
        grid.setPrefWrapLength(GALLERY_BAR_HEIGHT - 40);

        for (Sprite sprite : demoSprites()) {
            spriteRegistry.put(sprite.name(), sprite.image());
            grid.getChildren().add(buildGalleryCell(sprite));
        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);
        scroll.setStyle("-fx-background: #2b2d31; -fx-background-color: #2b2d31;");

        VBox bar = new VBox(header, scroll);
        VBox.setVgrow(scroll, javafx.scene.layout.Priority.ALWAYS);
        bar.setPrefHeight(GALLERY_BAR_HEIGHT);
        bar.setStyle("-fx-background-color: #2b2d31;");
        return bar;
    }

    private VBox buildGalleryCell(Sprite sprite) {
        ImageView thumb = new ImageView(sprite.image());
        thumb.setFitWidth(THUMB_SIZE);
        thumb.setFitHeight(THUMB_SIZE);
        thumb.setPreserveRatio(true);

        Label name = new Label(sprite.name());
        name.setTextFill(Color.web("#dbdee1"));
        name.setFont(Font.font(12));

        VBox cell = new VBox(6, thumb, name);
        cell.setAlignment(Pos.CENTER);
        cell.setPadding(new Insets(10));
        cell.setPrefWidth(THUMB_SIZE + 20);
        cell.setStyle("-fx-background-color: #313338; -fx-background-radius: 8;");
        cell.setCursor(javafx.scene.Cursor.OPEN_HAND);

        // Start of the drag: put the sprite's name on the dragboard (the source of
        // truth) plus the image itself so the OS shows a native drag cursor too.
        cell.setOnDragDetected(event -> {
            Dragboard db = cell.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(sprite.name());
            content.putImage(sprite.image());
            db.setContent(content);
            event.consume();
        });

        return cell;
    }

    // ------------------------------------------------------------- canvas area

    private StackPane buildCanvasArea() {
        baseCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        ghostCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        ghostCanvas.setMouseTransparent(true); // let drag events pass through to baseCanvas
        drawGrid(baseCanvas.getGraphicsContext2D());

        StackPane canvasStack = new StackPane(baseCanvas, ghostCanvas);
        canvasStack.setStyle("-fx-background-color: #1e1f22;");

        setupDragAndDrop(canvasStack);

        StackPane wrapper = new StackPane(canvasStack);
        wrapper.setPadding(new Insets(20));
        wrapper.setStyle("-fx-background-color: #1e1f22;");
        return wrapper;
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setFill(Color.web("#26282c"));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.setStroke(Color.web("#33353a"));
        gc.setLineWidth(1);
        for (int x = 0; x <= CANVAS_WIDTH; x += GRID_SIZE) {
            gc.strokeLine(x + 0.5, 0, x + 0.5, CANVAS_HEIGHT);
        }
        for (int y = 0; y <= CANVAS_HEIGHT; y += GRID_SIZE) {
            gc.strokeLine(0, y + 0.5, CANVAS_WIDTH, y + 0.5);
        }
    }

    private void setupDragAndDrop(StackPane canvasStack) {
        // Handlers are registered on canvasStack, but DragEvent.getX()/getY() are
        // relative to whichever node the drag *originated* from (the gallery cell),
        // not the node the handler is attached to — that mismatch was why the ghost
        // drifted away from the cursor. Converting scene coordinates into the
        // canvas's own local space via sceneToLocal sidesteps that ambiguity.
        canvasStack.setOnDragOver(event -> {
            if (event.getDragboard().hasString() && spriteRegistry.containsKey(event.getDragboard().getString())) {
                event.acceptTransferModes(TransferMode.COPY);
                Point2D local = ghostCanvas.sceneToLocal(event.getSceneX(), event.getSceneY());
                showGhost(event.getDragboard().getString(), local.getX(), local.getY());
            }
            event.consume();
        });

        canvasStack.setOnDragExited(event -> {
            clearGhost();
            event.consume();
        });

        canvasStack.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString() && spriteRegistry.containsKey(db.getString())) {
                Image image = spriteRegistry.get(db.getString());
                Point2D local = baseCanvas.sceneToLocal(event.getSceneX(), event.getSceneY());
                double[] snapped = snap(local.getX(), local.getY(), image);
                baseCanvas.getGraphicsContext2D().drawImage(image, snapped[0], snapped[1]);
                success = true;
            }
            clearGhost();
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /** Snap the sprite's top-left corner so it centers on the cursor and aligns to the grid. */
    private double[] snap(double cursorX, double cursorY, Image image) {
        double rawX = cursorX - image.getWidth() / 2.0;
        double rawY = cursorY - image.getHeight() / 2.0;
        double snappedX = Math.round(rawX / GRID_SIZE) * GRID_SIZE;
        double snappedY = Math.round(rawY / GRID_SIZE) * GRID_SIZE;
        return new double[]{snappedX, snappedY};
    }

    private void showGhost(String spriteName, double cursorX, double cursorY) {
        Image image = spriteRegistry.get(spriteName);
        double[] snapped = snap(cursorX, cursorY, image);

        GraphicsContext gc = ghostCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        gc.setGlobalAlpha(0.5);
        gc.drawImage(image, snapped[0], snapped[1]);
        gc.setGlobalAlpha(1.0);

        gc.setStroke(Color.web("#5865f2"));
        gc.setLineWidth(2);
        gc.setLineDashes(6);
        gc.strokeRect(snapped[0], snapped[1], image.getWidth(), image.getHeight());
        gc.setLineDashes(0);
    }

    private void clearGhost() {
        ghostCanvas.getGraphicsContext2D().clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    // ------------------------------------------------------------- demo sprites
    // Placeholder sprites generated on the fly so the app runs with no external
    // assets. Swap SpriteFactory calls for Image(file/url) to load real art.

    private List<Sprite> demoSprites() {
        List<Sprite> sprites = new ArrayList<>();
        sprites.add(new Sprite("Hero", SpriteFactory.circle(Color.web("#f23f42"))));
        sprites.add(new Sprite("Crate", SpriteFactory.square(Color.web("#e2a03f"))));
        sprites.add(new Sprite("Gem", SpriteFactory.diamond(Color.web("#3fa9f2"))));
        sprites.add(new Sprite("Tree", SpriteFactory.triangle(Color.web("#3fb26f"))));
        sprites.add(new Sprite("Star", SpriteFactory.star(Color.web("#f2e23f"))));
        return sprites;
    }

    private record Sprite(String name, Image image) {}

    /** Generates small colored placeholder sprite images so the demo needs no asset files. */
    private static final class SpriteFactory {
        private static final int SIZE = 96;

        static Image circle(Color color) {
            return render(gc -> {
                gc.setFill(color);
                gc.fillOval(4, 4, SIZE - 8, SIZE - 8);
            });
        }

        static Image square(Color color) {
            return render(gc -> {
                gc.setFill(color);
                gc.fillRoundRect(4, 4, SIZE - 8, SIZE - 8, 6, 6);
            });
        }

        static Image diamond(Color color) {
            return render(gc -> {
                gc.setFill(color);
                gc.fillPolygon(
                        new double[]{SIZE / 2.0, SIZE - 4, SIZE / 2.0, 4},
                        new double[]{4, SIZE / 2.0, SIZE - 4, SIZE / 2.0}, 4);
            });
        }

        static Image triangle(Color color) {
            return render(gc -> {
                gc.setFill(color);
                gc.fillPolygon(
                        new double[]{SIZE / 2.0, SIZE - 4, 4},
                        new double[]{4, SIZE - 4, SIZE - 4}, 3);
            });
        }

        static Image star(Color color) {
            return render(gc -> {
                gc.setFill(color);
                double cx = SIZE / 2.0, cy = SIZE / 2.0, outer = SIZE / 2.0 - 4, inner = outer / 2.3;
                double[] xs = new double[10], ys = new double[10];
                for (int i = 0; i < 10; i++) {
                    double r = (i % 2 == 0) ? outer : inner;
                    double angle = Math.PI / 2 * 3 + i * Math.PI / 5;
                    xs[i] = cx + r * Math.cos(angle);
                    ys[i] = cy + r * Math.sin(angle);
                }
                gc.fillPolygon(xs, ys, 10);
            });
        }

        private static Image render(java.util.function.Consumer<GraphicsContext> paint) {
            Canvas c = new Canvas(SIZE, SIZE);
            paint.accept(c.getGraphicsContext2D());
            WritableImage img = new WritableImage(SIZE, SIZE);
            c.snapshot(null, img);
            return img;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}