package mygame.editor.controlers;

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
import mygame.editor.App;
import mygame.editor.actions.*;
import mygame.editor.render.TreeItemPath;
import mygame.editor.repository.NodeRepository;
import mygame.editor.repository.SqlNodeRepository;
import mygame.editor.util.Resources;
import mygame.editor.views.CcSprite;
import mygame.editor.render.CanvasRenderer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import mygame.editor.data.DbConnection;
import mygame.editor.data.NodeDao;
import mygame.editor.data.entities.EntityNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import static mygame.editor.util.Constants.*;


public class Controller implements Initializable {

    public  VBox leftPane;
    public VBox rightPane;
    public AnchorPane centerPane;
    public SplitPane root;
    public TreeView<Path> resourcesTreeview;
    public HBox toolbar;
    public Button btnRun ;
    public Button btnMove;
    public Button btnEdit;

    private CanvasRenderer canvasRenderer;
    private Action currentDrawer;
    private Map<String, Action> actions = new HashMap<>();

    @FXML
    public InfoController infoController;

    public void initialize(URL location, ResourceBundle resources) {
        setListeners();
    }

    public InfoController getInfoController() {
        return infoController;
    }

    private void setListeners() {

        Platform.runLater(()-> {

            canvasRenderer = new CanvasRenderer(centerPane);


            App.instance .scene.setOnKeyPressed (it->{

                if (it.getCode()== KeyCode.ENTER)
                    currentDrawer.finishDrawing();
            });
            root.prefWidthProperty().bind(App.instance.stage.widthProperty());
            root.prefHeightProperty().bind(App.instance.stage.heightProperty());
//            centerPane.getChildren().add(canvas);

            root.setDividerPositions(.1, .8);

            setLeftPane();
            setRightPane();
            initActions();


        });
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

                CcSprite sprite = new CcSprite(image);
                canvasRenderer.addChild(sprite);
                canvasRenderer.update();

            }
        });

    }

    private void initActions() {
        NodeRepository repository = new SqlNodeRepository();
        actions.put(ACTION_EDIT, new EditAction(canvasRenderer,repository));
        actions.put(ACTION_MOVE, new MoverAction(canvasRenderer,repository));
        actions.put(ACTION_BOX_2D, new Box2dAction(canvasRenderer,repository));

        actions.put(ACTION_SELECT, new SelectAction(canvasRenderer,repository,infoController));
        actions.put(ACTION_ROTATE, new RotateAction(canvasRenderer,repository));
        actions.put(ACTION_CREATE_BODY, new CreateBodyAction(canvasRenderer,repository));
        actions.put(ACTION_CREATE_JOINT, new CreateJointAction(canvasRenderer,repository));
        actions.put(ACTION_SPRITE, new CreateSpriteAction(canvasRenderer, repository));
        switchDrawer(ACTION_SELECT);
    }

    private void fillTreeView(Path dir, TreeItem<Path> root) throws IOException{


        Files.list(dir).forEach(it -> {
            if (!it.toFile().isDirectory()) {
                TreeItem<Path> treeItem2 = new TreeItem<>(it);
                root.getChildren().add(treeItem2);
                ImageView folderIcon = new ImageView(Resources.imageIcon);

                folderIcon.setFitHeight(20);
                treeItem2.setGraphic(folderIcon);
                folderIcon.setFitWidth(20);
            } else {
                TreeItem <Path> newRoot = new TreeItem < > (it);
                root.getChildren().add(newRoot);
                ImageView folderIcon = new ImageView(Resources.folderImage);
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

            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
        }




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


    }

    public void onRun(ActionEvent actionEvent) {

        switchDrawer(ACTION_BOX_2D);
    }

    private void switchDrawer(String action) {
        if (currentDrawer != null) currentDrawer.finishDrawing();
        currentDrawer = actions.get(action);
        currentDrawer.init();
    }

    public void onMove() {
        switchDrawer(ACTION_MOVE);
    }


    public void onEdit(ActionEvent event) {
        switchDrawer(ACTION_EDIT);
    }

    public void onRotate() {
        switchDrawer(ACTION_ROTATE);
    }

    public void onSelect() {
        switchDrawer(ACTION_SELECT);
    }

    public void onCreateBody(ActionEvent event) {
        switchDrawer(ACTION_CREATE_BODY);
    }

    public void onCreateJoint(ActionEvent event) {
        switchDrawer(ACTION_CREATE_JOINT);
    }

    public void onPolygon(ActionEvent event) {
        switchDrawer(ACTION_POLYGON);
    }

    public void onChain(ActionEvent event) {
        switchDrawer(ACTION_CHAIN);
    }

    public void onCircle(ActionEvent event) { switchDrawer(ACTION_CIRCLE);
    }

    public void onCreateSprite(ActionEvent event) {
        switchDrawer(ACTION_SPRITE);
    }
}
