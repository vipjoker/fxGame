package mygame.editor;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Admin on 11.06.2016.
 */
public class Controller implements Initializable {
    public Pane pnGrid;
    public TilePane tilePane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 700;
        double height = 700;


        for (int i = 0; i < width; i += 50)
            for (int j = 0; j < height; j += 50) {
                Rectangle rectangle = new Rectangle(i + 2, j + 2, 46, 46);
                Label label = new Label(j + " " + i);
                label.setLayoutX(i);
                label.setLayoutY(j);

                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(2);

                rectangle.setArcHeight(5);
                rectangle.setArcWidth(5);

                rectangle.setOnMouseEntered(this::onMouseEnter);
                rectangle.setOnMouseExited(this::onMouseOver);
                pnGrid.getChildren().add(rectangle);
                pnGrid.getChildren().add(label);
            }

    }


    public void onMouseEnter(MouseEvent event) {
        Rectangle rectangle = (Rectangle) event.getSource();
        rectangle.setFill(Color.BLACK);

    }

    public void onMouseOver(MouseEvent event) {
        Rectangle rectangle = (Rectangle) event.getSource();
        rectangle.setFill(Color.WHITE);
    }


    public void onAbout(ActionEvent event) {
        Dialog.showDialog("Tile builder editor by vipjoker");
    }

    public void onAdd(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Resource File");
        Stage window = App.getInstance().getWindow();
        File imageDir = directoryChooser.showDialog(window);
        if (imageDir != null) {

            for(File file :imageDir.listFiles())
            addTile(file);
        }
    }

    private void addTile(File file) {

        Image image = null;
        try {
            image = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        tilePane.getChildren().add(imageView);
    }


}
