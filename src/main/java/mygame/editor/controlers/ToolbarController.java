package mygame.editor.controlers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import mygame.editor.App;
import mygame.editor.model.Command;
import mygame.editor.util.Constants;

import java.net.URL;
import java.util.ResourceBundle;


public class ToolbarController implements Initializable {

    private final static String SELECTED = "selected";
    public HBox mainActions;
    public HBox secondaryActions;
    private ToggleButton nodeMove;
    private ToggleButton nodeRotate;
    private ToggleButton fixtureMove;
    private ToggleButton fixtureEdit;
    private Command lastCommand;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);


        ToggleGroup mainToggleGroup = new ToggleGroup();
        ToggleButton nodeToggle=  new ToggleButton("Node");
        ToggleButton runToggle = new ToggleButton("Run");
        nodeToggle.setUserData("Node");

        ToggleButton fixtureToggle=  new ToggleButton("Fixture");
        fixtureToggle.setUserData("Fixture");

        mainToggleGroup.getToggles().addAll(nodeToggle,fixtureToggle);
        mainActions.getChildren().addAll(nodeToggle,fixtureToggle,runToggle);
        runToggle.selectedProperty().addListener(this::onRunChanged);
        ToggleGroup nodeToggleGroup  = new ToggleGroup();

        nodeMove = new ToggleButton("Move");
        Command nodeMoveCommand = new Command(Constants.MODE_NODE,Constants.PARAM_MOVE);
        nodeMove.setUserData(nodeMoveCommand);

        nodeRotate = new ToggleButton("Rotate");
        Command nodeRotateCommand = new Command(Constants.MODE_NODE,Constants.PARAM_ROTATE);
        nodeRotate.setUserData(nodeRotateCommand);
        nodeToggleGroup.getToggles().addAll(nodeMove,nodeRotate);

        ToggleGroup fixtureToggleGroup = new ToggleGroup();
        fixtureMove = new ToggleButton("Move");
        fixtureEdit = new ToggleButton("Edit");
        fixtureToggleGroup.getToggles().addAll(fixtureMove,fixtureEdit);
        final Command fixtureCommand = new Command(Constants.MODE_FIXTURE, Constants.PARAM_MOVE);
        fixtureMove.setUserData(fixtureCommand);

        final Command fixtureEditCommand = new Command(Constants.MODE_FIXTURE,Constants.PARAM_EDIT_POINTS);
        fixtureEdit.setUserData(fixtureEditCommand);

        nodeToggleGroup.selectedToggleProperty().addListener(this::onSecondaryActionChanged);
        fixtureToggleGroup.selectedToggleProperty().addListener(this::onSecondaryActionChanged);

        App.instance.observableAction.addListener(this::onActionChanged);




        mainToggleGroup.selectedToggleProperty().addListener(this::onModeChanged);
        nodeToggle.fire();

    }

    public void onRunChanged(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
        if(newValue){
            lastCommand = App.instance.observableAction.get();

            App.instance.observableAction.set(new Command(Constants.MODE_RUN,""));
        }else{
            App.instance.observableAction.setValue(lastCommand);
        }
    }

    private void onSecondaryActionChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue){
        if (newValue != null) {
            Command command = (Command) newValue.getUserData();
            App.instance.observableAction.set(command);
        }else {
            oldValue.setSelected(true);
        }
    }



    private void onModeChanged (ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue){
        secondaryActions.getChildren().clear();
        if(newValue == null){
           oldValue.setSelected(true);
            return;
        }
        switch (newValue.getUserData().toString()){
            case "Node":
                secondaryActions.getChildren().addAll(nodeMove,nodeRotate);
                nodeMove.fire();
                break;
            case "Fixture":
                secondaryActions.getChildren().addAll(fixtureMove,fixtureEdit);
                fixtureMove.fire();
                break;
        }
    }

    private void onActionChanged(ObservableValue<? extends Command> observableValue, Command object, Command object1) {

    }


}




