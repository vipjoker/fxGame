package mygame.editor.controlers;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mygame.editor.App;
import mygame.editor.actions.*;
import mygame.editor.component.SpriteComponent;
import mygame.editor.manager.FileManager;
import mygame.editor.model.Command;
import mygame.editor.model.TreeFileHolder;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.render.TreeItemPath;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Constants;
import mygame.editor.util.FileUtil;
import mygame.editor.util.Resources;
import mygame.editor.views.CcNode;
import mygame.editor.views.CcSprite;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static mygame.editor.util.Constants.*;


public class MainController implements Initializable {

    public VBox leftPane;
    public VBox rightPane;
    public AnchorPane centerPane;
    public SplitPane root;

    public TreeView<TreeFileHolder> resourcesTreeview;

    private CanvasRenderer canvasRenderer;
    private Action currentDrawer;
    private String currentDrawerName;
    private Map<String, Action> actions = new HashMap<>();

    @FXML
    public InfoController infoController;

    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);
        setListeners();

    }

    private void setListeners() {

        Platform.runLater(() -> {
            canvasRenderer = new CanvasRenderer(centerPane);
            root.prefWidthProperty().bind(App.instance.stage.widthProperty());
            root.prefHeightProperty().bind(App.instance.stage.heightProperty());
            root.setDividerPositions(.2, .8);

            initActions();

        });


        App.instance.selected.addListener(new ListChangeListener<CcNode>() {
                                              @Override
                                              public void onChanged(Change<? extends CcNode> c) {
                                                  for (CcNode node : App.instance.repository.getNodes()) {
                                                      node.setActive(c.getList().contains(node));
                                                  }
                                              }
                                          });

                App.instance.observableAction.addListener(this::onActionChanged);
    }

    private void onActionChanged(ObservableValue<? extends Command> observableValue, Command oldValue, Command newValue) {
        switchDrawer(newValue);
    }

    private void switchDrawer(Command command) {

        if(!command.action.equals(currentDrawerName)){
            if (currentDrawer != null) {
                currentDrawer.finishDrawing();
            }

            currentDrawer = actions.get(command.action);
            currentDrawer.init();
        }


        currentDrawerName = command.action;
        currentDrawer.setMode(command.param);


    }


    public void setLeftPane(File file) {

        TreeItem<TreeFileHolder> treeRoot = new TreeItem<>(new TreeFileHolder("root", "/"));

        try {

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



                final CcSprite ccSprite = new CcSprite(path,100,100);
                int count = App.instance.repository.count();
                ccSprite.getName().setValue("Sprite " + count);
                App.instance.repository.save(ccSprite);

                canvasRenderer.addChild(ccSprite);
                canvasRenderer.update();
            }
        });

    }

    private void initActions() {
        NodeRepository repository = App.instance.repository;

        actions.put(Constants.MODE_SELECT, new SelectAction(canvasRenderer, repository));
        actions.put(Constants.MODE_FIXTURE, new FixtureEditAction(canvasRenderer, repository));
        actions.put(Constants.MODE_BODY, new BodyEditAction(canvasRenderer, repository, infoController));
        actions.put(Constants.MODE_RUN, new Box2dAction(canvasRenderer, repository));
        actions.put(Constants.MODE_CREATE, new CreateBodyAction(canvasRenderer, repository));

        switchDrawer(new Command(Constants.MODE_BODY,Constants.PARAM_MOVE));
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

}
