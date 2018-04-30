package mygame.editor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import mygame.editor.actions.*;
import mygame.editor.kotlin.ActionListenerDelegate;
import mygame.editor.ui.CustomPane;
import mygame.editor.view.AbstractView;
import mygame.editor.model.Point;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import mygame.Constants.*;
import mygame.editor.actions.shapes.CircleDrawer;
import mygame.editor.actions.shapes.LineDrawer;
import mygame.editor.actions.shapes.PolygonDrawer;
import mygame.editor.actions.shapes.RectangleDrawer;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.shape.Circle;
import mygame.editor.data.DbConnection;
import mygame.editor.data.NodeDao;
import mygame.editor.data.entities.EntityNode;
import mygame.editor.ui.PhysicsNode;
import mygame.editor.ui.TreeItemPath;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import static mygame.Constants.*;


public class Controller implements Initializable, ActionListenerDelegate {
    TilePane tilePane = null;
    @FXML
    Button btnRun;

    public SplitPane root;
    ToggleGroup group;
    Box2dDialog box2dDialog = new Box2dDialog(this);
    CustomPane canvas = null;

    public  VBox leftPane;

    public VBox rightPane;

    public AnchorPane centerPane;

    public TreeView<Path> resourcesTreeview;

    private Action currentDrawer;
    private Map<String, Action> actions = new HashMap<>();

    private List<AbstractView> views = new ArrayList<>();

    public HBox toolbar;

    @FXML
    private InfoController infoController;

    private Image imageIcon = new Image(getClass().getResourceAsStream("/icons/image.png"));
    private Image folderImage = new Image(getClass().getResourceAsStream("/icons/foder_basic.png"));
    private Image crate = new Image(getClass().getResourceAsStream("/background/Object/Crate.png"));


    public void initialize(URL location, ResourceBundle resources) {


        setListeners();
        Platform.runLater(this::initNodesFromDb);


    }

    public InfoController getInfoController() {
        return infoController;
    }

    private void initNodesFromDb() {

        try (Connection con = DbConnection.getDbConnection()) {

            NodeDao nodeDao = new NodeDao(con);
            for (EntityNode n : nodeDao.getAll()) {
                PhysicsNode item = new PhysicsNode(this);
                item.setLayoutX(n.getX());
                item.setLayoutY(n.getY());
                item.setPrefWidth(n.getWidth());
                item.setPrefHeight(n.getHeight());
                ImageView imageview = new ImageView(crate);
                item.getChildren().add(imageview);

                item.setUserData(n);

                item.setActive();
                canvas.addItem(item);
            }
            con.close();
        } catch (Exception e) {

        }


    }

    private void setListeners() {

        Platform.runLater(()-> {
            canvas = new CustomPane(centerPane.getWidth(), centerPane.getHeight());


            canvas .setListenerDelegate(this);
            App.instance .scene.setOnKeyPressed (it->{

                if (it.getCode()== KeyCode.ENTER)
                    currentDrawer.finishDrawing();
            });
            root.prefWidthProperty().bind(App.instance.stage.widthProperty());
            root.prefHeightProperty().bind(App.instance.stage.heightProperty());
            centerPane.getChildren().add(canvas);
            AnchorPane.setTopAnchor(canvas, 0.0);
            AnchorPane.setBottomAnchor(canvas, 0.0);
            AnchorPane.setLeftAnchor(canvas, 0.0);
            AnchorPane.setRightAnchor(canvas, 0.0);
            root.setDividerPositions(.1, .8);

            setLeftPane();
            setRightPane();
            initActions();
            setupMenu();

        });
    }

    private void setupMenu() {
        ToggleButton tb1 = new ToggleButton("Select");
        tb1.setOnMouseClicked(e -> onSelect());

        tb1.setPrefSize(76.0, 45.0);
        ToggleButton tb2 = new ToggleButton("Move");
        tb2.setOnMouseClicked((e) -> onMove());
        tb2.setPrefSize(76.0, 45.0);
        ToggleButton tb3 = new ToggleButton("Rotate");
        tb3.setPrefSize(76.0, 45.0);
        tb3.setOnMouseClicked(e -> onRotate());
        ToggleGroup group = new ToggleGroup();
        tb1.setToggleGroup(group);
        tb2.setToggleGroup(group);
        tb3.setToggleGroup(group);
        group.selectToggle(tb1);
        toolbar.getChildren().addAll(tb1, tb2, tb3);


    }

    private void setRightPane() {
        infoController.setNameInfo("Test message");
    }


