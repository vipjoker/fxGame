package mygame.editor.controlers;

import javafx.beans.value.ObservableValue;
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

    private final static String SELECTED = "selected";

    private Button btnEditPoints = new Button("Edit points");
    private Button btnMove = new Button("Move");
    private Button btnMoveFixture = new Button("Move");
    private Button btnRotate = new Button("Rotate");

    private Button btnCreateSquare = new Button("Create square");
    private Button btnCreateCircle = new Button("Create circle");
    private Button btnCreateChain = new Button("Create chain");
    private Button btnDistanceJoint = new Button("Distance joint");
    private Button btnRevoluteJoint = new Button("Revolute joint");
    private Button btnSprite = new Button("Sprite");

    private Button[] bodyActions = {btnMove, btnRotate};
    private Button[] fixtureActions = {btnMoveFixture, btnEditPoints};
    private Button[] createActions = {btnCreateSquare, btnCreateCircle, btnCreateChain};

    public Button btnCreate;
    public Button btnRun;
    public Button btnBody;
    public Button btnFixture;
    public HBox hBoxActions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);
        btnMove.setOnMouseClicked(this::onBodyMove);
        btnRotate.setOnMouseClicked(this::onBodyRotate);
        btnMoveFixture.setOnMouseClicked(this::onFixtureMove);
        btnEditPoints.setOnMouseClicked(this::onEditPoint);

        btnCreateSquare.setOnMouseClicked(this::onCreateSquare);
        btnCreateChain.setOnMouseClicked(this::onCreateChain);
        btnCreateCircle.setOnMouseClicked(this::onCreateCircle);
        btnDistanceJoint.setOnMouseClicked(this::onCreateDistanceJoint);
        btnRevoluteJoint.setOnMouseClicked(this::onCreateRevoluteJoint);
        btnSprite.setOnMouseClicked(this::onCreateSprite);
        App.instance.observableAction.addListener(this::onActionChanged);
    }

    private void onCreateDistanceJoint(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnDistanceJoint.getStyleClass().add(SELECTED);

        App.instance.observableAction.set(new Command(Constants.MODE_CREATE, Constants.PARAM_DISTANCE));
    }

    private void onCreateCircle(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnCreateCircle.getStyleClass().add(SELECTED);
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE, Constants.PARAM_CIRCLE));
    }


    private void onCreateChain(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnCreateChain.getStyleClass().add(SELECTED);
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE, Constants.PARAM_CHAIN));
    }

    private void onCreateSquare(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnCreateSquare.getStyleClass().add(SELECTED);
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE, Constants.PARAM_SQUARE));
    }

    private void onBodyRotate(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnRotate.getStyleClass().add(SELECTED);
        App.instance.observableAction.setValue(new Command(Constants.MODE_BODY, Constants.PARAM_ROTATE));
    }

    private void onFixtureMove(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnMoveFixture.getStyleClass().add(SELECTED);
        App.instance.observableAction.setValue(new Command(Constants.MODE_FIXTURE, Constants.PARAM_MOVE));

    }

    private void onBodyMove(MouseEvent mouseEvent) {
        clearEditableSelection();
        btnMove.getStyleClass().add(SELECTED);
        App.instance.observableAction.setValue(new Command(Constants.MODE_BODY, Constants.PARAM_MOVE));
    }

    public void onEditPoint(MouseEvent actionEvent) {
        MainController controller = App.instance.getController(MainController.class);
        clearEditableSelection();
        btnEditPoints.getStyleClass().add(SELECTED);

        App.instance.observableAction.set(new Command(Constants.MODE_FIXTURE, Constants.PARAM_EDIT_POINTS));
    }

    public void onRun(ActionEvent actionEvent) {
        clearEditableSelection();
        btnRun.getStyleClass().add(SELECTED);
        App.instance.observableAction.set(new Command(Constants.MODE_RUN, Constants.PARAM_RUN));

    }

    private void clearEditableSelection() {
        for (Button editButton : bodyActions) editButton.getStyleClass().remove(SELECTED);
        for (Button editButton : fixtureActions) editButton.getStyleClass().remove(SELECTED);
        for (Button createButton : createActions) createButton.getStyleClass().remove(SELECTED);
        for (Button btn : new Button[]{btnCreate, btnRun, btnBody, btnFixture}) {
            btn.getStyleClass().remove(SELECTED);
        }


    }


    public void onFixtureMode(ActionEvent actionEvent) {
        App.instance
                .observableAction
                .set(new Command(Constants.MODE_FIXTURE, Constants.PARAM_MOVE));

    }

    public void onBodyMode(ActionEvent actionEvent) {
        App.instance
                .observableAction
                .set(new Command(Constants.MODE_BODY, Constants.PARAM_MOVE));


    }

    public void onCreateMode(ActionEvent actionEvent) {

        App.instance
                .observableAction
                .set(new Command(Constants.MODE_CREATE, Constants.PARAM_SQUARE));


    }

    public void onSelect(ActionEvent actionEvent) {

    }

    public void onCreateSprite(MouseEvent actionEvent) {
        //clearSelectoin();
        btnSprite.getStyleClass().add(SELECTED);
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE, Constants.ACTION_SPRITE));
    }


    public void onCreateRevoluteJoint(MouseEvent actionEvent) {
//        clearSelectoin();

        btnRevoluteJoint.getStyleClass().add(SELECTED);
        App.instance.observableAction.set(new Command(Constants.MODE_CREATE, Constants.PARAM_REVOLUTE));
    }


    public void onActionChanged(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) {
        clearEditableSelection();
        switch (newValue.action) {
            case Constants.MODE_CREATE:
                btnCreate.getStyleClass().add(SELECTED);
                hBoxActions.getChildren().clear();
                hBoxActions.getChildren().addAll(createActions);
                switch (newValue.param) {
                    case Constants.PARAM_SQUARE:
                        btnCreateSquare.getStyleClass().add(SELECTED);
                        break;
                    case Constants.PARAM_CIRCLE:
                        btnCreateCircle.getStyleClass().add(SELECTED);
                        break;
                    case Constants.PARAM_CHAIN:
                        btnCreateChain.getStyleClass().add(SELECTED);
                        break;
                }
                break;
            case Constants.MODE_BODY:
                btnBody.getStyleClass().add(SELECTED);
                hBoxActions.getChildren().clear();
                hBoxActions.getChildren().addAll(bodyActions);
                switch (newValue.param) {
                    case Constants.PARAM_MOVE:
                        btnMove.getStyleClass().add(SELECTED);
                        break;
                    case Constants.PARAM_ROTATE:
                        btnRotate.getStyleClass().add(SELECTED);
                        break;
                }
                break;
            case Constants.MODE_FIXTURE:
                btnFixture.getStyleClass().add(SELECTED);
                hBoxActions.getChildren().clear();
                hBoxActions.getChildren().addAll(fixtureActions);
                switch (newValue.param) {
                    case Constants.PARAM_MOVE:
                        btnMoveFixture.getStyleClass().add(SELECTED);
                        break;
                    case Constants.PARAM_EDIT_POINTS:
                        btnEditPoints.getStyleClass().add(SELECTED);
                        break;
                }
                break;
            case Constants.MODE_RUN:
                btnRun.getStyleClass().add(SELECTED);


                break;
        }
    }

}




