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
import static mygame.editor.util.Constants.ACTION_EDIT_POINTS;


public class ToolbarController implements Initializable {
    private Button btnRun = new Button("Run");
    private Button btnEditPoints = new Button("Edit points");
    private Button btnMove = new Button ("Move");
    private Button btnMoveFixture = new Button("Move");
    private Button btnRotate = new Button("Rotate") ;

    private Button [] bodyActions = {btnMove,btnRotate,btnRun};
    private Button [] fixtureActions = {btnMoveFixture,btnEditPoints,btnRun};



    public Button btnBody;
    public Button btnFixture;
    public HBox hBoxActions;
    private boolean isBodyMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);
        btnMove.setOnMouseClicked(this::onBodyMove);
        btnRotate.setOnMouseClicked(this::onBodyRotate);
        btnRun.setOnMouseClicked(this::onRun);
        btnMoveFixture.setOnMouseClicked(this::onFixtureMove);
        btnEditPoints.setOnMouseClicked(this::onEditPoint);
        onBodyMode(null);
    }

    private void onBodyRotate(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnRotate.getStyleClass().add("selected");
    }

    private void onFixtureMove(MouseEvent mouseEvent) {
        MainController controller = App.instance.getConroller(MainController.class);
        clearEditableSelection();
        btnMoveFixture.getStyleClass().add("selected");
        controller.switchDrawer(Constants.ACTION_FIXTURE_EDIT);

    }

    private void onBodyMove(MouseEvent mouseEvent) {
        MainController controller = App.instance.getConroller(MainController.class);
        clearEditableSelection();
        btnMove.getStyleClass().add("selected");
        controller.switchDrawer(Constants.ACTION_BODY_EDIT);


    }







    public void onEditPoint(MouseEvent actionEvent) {
        MainController controller = App.instance.getConroller(MainController.class);
        clearEditableSelection();
        btnEditPoints.getStyleClass().add("selected");

        App.instance.observableAction.set(ACTION_EDIT_POINTS);
    }

    public void onRun(MouseEvent actionEvent) {
        MainController controller = App.instance.getConroller(MainController.class);
        clearEditableSelection();
        btnRun.getStyleClass().add("selected");
        controller.switchDrawer(ACTION_BOX_2D);

    }

    private void clearEditableSelection(){
        for (Button editButton :bodyActions) editButton.getStyleClass().remove("selected");
        for (Button editButton :fixtureActions) editButton.getStyleClass().remove("selected");

    }



    public void onFixtureMode(ActionEvent actionEvent) {
        if(!isBodyMode){
            return;
        }
        isBodyMode = false;
        btnFixture.getStyleClass().add("selected");
        btnBody.getStyleClass().remove("selected");

        hBoxActions.getChildren().clear();
        hBoxActions.getChildren().addAll(fixtureActions);
    }

    public void onBodyMode(ActionEvent actionEvent) {
        if(isBodyMode){
            return;
        }

        isBodyMode  = true;
        btnBody.getStyleClass().add("selected");
        btnFixture.getStyleClass().remove("selected");

        hBoxActions.getChildren().clear();
        hBoxActions.getChildren().addAll(bodyActions);
    }
}
