package mygame.editor.controlers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;

import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import mygame.editor.App;
import mygame.editor.actions.*;
import mygame.editor.component.SpriteComponent;
import mygame.editor.model.TreeFileHolder;
import mygame.editor.render.TreeItemPath;
import mygame.editor.repository.InMemoryRepository;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Resources;
import mygame.editor.views.CcNode;
import mygame.editor.render.CanvasRenderer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import mygame.editor.data.DbConnection;
import mygame.editor.data.NodeDao;

import java.sql.Connection;

import static mygame.editor.util.Constants.*;


public class MainController implements Initializable {

    public VBox leftPane;
    public VBox rightPane;
    public AnchorPane centerPane;
    public SplitPane root;

    public TreeView<TreeFileHolder> resourcesTreeview;
    public HBox toolbar;

    private CanvasRenderer canvasRenderer;
    private Action currentDrawer;
    private Map<String, Action> actions = new HashMap<>();

    @FXML
    public InfoController infoController;

    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);
        setListeners();

    }

    public InfoController getInfoController() {
        return infoController;
    }

    private void setListeners() {

        Platform.runLater(() -> {
            canvasRenderer = new CanvasRenderer(centerPane);
            root.prefWidthProperty().bind(App.instance.stage.widthProperty());
            root.prefHeightProperty().bind(App.instance.stage.heightProperty());
            root.setDividerPositions(.2, .8);
            setLeftPane();
            setRightPane();
            initActions();
        });
    }


    private void setRightPane() {
        infoController.setNameInfo("Test message");
    }


    private void setLeftPane() {

        TreeItem<TreeFileHolder> treeRoot = new TreeItem<>(new TreeFileHolder("root", "/"));

        try {
            URI uri = getClass().getResource("/").toURI();
            File file = new File(uri);
            fillTreeView("/", file, treeRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }


        resourcesTreeview.setShowRoot(true);
        resourcesTreeview.setRoot(treeRoot);
        resourcesTreeview.setCellFactory(e -> new TreeItemPath());
        resourcesTreeview.setOnMouseClicked(e -> {
            TreeItem<TreeFileHolder> selected = resourcesTreeview.getSelectionModel().getSelectedItem();

            if (selected != null && selected.isLeaf()) {
                String path = selected.getValue().getPath();
                System.out.println(path);
                CcNode node = new CcNode();
                node.setWidth(100);
                node.setHeight(100);
                SpriteComponent spriteComponent = new SpriteComponent(path);
                node.addComponent(spriteComponent);
                canvasRenderer.addChild(node);
                canvasRenderer.update();
            }
        });

    }

    private void initActions() {
        NodeRepository repository = App.instance.repository;
        actions.put(ACTION_FIXTURE_EDIT, new FixtureEditAction(canvasRenderer, repository, infoController));
        actions.put(ACTION_BODY_EDIT, new BodyEditAction(canvasRenderer, repository, infoController));
        actions.put(ACTION_BOX_2D, new Box2dAction(canvasRenderer, repository));
        actions.put(ACTION_CREATE_SQUARE_BODY, new CreateBodyAction(canvasRenderer, repository, CreateBodyAction.Mode.SQUARE));
        actions.put(ACTION_CREATE_CIRCLE_BODY, new CreateBodyAction(canvasRenderer, repository, CreateBodyAction.Mode.CIRCLE));
        actions.put(ACTION_CREATE_CHAIN_BODY, new CreateBodyAction(canvasRenderer, repository, CreateBodyAction.Mode.CHAIN));
        actions.put(ACTION_CREATE_EDGE_BODY, new CreateBodyAction(canvasRenderer, repository, CreateBodyAction.Mode.EDGE));
        actions.put(ACTION_CREATE_JOINT, new CreateJointAction(canvasRenderer, repository));
        actions.put(ACTION_SPRITE, new CreateSpriteAction(canvasRenderer, repository));
        actions.put(ACTION_EDIT_POINTS, new EditPointsAction(canvasRenderer, repository));
        switchDrawer(ACTION_BODY_EDIT);
    }

    private void fillTreeView(String parent, File dir, TreeItem<TreeFileHolder> root) throws IOException {

        File[] files = dir.listFiles();
        if (files != null)
            for (File file : files) {
                String fullPath = parent + file.getName();
                if (!file.isDirectory()) {


                    TreeItem<TreeFileHolder> treeItem2 = new TreeItem<>(new TreeFileHolder(file.getName(), fullPath));
                    root.getChildren().add(treeItem2);
                    ImageView folderIcon = new ImageView(Resources.imageIcon);

                    folderIcon.setFitHeight(20);
                    folderIcon.setFitWidth(20);
                    treeItem2.setGraphic(folderIcon);
                } else {
                    TreeItem<TreeFileHolder> newRoot = new TreeItem<>(new TreeFileHolder(file.getName(), fullPath));
                    root.getChildren().add(newRoot);
                    ImageView folderIcon = new ImageView(Resources.folderImage);
                    folderIcon.setFitWidth(20);
                    folderIcon.setFitHeight(20);

                    newRoot.setGraphic(folderIcon);


                    try {
                        fillTreeView(fullPath + "/", file, newRoot);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

    }


    public void onSave(ActionEvent event) {

        try (Connection connection = DbConnection.getDbConnection()) {
            connection.setAutoCommit(false);
            NodeDao dao = new NodeDao(connection);

            connection.commit();
        } catch (Exception e) {
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

    public void switchDrawer(String action) {
        App.instance.observableAction.set(action);
        if (currentDrawer != null) currentDrawer.finishDrawing();
        currentDrawer = actions.get(action);
        currentDrawer.init();
    }

    public void onEditPoint(ActionEvent actionEvent) {
        switchDrawer(ACTION_EDIT_POINTS);
    }

}
