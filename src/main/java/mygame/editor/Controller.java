package mygame.editor;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    private StackPane mPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 700;
        double height = 700;


        for (int i = 0; i < width; i += 50)
            for (int j = 0; j < height; j += 50) {
                Rectangle rectangle = new Rectangle(i , j , 50, 50);


                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(.5);

                rectangle.setArcHeight(5);
                rectangle.setArcWidth(5);

                rectangle.setOnMouseClicked(this::onMouseClicked);
                pnGrid.getChildren().add(rectangle);
            }

    }


    public void onMouseClicked(MouseEvent event) {
        Rectangle rectangle = (Rectangle) event.getSource();
        rectangle.setFill(Color.BLACK);


        ImageView imageView = (ImageView)mPane.getChildren().get(0);
        Image i = imageView.getImage();

        ImageView newImage = new ImageView();

        newImage.setFitHeight(rectangle.getHeight());
        newImage.setFitWidth(rectangle.getWidth());
        newImage.setLayoutX(rectangle.getLayoutX());
        newImage.setLayoutY(rectangle.getLayoutY());

        newImage.setImage(i);

        pnGrid.getChildren().add(newImage);


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
        StackPane pane = new StackPane();

        ImageView imageView = new ImageView(image);


        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        pane.setStyle("-fx-border-color: #000000; -fx-border-width: 5px;-fx-border-radius: 5px;");

        pane.getChildren().add(imageView);
        imageView.setOnMouseClicked(this::onTileSelected);
        tilePane.getChildren().add(pane);


    }

    public void onTileSelected(MouseEvent event){
        ImageView imageView = (ImageView)event.getSource();

        imageView.getParent().setStyle("-fx-border-color: #ff005e;-fx-border-width: 5px;-fx-border-radius: 5px;");
        if(mPane != null)
        mPane.setStyle("-fx-border-color: #000000;-fx-border-width: 5px;-fx-border-radius: 5px;");

        mPane = (StackPane) imageView.getParent();

    }


}
