package mygame.editor;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.util.ArrayList;

public class FileListener {
    public static void main(String[] args) {
        final ObservableList<DoubleProperty> observableList = FXCollections.observableList(new ArrayList<>(), new Callback<DoubleProperty, javafx.beans.Observable[]>() {
            @Override
            public javafx.beans.Observable[] call(DoubleProperty param) {
                return new Observable[]{param};
            }
        });


        observableList.addListener(new ListChangeListener<DoubleProperty>() {
            @Override
            public void onChanged(Change<? extends DoubleProperty> change) {
                change.next();
                if(change.wasPermutated()) {
                    final int from = change.getFrom();
                    System.out.println(change +"  " + from + " " + change.getTo());

                }

            }
        });


        observableList.add(new SimpleDoubleProperty(10));
        observableList.add(new SimpleDoubleProperty(15));
        observableList.get(0).set(777);
        observableList.get(1).set(555);
    }


}
