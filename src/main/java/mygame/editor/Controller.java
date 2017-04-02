package mygame.editor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mygame.editor.actions.*;
import mygame.editor.kotlin.ActionListenerDelegate;
import mygame.editor.kotlin.CustonPane;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.Point;
import mygame.editor.model.TileModel;
import mygame.util.LevelParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import static mygame.Constants.*;


public class Controller implements Initializable ,ActionListenerDelegate{
    public VBox vbInfo;


    public TilePane tilePane;
    public Button btnRun;
    public Label tvStatus;
    public CustonPane canvas;
    public FlowPane buttonLayout;
    ToggleGroup group;
    Box2dDialog box2dDialog;

    private StackPane mPane;

    private Action currentDrawer;
    private Map<String, Action> actions;

    private List<AbstractModel> models;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        models = new ArrayList<>();
        group = new ToggleGroup();
        box2dDialog = new Box2dDialog();
        for (Node node : buttonLayout.getChildren()) {
            group.getToggles().add(((ToggleButton) node));
        }


        canvas.setListenerDelegate(this);


        setListeners();
        initActions();


    }


    private void setListeners() {

        Platform.runLater(() -> {
            App.getInstance().getScene().setOnKeyPressed(event -> {

                if (event.getCode() == KeyCode.ENTER)
                    if (currentDrawer != null) currentDrawer.finishDrawing();
            });
        });

        canvas.prefWidthProperty().bind(((BorderPane) canvas.getParent()).widthProperty());
        canvas.prefHeightProperty().bind(((BorderPane) canvas.getParent()).heightProperty());

//
//        ChangeListener<Number> tChangeListener = (observable, oldValue, newValue) -> updateViews();
//        canvas.widthProperty().addListener(tChangeListener);
//        canvas.heightProperty().addListener(tChangeListener);
//        Platform.runLater(() -> updateViews());

    }






    private void initActions() {
        actions = new HashMap<>();
        actions.put(ACTION_SELECT,new SelectAction(vbInfo,canvas,models));
        actions.put(ACTION_POLYGON, new PolygonDrawer(canvas, models));
        actions.put(ACTION_CIRCLE, new CircleDrawer(canvas, models));
        actions.put(ACTION_RECTANGLE, new RectangleDrawer(canvas, models));
        actions.put(ACTION_CHAIN, new LineDrawer(canvas, models));
        actions.put(ACTION_MOVE, new MoverAction(canvas, models));
        actions.put(ACTION_ROTATE, new RotateAction(canvas, models));
        actions.put(ACTION_EDIT, new EditAction(canvas, models));
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


    public void onRun(ActionEvent actionEvent) {


        box2dDialog.show();
    }

    public void onMove(ActionEvent actionEvent) {

        switchDrawer(ACTION_MOVE);
    }


    //buttons
    public void onStatic(ActionEvent actionEvent) {

    }

    public void onKinematic(ActionEvent actionEvent) {

    }

    public void onDynamic(ActionEvent actionEvent) {

    }

    public void onPolygon(ActionEvent actionEvent) {
        switchDrawer(ACTION_POLYGON);
    }

    public void onChain(ActionEvent actionEvent) {
        switchDrawer(ACTION_CHAIN);
    }

    public void onRectangle(ActionEvent actionEvent) {
        switchDrawer(ACTION_RECTANGLE);
    }

    public void onCircle(ActionEvent actionEvent) {
        switchDrawer(ACTION_CIRCLE);
    }


    public void onDistance(ActionEvent actionEvent) {

    }

    public void onRevolute(ActionEvent actionEvent) {

    }


    private void switchDrawer(String action) {
        if (currentDrawer != null) currentDrawer.finishDrawing();
        currentDrawer = actions.get(action);
        currentDrawer.init();
    }

    public void onRotate(ActionEvent actionEvent) {
        switchDrawer(ACTION_ROTATE);
    }

    public void onEdit(ActionEvent actionEvent) {
        switchDrawer(ACTION_EDIT);
    }

    public void onSelect(ActionEvent actionEvent) {
        switchDrawer(ACTION_SELECT);
    }

    @Override
    public void onMousePressed(@NotNull Point point) {
        if (currentDrawer != null) currentDrawer.mousePressed(point);
    }

    @Override
    public void onMouseReleased(@NotNull Point point) {
        if (currentDrawer != null) currentDrawer.mouseReleased(point);
    }

    @Override
    public void onMouseDragged(@NotNull Point point) {
        if (currentDrawer != null) currentDrawer.mouseMoved(point);
    }
}