    private void setLeftPane() {


        TreeItem<Path> treeRoot = new TreeItem<>(Paths.get("resources"));

        try {
            fillTreeView(Paths.get(getClass().getResource("/").toURI()), treeRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }


        resourcesTreeview.setShowRoot(true);
        resourcesTreeview.setRoot(treeRoot);

        resourcesTreeview.setCellFactory(e -> new TreeItemPath());

        resourcesTreeview.setOnMouseClicked(e -> {
            TreeItem<Path> selected = resourcesTreeview.getSelectionModel().getSelectedItem();

            if (selected != null && selected.isLeaf()) {


                Image image = new Image(selected.getValue().toUri().toString());

                ImageView imageView = new ImageView(image);


                PhysicsNode pane = new PhysicsNode(this);

                pane.getChildren().addAll(imageView);
                canvas.addItem(pane);
            }
        });

    }


    private void initActions() {

        actions.put(ACTION_SELECT, new SelectAction(rightPane, canvas, views));
        actions.put(ACTION_POLYGON, new PolygonDrawer(canvas, views));
        actions.put(ACTION_CIRCLE, new CircleDrawer(canvas, views));
        actions.put(ACTION_RECTANGLE, new RectangleDrawer(canvas, views));
        actions.put(ACTION_CHAIN, new LineDrawer(canvas, views));
        actions.put(ACTION_MOVE, new MoverAction(canvas, views));
        actions.put(ACTION_ROTATE, new RotateAction(canvas, views));
        actions.put(ACTION_EDIT, new EditAction(canvas, views));
        actions.put(ACTION_CREATE_BODY, new CreateBodyAction(canvas, views));
        actions.put(ACTION_CREATE_JOINT, new CreateJointAction(canvas, views));

        switchDrawer(ACTION_SELECT);
    }

    private void fillTreeView(Path dir, TreeItem<Path> root) throws IOException{


        Files.list(dir).forEach(it -> {
            if (!it.toFile().isDirectory()) {
                TreeItem<Path> treeItem2 = new TreeItem<>(it);
                root.getChildren().add(treeItem2);
                ImageView folderIcon = new ImageView(imageIcon);

                folderIcon.setFitHeight(20);
                treeItem2.setGraphic(folderIcon);
                folderIcon.setFitWidth(20);
            } else {
                TreeItem <Path> newRoot = new TreeItem < > (it);
                root.getChildren().add(newRoot);
//                if(it.toFile().name.endsWith(".png") || it.toFile().name.endsWith(".jpg")){
                ImageView folderIcon = new ImageView(folderImage);
                folderIcon.setFitWidth(20);
                folderIcon.setFitHeight(20);
                newRoot.setGraphic(folderIcon);


                try {
                    fillTreeView(it, newRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void onSave(ActionEvent event) {

        try(Connection connection = DbConnection.getDbConnection()){
            connection.setAutoCommit(false);
            NodeDao dao = new NodeDao(connection);
            for (Node n : canvas.getRoot().getChildren()) {
                PhysicsNode physicsNode = (PhysicsNode) n;
                EntityNode entity = (EntityNode) physicsNode.getUserData();
                dao.update(entity);




            }
            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
        }



//        val node = event.target as MenuItem
//        val window = node.scene.window
//        node.fire()
//        val fileChooser = FileChooser()
//        fileChooser.setSelectedExtensionFilter(FileChooser.ExtensionFilter("Level files", ".level"));
//        var file = fileChooser.showSaveDialog(window);


    }

    public void onLoad(ActionEvent event) {
        Node node = (Node) event.getTarget();
        Window window = node.getScene().getWindow();


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open level");

        File file = fileChooser.showOpenDialog(window);


    }


    public void onAdd(ActionEvent event) {
        Node node = (Node) event.getTarget();
        Window window = node.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Resource File");
        File imageDir = directoryChooser.showDialog(window);

        for (File f : imageDir.listFiles()) {
            addTile(f);
        }
    }

    void addTile(File file) {

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

        onImageClicked(imageView);
        tilePane.getChildren().add(pane);


    }


    void onImageClicked(ImageView _imageView) {

        _imageView.setOnMouseClicked((it) -> {


            ImageView imageView = (ImageView) it.getSource();

//            imageView.getParent().setStyle("-fx-border-color: #ff005e;-fx-border-width: 5px;-fx-border-radius: 5px;");
//            if (mPane != null)
//                (mPane as StackPane).setStyle("-fx-border-color: #000000;-fx-border-width: 5px;-fx-border-radius: 5px;");
//
//            mPane =  imageView.getParent() as StackPane
        });
    }


    public void onRun(ActionEvent actionEvent) {

        box2dDialog.show();
    }

    public void onMove() {

        switchDrawer(ACTION_MOVE);
    }


    public void onPolygon(ActionEvent event) {
        switchDrawer(ACTION_POLYGON);
    }

    public void onChain(ActionEvent event) {
        switchDrawer(ACTION_CHAIN);
    }

    public void onCircle(ActionEvent event) {


        PhysicsNode physicsNode = new PhysicsNode(this);
        physicsNode.getChildren().add(new Circle(10.0, 10.0, 5.0));
        physicsNode.getChildren().add(new Circle(-10.0, -10.0, 5.0));
        physicsNode.getChildren().add(new Circle(10.0, -10.0, 5.0));
        canvas.addItem(physicsNode);


        switchDrawer(ACTION_CIRCLE);
    }


    private void switchDrawer(String action) {
        if (currentDrawer != null) currentDrawer.finishDrawing();
        currentDrawer = actions.get(action);
        currentDrawer.init();
    }

    void onRotate() {
        switchDrawer(ACTION_ROTATE);
    }

    void onEdit(ActionEvent event) {
        switchDrawer(ACTION_EDIT);
    }

    void onSelect() {
        switchDrawer(ACTION_SELECT);
    }

    public void onCreateBody(ActionEvent event) {
        switchDrawer(ACTION_CREATE_BODY);
    }

    public void onCreateJoint(ActionEvent event) {
        switchDrawer(ACTION_CREATE_JOINT);
    }


    @Override
    public void onMousePressed(Point point) {
        currentDrawer.mousePressed(point);
    }

    @Override
    public void onMouseReleased(Point point) {
        currentDrawer.mouseReleased(point);
    }

    @Override
    public void onMouseDragged(Point point) {
        currentDrawer.mouseMoved(point);
    }


}
