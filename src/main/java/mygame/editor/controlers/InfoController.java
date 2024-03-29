package mygame.editor.controlers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mygame.editor.App;
import mygame.editor.model.Node;
import mygame.editor.model.Physics;
import mygame.editor.model.Point;
import mygame.editor.ui.SlideableTextField;
import mygame.editor.ui.StringTextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by oleh on 3/27/17.
 */
public class InfoController implements Initializable {
    public VBox vbRoot;
    private Node ccNode;

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
    ObservableList<String> physicsType = FXCollections.observableArrayList(Physics.STATIC, Physics.KINEMATIC, Physics.DYNAMIC);
    ObservableList<String> fixtureType = FXCollections.observableArrayList(Physics.CIRCLE,Physics.RECT,Physics.CHAIN);

    ChoiceBox<String> physicsTypeChoiceBox = new ChoiceBox<>(physicsType);
    ChoiceBox<String> fixtureTypeChoiceBox = new ChoiceBox<>(fixtureType);
    private CheckBox cbHasPhysics = new CheckBox("Physics");
    VBox generalLayout = new VBox();
    VBox nodeSettingsLayout = new VBox();
    VBox physicsLayout = new VBox();
    VBox pointsBox = new VBox();
    TitledPane titledPane = new TitledPane("General",generalLayout);
    TitledPane nodeSettings = new TitledPane("Node settings",nodeSettingsLayout);
    TitledPane physicsSettingsLayout = new TitledPane("Physics",physicsLayout);

    private final List<SlideableTextField> slideableTextFields = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);

        App.instance.selected.addListener(this::onSelected);



        physicsLayout.getChildren().addAll(physicsTypeChoiceBox,etDensity,etRestitution,etFriction,fixtureTypeChoiceBox ,pointsBox);
        Accordion accordion = new Accordion(titledPane,nodeSettings,physicsSettingsLayout);

        generalLayout.getChildren().addAll(etGravityX,etGravityY,screenWidth,screenHeight);
        nodeSettingsLayout.getChildren().addAll(etName, etX, etY, etAngle, etWidth, etHeight,etAnchorX,etAnchorY,cbHasPhysics);
        vbRoot.getChildren().addAll(accordion);


        cbHasPhysics.selectedProperty().addListener(this::onCheckPhysicsChanged);
        physicsTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::onPhysicsTypeChanged);

        fixtureTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::onFixtureTypeChanged);

    }

    private void onCheckPhysicsChanged(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
        physicsSettingsLayout.setVisible(newValue);
        if(newValue){
            ccNode.setPhysics(new Physics());
        }else{
            ccNode.setPhysics(null);
        }
    }

    private void onPhysicsTypeChanged(ObservableValue<? extends String> observable, String oldValue, String newValue){
        if(ccNode != null && ccNode.getPhysics() != null){
            ccNode.getPhysics().setType(newValue);
        }
    }

    private void onFixtureTypeChanged(ObservableValue<? extends String> observable, String oldValue, String newValue){
        if(ccNode != null && ccNode.getPhysics() != null){
            ccNode.getPhysics().setShape(newValue);
        }
    }

    private javafx.scene.Node createDoubleNumberField(String name,Point point){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(name);
        label.setPrefWidth(50);
        SlideableTextField x = new SlideableTextField("X",precission);
        x.bind(point.getX());
        x.setPrefWidth(100);
        SlideableTextField y = new SlideableTextField("Y",precission);
        y.bind(point.getY());
        y.setPrefWidth(100);
        hBox.getChildren().addAll(label,x,y);
        hBox.setSpacing(5);

        slideableTextFields.add(x);
        slideableTextFields.add(y);


        return hBox;

    }

    private void onSelected(ListChangeListener.Change<? extends Node> c) {

        if (ccNode != null) {
            etX.unbind();
            etY.unbind();
            etAngle.unbind();
            etWidth.unbind();
            etHeight.unbind();
            etName.unbind();
            etAnchorX.unbind();
            etAnchorY.unbind();
            pointsBox.getChildren().clear();
            for (SlideableTextField slideableTextField : slideableTextFields) slideableTextField.unbind();
        }


        if (c.getList().size() == 1) {
            ccNode = c.getList().get(0);
            etX.bind(ccNode.getPosition().getX());
            etY.bind(ccNode.getPosition().getY());
            etName.bind(ccNode.getName());
            etWidth.bind(ccNode.getWidth());
            etHeight.bind(ccNode.getHeight());
            etAngle.bind(ccNode.getAngle());
            etAnchorX.bind(ccNode.getAnchor().getX());
            etAnchorY.bind(ccNode.getAnchor().getY());

            Physics physics = ccNode.getPhysics();
            if(physics != null) {
                switch (physics.getShape().getValue()) {
                    case Physics.CHAIN:
                        int pointCount = 1;
                        for (Point point : physics.getPoints()) {
                            pointsBox.getChildren().add(createDoubleNumberField("Point " + pointCount, point));
                            pointCount++;
                        }
                        break;
                    case Physics.CIRCLE:
                        SlideableTextField radiusField = new SlideableTextField("Radius", 5);
                        radiusField.bind(physics.getRadius());
                        slideableTextFields.add(radiusField);
                        pointsBox.getChildren().add(radiusField);
                        break;

                    case Physics.RECT:
                        SlideableTextField width = new SlideableTextField("Width", 5);

                        SlideableTextField height = new SlideableTextField("Height", 5);

                        width.bind(physics.getWidth());
                        height.bind(physics.getHeight());
                        slideableTextFields.add(width);
                        slideableTextFields.add(height);
                        pointsBox.getChildren().addAll(width, height);


                        break;
                }
                physicsTypeChoiceBox.setValue(physics.getType().get());
                fixtureTypeChoiceBox.setValue(physics.getShape().get());

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
