package mygame.editor;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mygame.Constants;
import mygame.editor.actions.*;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.TileModel;
import mygame.editor.views.CustomRegion;
import mygame.util.LevelParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable  {
    private PanListener panListener;
    //    public Pane pnGrid;
    public TilePane tilePane;
    public Button btnRun;
    public Label tvStatus;
    public Pane canvas;
    public FlowPane buttonLayout;
    ToggleGroup group;
    Box2dDialog box2dDialog;

    private StackPane mPane;

    private Line horLine,verLine;

    private Action currentDrawer;
    private Map<String, Action> actions;
    private CustomRegion transGroup;
    private List <AbstractModel> models;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        models = new ArrayList<>();
        group = new ToggleGroup();
        box2dDialog= new Box2dDialog();
        for (Node node : buttonLayout.getChildren()) {
            group.getToggles().add(((ToggleButton) node));
        }


        transGroup = new CustomRegion();



        canvas.getChildren().add(transGroup);



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



        canvas.setBackground(new Background(new BackgroundFill(Constants.BACKGROUND, null, null)));
        setListeners();
//        setGrid();
        initActions();




    }



    private void setListeners() {

        Platform.runLater(() -> {
            App.getInstance().getScene().setOnKeyPressed(event -> {

                if (event.getCode() == KeyCode.ENTER)
                    if (currentDrawer != null) currentDrawer.finishDrawing();
            });
        });




        canvas.setCursor(Cursor.CROSSHAIR);

        canvas.prefWidthProperty().bind(((BorderPane) canvas.getParent()).widthProperty());
        canvas.prefHeightProperty().bind(((BorderPane) canvas.getParent()).heightProperty());


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

        canvas.setOnScroll(event -> panListener.onScroll(event));
        ChangeListener<Number> tChangeListener = (observable, oldValue, newValue) -> updateViews();
        canvas.widthProperty().addListener(tChangeListener);
        canvas.heightProperty().addListener(tChangeListener);

    }


    private Point2D asTranslated(MouseEvent event) {


        return transGroup.screenToLocal(event.getScreenX(), event.getScreenY());

    }

    private void updateViews() {
        System.out.println("Updated");
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        Rectangle rect = new Rectangle(width, height);
        canvas.setClip(rect);
        if(horLine == null){
            horLine = getLine(0,height/2,width,height/2);
            verLine = getLine(width/2,0,width/2,height);
            canvas.getChildren().addAll(horLine,verLine);
        }





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
        actions.put(Constants.ACTION_POLYGON, new PolygonDrawer(transGroup,models));
        actions.put(Constants.ACTION_CIRCLE, new CircleDrawer(transGroup,models));
        actions.put(Constants.ACTION_RECTANGLE, new RectangleDrawer(transGroup,models));
        actions.put(Constants.ACTION_CHAIN, new LineDrawer(transGroup,models));
        actions.put(Constants.ACTION_MOVE,new MoverAction(transGroup,models));
        actions.put(Constants.ACTION_ROTATE,new RotateAction(transGroup,models));
        actions.put(Constants.ACTION_EDIT,new EditAction(transGroup,models));
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

        switchDrawer(Constants.ACTION_MOVE);
    }


    //buttons
    public void onStatic(ActionEvent actionEvent) {

    }

    public void onKinematic(ActionEvent actionEvent) {

    }

    public void onDynamic(ActionEvent actionEvent) {

    }

    public void onPolygon(ActionEvent actionEvent) {
        switchDrawer(Constants.ACTION_POLYGON);
    }

    public void onChain(ActionEvent actionEvent) {
        switchDrawer(Constants.ACTION_CHAIN);
    }

    public void onRectangle(ActionEvent actionEvent) {
        switchDrawer(Constants.ACTION_RECTANGLE);
    }

    public void onCircle(ActionEvent actionEvent) {
        switchDrawer(Constants.ACTION_CIRCLE);
    }



    public void onDistance(ActionEvent actionEvent) {

    }

    public void onRevolute(ActionEvent actionEvent) {

    }


     private void switchDrawer(String action){
        if(currentDrawer !=null) currentDrawer.finishDrawing();
         currentDrawer = actions.get(action);
     }

    public void onRotate(ActionEvent actionEvent) {
        switchDrawer(Constants.ACTION_ROTATE);
    }

    public void onEdit(ActionEvent actionEvent) {
        switchDrawer(Constants.ACTION_EDIT);
    }
}
