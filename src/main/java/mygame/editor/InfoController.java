package mygame.editor;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import mygame.editor.model.Point;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by oleh on 3/27/17.
 */
public class InfoController implements Initializable {
    public GridPane gpInfo ;
    public TextField etName;
    public TextField etX;
    public TextField etY;
    public TextField etWidth;
    public TextField etHeight;

    DoubleProperty property = new SimpleDoubleProperty(10);
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void setWidthInfo(Double width){
        etWidth.setText(String.valueOf(width));
    }

    public void setHeightInfo(Double height){
        etHeight.setText(String.valueOf(height));
    }

    public void setNameInfo(String name){
        etName.setText(name);
    }

    public void setPositionInfo(Point positionInfo){
        etX.setText(String.valueOf(positionInfo.getX()));
        etY.setText(String.valueOf(positionInfo.getY()));
    }
}
