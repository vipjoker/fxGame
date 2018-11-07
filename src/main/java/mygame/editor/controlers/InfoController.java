package mygame.editor.controlers;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.GridPane;
import mygame.editor.App;
import mygame.editor.model.Point;
import mygame.editor.model.Selectable;
import mygame.editor.views.CcNode;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by oleh on 3/27/17.
 */
public class InfoController implements Initializable {
    public GridPane gpInfo;
    public TextField etName;
    public TextField etX;
    public TextField etY;
    public TextField etWidth;
    public TextField etHeight;
    public ComboBox<String> comboType;
    public TextField etRotation;
    public Spinner<Double> spFriction;
    public Spinner<Double> spRestitution;
    public Spinner<Double> spDensity;
    public Label tvX;
    public Label tvY;
    public Label tvWidth;
    public Label tvHeight;
    private CcNode ccNode;
    private Runnable mCallback;
    Pattern floatNumberPattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
    DoubleProperty property = new SimpleDoubleProperty(10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.instance.registerController(this);

        ObservableList<String> strings = FXCollections.observableArrayList("Static", "Kinematic", "Dynamic");

        App.instance.selected.addListener(this::onSelected);

        comboType.setItems(strings);

        setListeners();
        initSpinners();
        setupScrollers();
    }

    private void onSelected(ListChangeListener.Change<? extends Selectable> c) {
        i
    }

    private double counter = 0;
    private double value;
    private void setupScrollers() {
        tvX.setCursor(Cursor.H_RESIZE);


        tvX.setOnMouseReleased(event -> counter = 0);
        tvX.setOnMouseDragged(event->{
            if(counter == 0){
                counter = event.getX();
            }else{

                double temp = event.getX() - counter;
                value += temp;
                tvX.setText(String.valueOf(value));
                counter = event.getX();
            }

        });
    }

    public void setNode(CcNode node, Runnable runnable) {
        this.ccNode = node;
        this.mCallback = runnable;

        etX.setText(String.valueOf(node.getX()));
        etY.setText(String.valueOf(node.getY()));
        etWidth.setText(String.valueOf(node.getWidth()));
        etHeight.setText(String.valueOf(node.getHeight()));
    }

    private void initSpinners() {
        spFriction.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0.5, 0.1));
        spDensity.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0.5, 0.1));
        spRestitution.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0.5, 0.1));
    }

    private void setListeners() {
        etX.textProperty().addListener((observable, oldValue, newValue) -> {


          if(validateNumberField(oldValue,newValue,etX)) {
                ccNode.setX(Double.parseDouble(newValue));
                mCallback.run();
            }


        });
        etY.textProperty().addListener((observable, oldValue, newValue) -> {
            if(validateNumberField(oldValue,newValue,etY)) {
                ccNode.setY(Double.parseDouble(newValue));
                mCallback.run();
            }
        });

        etWidth.textProperty().addListener((observable, oldValue, newValue) -> {

            if(validateNumberField(oldValue,newValue,etWidth)) {
                ccNode.setWidth(Double.parseDouble(newValue));
                mCallback.run();
            }
        });

        etHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if(validateNumberField(oldValue,newValue,etHeight)) {
                ccNode.setHeight(Double.parseDouble(newValue));
                mCallback.run();
            }

        });

        etRotation.textProperty().addListener((observable, oldValue, newValue) -> {
            if(validateNumberField(oldValue,newValue,etRotation)) {
                ccNode.setAngle(Double.parseDouble(newValue));
                mCallback.run();
            }
        });

    }


    private boolean validateNumberField(String oldValue, String newValue, TextField textField) {
        if (!floatNumberPattern.matcher(newValue).matches()) {

            if (newValue.endsWith(".")) {
                String replace = newValue.substring(0,newValue.length()-1);
                textField.setText(replace);
            } else if(newValue.equals("-")){
                textField.setText("0");
            }
            else {
                textField.setText(newValue.isEmpty() ? "0" : oldValue);
            }
            return false;
        }else if(newValue.length() > 1 && newValue.startsWith("0") && !newValue.contains(".")){
            textField.setText(newValue.substring(1));
        }

        return true;
    }

    public void setWidthInfo(Double width) {
        etWidth.setText(String.valueOf(width));
    }

    public void setHeightInfo(Double height) {
        etHeight.setText(String.valueOf(height));
    }

    public void setNameInfo(String name) {
        etName.setText(name);
    }

    public void setPositionInfo(Point positionInfo) {
        etX.setText(String.valueOf(positionInfo.getX()));
        etY.setText(String.valueOf(positionInfo.getY()));
    }
}
