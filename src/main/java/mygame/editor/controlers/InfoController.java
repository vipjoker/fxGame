package mygame.editor.controlers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.GridPane;
import mygame.editor.model.Point;
import mygame.editor.views.CcNode;

import java.net.URL;
import java.util.ResourceBundle;

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
    private CcNode ccNode;
    private Runnable mCallback;
    DoubleProperty property = new SimpleDoubleProperty(10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> strings = FXCollections.observableArrayList("Static", "Kinematic", "Dynamic");

        comboType.setItems(strings);

        setListeners();
        initSpinners();
    }

    public void setNode(CcNode node,Runnable runnable){
        System.out.println(node);
        this.ccNode = node;
        System.out.println(node.x);
        this.mCallback = runnable;

        etX.setText(String.valueOf(node.x));
        etY.setText(String.valueOf(node.y));
        etWidth.setText(String.valueOf(node.width));
        etHeight.setText(String.valueOf(node.height));
    }

    private void initSpinners() {
        spFriction.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1,0.5,0.1));
        spDensity.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1,0.5,0.1));
        spRestitution.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1,0.5,0.1));
    }

    private void setListeners() {
        etX.textProperty().addListener((observable, oldValue, newValue) -> {

            ccNode.x = Double.parseDouble(newValue);
            mCallback.run();
        });
        etY.textProperty().addListener((observable, oldValue, newValue) -> {
            ccNode.y = Double.parseDouble(newValue);
            mCallback.run();
        });

        etWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            ccNode.width = Double.parseDouble(newValue);
            mCallback.run();
        });

        etHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            ccNode.height = Double.parseDouble(newValue);
            mCallback.run();

        });

        etRotation.textProperty().addListener((observable, oldValue, newValue) -> {
            ccNode.angle = Double.parseDouble(newValue);
            mCallback.run();
        });

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
