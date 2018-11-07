package mygame.editor.controlers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import mygame.editor.App;
import mygame.editor.util.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateController implements Initializable{

    public Button btnSprite;
    public Button btnSquareBody;
    public Button btnCircleBody;
    public Button btnChainBody;
    public Button btnDistanceJoint;
    public Button btnRevoluteJoint;
    public Button[] buttons;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);
        App.instance.observableAction.addListener(this::onActionChanged);
        buttons = new Button[]{btnSprite,btnSquareBody,btnCircleBody,btnChainBody,btnDistanceJoint,btnRevoluteJoint};
    }

    private void onActionChanged(ObservableValue<? extends String> observableValue, String oldValue , String newValue) {
        System.out.println("New action: " + newValue);
    }

    public void onCreateSprite(ActionEvent actionEvent) {
        clearSelectoin();
        btnSprite.getStyleClass().add("selected");
        MainController controller = App.instance.getController(MainController.class);
        controller.switchDrawer(Constants.ACTION_SPRITE);
    }

    public void onCreateSquare(ActionEvent actionEvent) {
        clearSelectoin();
        btnSquareBody.getStyleClass().add("selected");

        MainController controller = App.instance.getController(MainController.class);
        controller.switchDrawer(Constants.ACTION_CREATE_SQUARE_BODY);
    }

    public void onCreateCircleBody(ActionEvent actionEvent) {
        clearSelectoin();
        btnCircleBody.getStyleClass().add("selected");

        MainController controller = App.instance.getController(MainController.class);
        controller.switchDrawer(Constants.ACTION_CREATE_CIRCLE_BODY);
    }

    public void onCreateChainBody(ActionEvent actionEvent) {
        clearSelectoin();
        btnChainBody.getStyleClass().add("selected");
        MainController controller = App.instance.getController(MainController.class);
        controller.switchDrawer(Constants.ACTION_CREATE_CHAIN_BODY );
    }

    public void onCreaetJoint(ActionEvent actionEvent) {
        clearSelectoin();
        btnDistanceJoint.getStyleClass().add("selected");

        App.instance.observableAction.set(Constants.ACTION_DISTANCE_JOINT);
    }

    public void onCreateRevoluteJoint(ActionEvent actionEvent) {
        clearSelectoin();
        btnRevoluteJoint.getStyleClass().add("selected");
        App.instance.observableAction.set(Constants.ACTION_REVOLUTE_JOINT);
    }

    private void clearSelectoin(){
        for (Button button : buttons) {
            button.getStyleClass().remove("selected");
        }
    }

}
