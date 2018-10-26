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
import mygame.editor.render.TreeItemCcNode;
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


public class Controller implements Initializable {

    public  VBox leftPane;
    public VBox rightPane;
    public AnchorPane centerPane;
    public SplitPane root;
    public TreeView<TreeFileHolder> resourcesTreeview;
    public HBox toolbar;
    public Button btnRun ;
    public Button btnMove;
    public Button btnEdit;
    public Button btnStop;
    public TreeView<CcNode> nodeTreeview;
    public Button btnPoint;

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


            root.prefWidthProperty().bind(App.instance.stage.widthProperty());
            root.prefHeightProperty().bind(App.instance.stage.heightProperty());
//            centerPane.getChildren().add(canvas);

            root.setDividerPositions(.2, .8);

            setLeftPane();
            setRightPane();
            initActions();
            CcNode node1 = new CcNode();
            node1.name = "FIRST";
            CcNode node2 = new CcNode();
            node2.name = "Second";
            CcNode node3 = new CcNode();
            node3.name = "Third";
            CcNode node4 = new CcNode();
            node4.name = "Fourth";
            node1.addChild(node2);
            node2.addChild(node3);
            node2.addChild(node4);
            updateNodeTreeView(node1);

        });
    }

    private void updateNodeTreeView(CcNode node) {

        nodeTreeview.setCellFactory(e->new TreeItemCcNode());
        TreeItem<CcNode> ccNodeTreeItem = new TreeItem<>(node);
        nodeTreeview.setRoot(ccNodeTreeItem);
        nodeTreeview.setOnMouseDragged(e->{
            TreeItem<CcNode> ccNodeTreeItem1 = nodeTreeview.getSelectionModel().getSelectedItems().get(0);

        });
        fillNodeTreeView(ccNodeTreeItem,node);

    }

    private void fillNodeTreeView(TreeItem<CcNode> ccNodeTreeItem ,CcNode root) {
        for (CcNode node : root.getChildren()) {
            TreeItem<CcNode> item = new TreeItem<>(node);

            ;



            ccNodeTreeItem.getChildren().add(item);
            if(!node.getChildren().isEmpty()){
                fillNodeTreeView(item,node);
            }


        }
    }


    private void setRightPane() {
        infoController.setNameInfo("Test message");
    }


    private void setLeftPane() {

        TreeItem<TreeFileHolder> treeRoot = new TreeItem<>(new TreeFileHolder("root","/"));

        try {
            URI uri = getClass().getResource("/").toURI();
            File file = new File(uri);
            fillTreeView("/",file, treeRoot);
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
                CcNode node=  new CcNode();
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
        NodeRepository repository = new InMemoryRepository();
        actions.put(ACTION_EDIT, new FixtureEditAction(canvasRenderer,repository,infoController));
        actions.put(ACTION_MOVE, new MoverAction(canvasRenderer,repository));
        actions.put(ACTION_BOX_2D, new Box2dAction(canvasRenderer,repository));
        actions.put(ACTION_SELECT, new SelectBodyAction(canvasRenderer,repository,infoController));
        actions.put(ACTION_ROTATE, new RotateAction(canvasRenderer,repository));
        actions.put(ACTION_CREATE_SQUARE_BODY, new CreateBodyAction(canvasRenderer,repository, CreateBodyAction.Mode.SQUARE));
        actions.put(ACTION_CREATE_CIRCLE_BODY, new CreateBodyAction(canvasRenderer,repository, CreateBodyAction.Mode.CIRCLE));
        actions.put(ACTION_CREATE_CHAIN_BODY, new CreateBodyAction(canvasRenderer,repository, CreateBodyAction.Mode.CHAIN));
        actions.put(ACTION_CREATE_EDGE_BODY, new CreateBodyAction(canvasRenderer,repository, CreateBodyAction.Mode.EDGE));
        actions.put(ACTION_CREATE_JOINT, new CreateJointAction(canvasRenderer,repository));
        actions.put(ACTION_SPRITE, new CreateSpriteAction(canvasRenderer, repository));
        actions.put(ACTION_EDIT_POINTS,new EditPointsAction(canvasRenderer,repository));
        switchDrawer(ACTION_SELECT);
    }

    private void fillTreeView(String parent,File dir, TreeItem<TreeFileHolder> root) throws IOException{

        File[] files = dir.listFiles();
        if(files != null)
        for (File file : files) {
            String fullPath =parent + file.getName();
            if (!file.isDirectory()) {


                TreeItem<TreeFileHolder> treeItem2 = new TreeItem<>(new TreeFileHolder(file.getName(),fullPath));
                root.getChildren().add(treeItem2);
                ImageView folderIcon = new ImageView(Resources.imageIcon);

                folderIcon.setFitHeight(20);
                folderIcon.setFitWidth(20);
                treeItem2.setGraphic(folderIcon);
            } else {
                TreeItem <TreeFileHolder> newRoot = new TreeItem < > (new TreeFileHolder(file.getName(),fullPath));
                root.getChildren().add(newRoot);
                ImageView folderIcon = new ImageView(Resources.folderImage);
                folderIcon.setFitWidth(20);
                folderIcon.setFitHeight(20);

                newRoot.setGraphic(folderIcon);


                try {
                    fillTreeView(fullPath + "/" , file, newRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

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


    public void onCreateSquare(ActionEvent event) {
        switchDrawer(ACTION_CREATE_SQUARE_BODY);
    }

    public void onCreateCircleBody(ActionEvent actionEvent) {
        switchDrawer(ACTION_CREATE_CIRCLE_BODY);
    }

    public void onCreateChainBody(ActionEvent actionEvent) {
        switchDrawer(ACTION_CREATE_CHAIN_BODY);
    }

    public void onCreaetJoint(ActionEvent actionEvent) {

    }

    public void onCreateRevoluteJoint(ActionEvent actionEvent) {
    }

    public void onStop(ActionEvent actionEvent) {

    }

    public void onEditPoint(ActionEvent actionEvent) {
        switchDrawer(ACTION_EDIT_POINTS);

    }
}
