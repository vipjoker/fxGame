package mygame.editor.controlers;

import javafx.event.ActionEvent;

import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import mygame.editor.App;
import mygame.editor.util.Constants;

import java.net.URL;
import java.util.ResourceBundle;

import static mygame.editor.util.Constants.ACTION_BOX_2D;


public class ToolbarController implements Initializable {
    public Button btnCreate;
    private Button btnRun = new Button("Run");
    private Button btnEditPoints = new Button("Edit points");
    private Button btnMove = new Button ("Move");
    private Button btnMoveFixture = new Button("Move");
    private Button btnRotate = new Button("Rotate") ;

    private Button btnCreateSquare = new Button("Create square");
    private Button btnCreateCircle = new Button("Create circle");
    private Button btnCreateChain = new Button("Create chain");


    private Button [] bodyActions = {btnMove,btnRotate,btnRun};
    private Button [] fixtureActions = {btnMoveFixture,btnEditPoints,btnRun};
    private Button [] createActions = {btnCreateSquare,btnCreateCircle,btnCreateChain};


    public Button btnBody;
    public Button btnFixture;
    public HBox hBoxActions;
    private boolean isBodyMode = false;
    private boolean isFixtureMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);
        btnMove.setOnMouseClicked(this::onBodyMove);
        btnRotate.setOnMouseClicked(this::onBodyRotate);
        btnRun.setOnMouseClicked(this::onRun);
        btnMoveFixture.setOnMouseClicked(this::onFixtureMove);
        btnEditPoints.setOnMouseClicked(this::onEditPoint);

    }

    private void onBodyRotate(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnRotate.getStyleClass().add("selected");
        App.instance.observableAction.setValue(Constants.ACTION_ROTATE);
    }

    private void onFixtureMove(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnMoveFixture.getStyleClass().add("selected");
        App.instance.observableAction.setValue(Constants.ACTION_MOVE);

    }

    private void onBodyMove(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnMove.getStyleClass().add("selected");
        App.instance.observableAction.setValue(Constants.ACTION_MOVE);
    }

    public void onEditPoint(MouseEvent actionEvent) {
        MainController controller = App.instance.getController(MainController.class);
        clearEditableSelection();
        btnEditPoints.getStyleClass().add("selected");

        App.instance.observableAction.set(Constants.ACTION_POINT_EDIT);
    }

    public void onRun(MouseEvent actionEvent) {
        MainController controller = App.instance.getController(MainController.class);
        clearEditableSelection();
        btnRun.getStyleClass().add("selected");
        controller.switchDrawer(ACTION_BOX_2D);

    }

    private void clearEditableSelection(){
        for (Button editButton :bodyActions) editButton.getStyleClass().remove("selected");
        for (Button editButton :fixtureActions) editButton.getStyleClass().remove("selected");

    }



    public void onFixtureMode(ActionEvent actionEvent) {
        if(isFixtureMode){
            return;
        }
        isFixtureMode = true;
        isBodyMode = false;
        btnFixture.getStyleClass().add("selected");
        btnBody.getStyleClass().remove("selected");
        btnCreate.getStyleClass().remove("selected");

        hBoxActions.getChildren().clear();
        hBoxActions.getChildren().addAll(fixtureActions);

        MainController controller = App.instance.getController(MainController.class);
        controller.switchDrawer(Constants.ACTION_FIXTURE_EDIT);

    }

    public void onBodyMode(ActionEvent actionEvent) {
        if(isBodyMode){
            return;
        }

        isBodyMode = true;
        isFixtureMode = false;
         onBodyMove(null);
        btnBody.getStyleClass().add("selected");
        btnFixture.getStyleClass().remove("selected");
        btnCreate.getStyleClass().remove("selected");

        hBoxActions.getChildren().clear();
        hBoxActions.getChildren().addAll(bodyActions);
        MainController controller = App.instance.getController(MainController.class);
        controller.switchDrawer(Constants.ACTION_BODY_EDIT);
    }

    public void onCreateMode(ActionEvent actionEvent) {
        btnBody.getStyleClass().remove("selected");
        btnFixture.getStyleClass().remove("selected");
        btnCreate.getStyleClass().add("selected");

        hBoxActions.getChildren().clear();
        hBoxActions.getChildren().addAll(createActions);

    }
}
