package mygame.editor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mygame.Constants;
import mygame.editor.actions.*;
import mygame.editor.model.TileModel;
import mygame.util.LevelParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable {
    private PanListener panListener;
    //    public Pane pnGrid;
    public TilePane tilePane;
    public Button btnRun;
    public Label tvStatus;
    public Pane canvas;
    public FlowPane buttonLayout;
    ToggleGroup group;
    private float scale = 1;

    private StackPane mPane;
    private World world;
    private Body body;
    private AnimationTimer animationTimer;


    private Drawer currentDrawer;
    private Map<String, Drawer> actions;
    private Group transGroup;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        group = new ToggleGroup();

        for (Node node : buttonLayout.getChildren()) {
            group.getToggles().add(((ToggleButton) node));
        }


        transGroup = new Group();


        canvas.getChildren().add(transGroup);


        world = new World(new Vector2(0, 10), false);
        panListener = new PanListener((x, y) -> {


            double scaleMult = 1 / transGroup.getScaleX();

            System.out.println(scaleMult + " " + transGroup.getScaleX());
            transGroup.setTranslateX(transGroup.getTranslateX() + x);
            transGroup.setTranslateY(transGroup.getTranslateY() + y);
            // transGroup.getTransforms().addAll(Transform.translate(scaleMult*x,scaleMult*y));

        }, (s) -> {
            double scale = transGroup.getScaleX() + s / 100;
            //child.getTransforms().add(Transform.translate(x,y));
            if (scale < 0.1 || scale > 10) return;
            transGroup.setScaleY(scale);
            transGroup.setScaleX(scale);
        }


        );

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 10);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f, .5f);

        body = world.createBody(bodyDef);
        body.createFixture(shape, .1f);


        canvas.setBackground(new Background(new BackgroundFill(Constants.BACKGROUND, null, null)));
        setListeners();
