package mygame.editor.controlers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import mygame.editor.App;
import mygame.editor.ui.SlideableTextField;
import mygame.editor.ui.StringTextField;
import mygame.editor.util.Constants;
import mygame.editor.views.CcEditBodyNode;
import mygame.editor.views.NodeView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by oleh on 3/27/17.
 */
public class InfoController implements Initializable {
    public VBox vbRoot;
    private NodeView ccNode;

    private Pattern floatNumberPattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");

    private double precission = 10;
    private SlideableTextField etGravityX = new SlideableTextField("Gravity x",precission);
    private SlideableTextField etGravityY = new SlideableTextField("Gravity y",precission);
    private SlideableTextField screenWidth = new SlideableTextField("Screen width",precission);
    private SlideableTextField screenHeight = new SlideableTextField("Screen height",precission);

    private StringTextField etName = new StringTextField("Name");
    private SlideableTextField etX = new SlideableTextField("X",precission);
    private SlideableTextField etY = new SlideableTextField("Y",precission);
    private SlideableTextField etWidth = new SlideableTextField("Width",precission);
    private SlideableTextField etHeight = new SlideableTextField("Height",precission);
    private SlideableTextField etAngle = new SlideableTextField("Angle",5);
    private SlideableTextField etAnchorX = new SlideableTextField("Anchor x",0.1);
    private SlideableTextField etAnchorY = new SlideableTextField("Anchor y",0.1);


    private SlideableTextField etDensity = new SlideableTextField("Density",0.1);
    private SlideableTextField etRestitution = new SlideableTextField("Restitution",0.1);
    private SlideableTextField etFriction = new SlideableTextField("Friction",0.1);
    ObservableList<String> strings = FXCollections.observableArrayList("Static", "Kinematic", "Dynamic");
    ObservableList<String> fixtureType = FXCollections.observableArrayList("Circle","Rectangle","Line");

    ChoiceBox<String> choiceBox = new ChoiceBox<>(strings);
    ChoiceBox<String> fixtureTypeChoiceBox = new ChoiceBox<>(fixtureType);
    private CheckBox cbHasPhysics = new CheckBox("Physics");
    VBox generalLayout = new VBox();
    VBox nodeSettingsLayout = new VBox();
    VBox physicsLayout = new VBox();
    TitledPane titledPane = new TitledPane("General",generalLayout);
    TitledPane nodeSettings = new TitledPane("Node settings",nodeSettingsLayout);
    TitledPane physicsSettingsLayout = new TitledPane("Physics",physicsLayout);



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);

        App.instance.selected.addListener(this::onSelected);


        choiceBox.setValue(strings.get(0));
        fixtureTypeChoiceBox.setValue(fixtureType.get(0));
        physicsLayout.getChildren().addAll(choiceBox,fixtureTypeChoiceBox,etDensity,etRestitution,etFriction);
        Accordion accordion = new Accordion(titledPane,nodeSettings,physicsSettingsLayout);

        nodeSettingsLayout.getChildren().addAll(etName, etX, etY, etAngle, etWidth, etHeight,etAnchorX,etAnchorY,cbHasPhysics);
        generalLayout.getChildren().addAll(etGravityX,etGravityY,screenWidth,screenHeight);
        vbRoot.getChildren().addAll(accordion);


        cbHasPhysics.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                physicsSettingsLayout.setVisible(newValue);
                if(newValue){
                    ccNode.setEditBody(CcEditBodyNode.create(Constants.PARAM_SQUARE,0,0));
                }else{
                    ccNode.setEditBody(null);
                }
            }
        });


    }

    private void onSelected(ListChangeListener.Change<? extends NodeView> c) {

        if (ccNode != null) {
            etX.unbind();
            etY.unbind();
            etAngle.unbind();
            etWidth.unbind();
            etHeight.unbind();
            etName.unbind();
            etAnchorX.unbind();
            etAnchorY.unbind();
        }


        if (c.getList().size() == 1) {
            ccNode = c.getList().get(0);
            etX.bind(ccNode.getX());
            etY.bind(ccNode.getY());
            etName.bind(ccNode.getName());
            etWidth.bind(ccNode.getWidth());
            etHeight.bind(ccNode.getHeight());
            etAngle.bind(ccNode.getAngle());
            etAnchorX.bind(ccNode.anchorXProperty());
            etAnchorY.bind(ccNode.anchorYProperty());

            if(ccNode.getEditBody() != null){

            }

        }


    }



    private boolean validateNumberField(String oldValue, String newValue, TextField textField) {
        if (!floatNumberPattern.matcher(newValue).matches()) {

            if (newValue.endsWith(".")) {
                String replace = newValue.substring(0, newValue.length() - 1);
                textField.setText(replace);
            } else if (newValue.equals("-")) {
                textField.setText("0");
            } else {
                textField.setText(newValue.isEmpty() ? "0" : oldValue);
            }
            return false;
        } else if (newValue.length() > 1 && newValue.startsWith("0") && !newValue.contains(".")) {
            textField.setText(newValue.substring(1));
        }

        return true;
    }


}
