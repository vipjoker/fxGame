package mygame.editor.controlers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;

import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import mygame.editor.App;
import mygame.editor.model.Command;
import mygame.editor.util.Constants;

import java.net.URL;
import java.util.ResourceBundle;



public class ToolbarController implements Initializable {

    private SimpleStringProperty mode = new SimpleStringProperty();
    public Button btnCreate;
    private Button btnRun = new Button("Run");
    private Button btnEditPoints = new Button("Edit points");
    private Button btnMove = new Button ("Move");
    private Button btnMoveFixture = new Button("Move");
    private Button btnRotate = new Button("Rotate") ;

    private Button btnCreateSquare = new Button("Create square");
    private Button btnCreateCircle = new Button("Create circle");
    private Button btnCreateChain = new Button("Create chain");
    private Button btnDistanceJoint = new Button("Distance joint");
    private Button btnRevoluteJoint = new Button("Revolute joint");
    private Button btnSprite = new Button("Sprite");


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

        btnCreateSquare.setOnMouseClicked(this::onCreateSquare);
        btnCreateChain.setOnMouseClicked(this::onCreateChain);
        btnCreateCircle.setOnMouseClicked(this::onCreateCircle);
        btnDistanceJoint.setOnMouseClicked(this::onCreateDistanceJoint);
        btnRevoluteJoint.setOnMouseClicked(this::onCreateRevoluteJoint);
        btnSprite.setOnMouseClicked(this::onCreateSprite);

    }

    private void onCreateDistanceJoint(MouseEvent mouseEvent) {

    }

    private void onCreateCircle(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnCreateCircle.getStyleClass().add("selected");
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE,Constants.PARAM_CIRCLE));
    }


    private void onCreateChain(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnCreateChain.getStyleClass().add("selected");
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE,Constants.PARAM_CHAIN));
    }

    private void onCreateSquare(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnCreateSquare.getStyleClass().add("selected");
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE,Constants.PARAM_SQUARE));
    }

    private void onBodyRotate(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnRotate.getStyleClass().add("selected");
        App.instance.observableAction.setValue(new Command(Constants.MODE_BODY,Constants.PARAM_ROTATE));
    }

    private void onFixtureMove(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnMoveFixture.getStyleClass().add("selected");
        App.instance.observableAction.setValue(new Command(Constants.MODE_FIXTURE,Constants.PARAM_MOVE));

    }

    private void onBodyMove(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnMove.getStyleClass().add("selected");
        App.instance.observableAction.setValue(new Command(Constants.MODE_BODY,Constants.PARAM_MOVE));
    }

    public void onEditPoint(MouseEvent actionEvent) {
        MainController controller = App.instance.getController(MainController.class);
        clearEditableSelection();
        btnEditPoints.getStyleClass().add("selected");

        App.instance.observableAction.set(new Command(Constants.MODE_FIXTURE,Constants.PARAM_EDIT_POINTS));
    }

    public void onRun(MouseEvent actionEvent) {
        clearEditableSelection();
        btnRun.getStyleClass().add("selected");
        App.instance.observableAction.set(new Command(Constants.MODE_RUN,Constants.PARAM_RUN));

    }

    private void clearEditableSelection(){
        for (Button editButton :bodyActions) editButton.getStyleClass().remove("selected");
        for (Button editButton :fixtureActions) editButton.getStyleClass().remove("selected");
        for(Button createButton: createActions)createButton.getStyleClass().remove("selected");

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

        App.instance.observableAction.set(new Command(Constants.MODE_FIXTURE,Constants.PARAM_MOVE));

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
         App.instance.observableAction.set(new Command(Constants.MODE_BODY,Constants.PARAM_MOVE));
    }

    public void onCreateMode(ActionEvent actionEvent) {
        btnBody.getStyleClass().remove("selected");
        btnFixture.getStyleClass().remove("selected");
        btnCreate.getStyleClass().add("selected");

        hBoxActions.getChildren().clear();
        hBoxActions.getChildren().addAll(createActions);

    }





    public void onCreateSprite(MouseEvent actionEvent) {
        //clearSelectoin();
        btnSprite.getStyleClass().add("selected");
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE,Constants.ACTION_SPRITE));
    }





    public void onCreaetJoint(MouseEvent actionEvent) {
//        clearSelectoin();
        btnDistanceJoint.getStyleClass().add("selected");

        App.instance.observableAction.set(new Command(Constants.MODE_CREATE,Constants.PARAM_DISTANCE));
    }

    public void onCreateRevoluteJoint(MouseEvent actionEvent) {
//        clearSelectoin();

        btnRevoluteJoint.getStyleClass().add("selected");
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE,Constants.PARAM_REVOLUTE));
    }

}