//        setGrid();
        initActions();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> handleHere(now));

            }
        };


    }

    private void setListeners() {

        Platform.runLater(() -> {
            App.getInstance().getScene().setOnKeyPressed(event -> {

                if (event.getCode() == KeyCode.ENTER)
                    if (currentDrawer != null) currentDrawer.finishDrawing();
            });
        });




        canvas.setCursor(Cursor.CROSSHAIR);




        canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                panListener.panCanvasDragged(event);

            } else if (currentDrawer != null) currentDrawer.mouseMoved(asTranslated(event));
        });

        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                panListener.panCanvasPressed(event);

            } else if (currentDrawer != null) currentDrawer.mousePressed(asTranslated(event));
        });


        canvas.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                panListener.panCanvasReleased(event);
            } else if (currentDrawer != null) currentDrawer.mouseReleased(asTranslated(event));

        });

        canvas.setOnScroll(event -> {

            panListener.onScroll(event);
        });
        ChangeListener<Number> tChangeListener = (observable, oldValue, newValue) -> updateViews();

        canvas.widthProperty().addListener(tChangeListener);
        canvas.heightProperty().addListener(tChangeListener);


    }


    private Point2D asTranslated(MouseEvent event) {


        return transGroup.screenToLocal(event.getScreenX(), event.getScreenY());

    }

    private void updateViews() {
        System.out.println("Updated");
        transGroup.getChildren().clear();
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        Rectangle rect = new Rectangle(width, height);
        canvas.setClip(rect);


        Point2D canvasLocationY = canvas.localToScreen(0, 0);
        Point2D point2D = transGroup.screenToLocal(canvasLocationY);

//        for (int y = (int)asGroup.getX(); y < height; y += 20) {

        transGroup.getChildren().addAll(
                getLine(width / 2, point2D.getY(), width / 2, height)
                // getText(height, i)
        );

//        }


//        for (int i = 20; i < width; i += 20) {
        transGroup.getChildren().addAll(
                getLine(0, height / 2, width, height / 2)
                // getText(height, i)
        );
//        }


    }

    private Text getText(double x, double y, String message) {
        Text text = new Text(x, y, message);
        text.setFill(Color.WHITE.deriveColor(1, 1, 1, .5));
        return text;
    }

    private Line getLine(double x, double y, double x2, double y2) {
        Line line = new Line(x, y, x2, y2);
        line.setStroke(Constants.LIGHT_GREY);
        line.setStrokeWidth(1);
        return line;
    }


    private void initActions() {
        actions = new HashMap<>();
        actions.put(Constants.ACTION_POLYGON, new PolygonDrawer(transGroup));
        actions.put(Constants.ACTION_CIRCLE, new CircleDrawer(transGroup));
        actions.put(Constants.ACTION_RECTANGLE, new RectangleDrawer(transGroup));
        actions.put(Constants.ACTION_CHAIN, new LineDrawer(transGroup));
    }

    private void handleHere(long now) {
        Array<Body> bodies = new Array<>();
        world.step(1f / 60f, 6, 3);
        world.getBodies(bodies);

        // pnGrid.getChildren().clear();
        for (Body body : bodies) {

            Vector2 position = body.getPosition();

            tvStatus.setText(String.format("%s timer = %d", position.toString(), now));
            Circle circle = new Circle(position.x, position.y, 10, Color.BLUE);

            //  pnGrid.getChildren().add(circle);

        }

    }





    public void onSave(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Level files", ".level"));
        File file = fileChooser.showSaveDialog(App.getInstance().getWindow());
//        LevelParser.saveLevel(modelList, file);

    }

    public void onLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open level");

        File file = fileChooser.showOpenDialog(App.getInstance().getWindow());

        List<TileModel> tileModels = LevelParser.loadLevel(file);
        System.out.println(tileModels);


    }

    public void onAbout(ActionEvent event) {
        Dialog.showDialog("Tile builder editor by vipjoker");
    }

    public void onAdd(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Resource File");
        Stage window = App.getInstance().getStage();
        File imageDir = directoryChooser.showDialog(window);
        if (imageDir != null) {

            for (File file : imageDir.listFiles())
                addTile(file);
        }
    }

    private void addTile(File file) {

        Image image = null;
        try {
            image = new Image(new FileInputStream(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StackPane pane = new StackPane();

        ImageView imageView = new ImageView(image);


        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        pane.setStyle("-fx-border-color: #000000; -fx-border-width: 5px;-fx-border-radius: 5px;");

        pane.getChildren().add(imageView);
        imageView.setOnMouseClicked(this::onTileSelected);
        tilePane.getChildren().add(pane);


    }

    public void onTileSelected(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();

        imageView.getParent().setStyle("-fx-border-color: #ff005e;-fx-border-width: 5px;-fx-border-radius: 5px;");
        if (mPane != null)
            mPane.setStyle("-fx-border-color: #000000;-fx-border-width: 5px;-fx-border-radius: 5px;");

        mPane = (StackPane) imageView.getParent();

    }


    public void onAddH(ActionEvent event) {

    }

    public void onRemoveH(ActionEvent event) {

    }

    boolean started;

    public void onRun(ActionEvent actionEvent) {
//        if (!started) {
//            animationTimer.start();
//            started = true;
//        } else {
//            animationTimer.stop();
//            started = false;
//        }



        Dialog.showBox2dDialog();
    }

    public void onMove(ActionEvent actionEvent) {
        body.applyForceToCenter(new Vector2(10000, 0), true);

    }


    //buttons
    public void onStatic(ActionEvent actionEvent) {

    }

    public void onKinematic(ActionEvent actionEvent) {

    }

    public void onDynamic(ActionEvent actionEvent) {

    }

    public void onPolygon(ActionEvent actionEvent) {
        currentDrawer = actions.get(Constants.ACTION_POLYGON);

    }

    public void onChain(ActionEvent actionEvent) {
        currentDrawer = actions.get(Constants.ACTION_CHAIN);
    }

    public void onRectangle(ActionEvent actionEvent) {
        currentDrawer = actions.get(Constants.ACTION_RECTANGLE);
    }

    public void onCircle(ActionEvent actionEvent) {
        currentDrawer = actions.get(Constants.ACTION_CIRCLE);
    }

    public void onDistance(ActionEvent actionEvent) {

    }

    public void onRevolute(ActionEvent actionEvent) {

    }


}
