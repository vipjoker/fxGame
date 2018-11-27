package mygame.editor.controlers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import mygame.editor.App;
import mygame.editor.model.Point;
import mygame.editor.ui.SlideableTextField;
import mygame.editor.ui.StringTextField;
import mygame.editor.views.CcNode;

import java.net.URL;
import java.text.*;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by oleh on 3/27/17.
 */
public class InfoController implements Initializable {
    public VBox vbRoot;
    private CcNode ccNode;

    private Pattern floatNumberPattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
    private StringTextField etName = new StringTextField("Name");
    private SlideableTextField etX = new SlideableTextField("X");
    private SlideableTextField etY = new SlideableTextField("Y");
    private SlideableTextField etWidth = new SlideableTextField("Width");
    private SlideableTextField etHeight = new SlideableTextField("Height");
    private SlideableTextField etAngle = new SlideableTextField("Angle");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);

        ObservableList<String> strings = FXCollections.observableArrayList("Static", "Kinematic", "Dynamic");

        App.instance.selected.addListener(this::onSelected);


        vbRoot.getChildren().addAll(etName, etX, etY, etAngle, etWidth, etHeight);


    }

    private void onSelected(ListChangeListener.Change<? extends CcNode> c) {

        if (ccNode != null) {
            etX.unbind();
            etY.unbind();
            etAngle.unbind();
            etWidth.unbind();
            etHeight.unbind();
            etName.unbind();
        }


        if (c.getList().size() == 1) {
            ccNode = c.getList().get(0);
            etX.bind(ccNode.getX());
            etY.bind(ccNode.getY());
            etName.bind(ccNode.getName());
            etWidth.bind(ccNode.getWidth());
            etHeight.bind(ccNode.getHeight());
            etAngle.bind(ccNode.getAngle());
        }


    }


    private void initSpinners() {
//        spFriction.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0.5, 0.1));
//        spDensity.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0.5, 0.1));
//        spRestitution.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0.5, 0.1));
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
