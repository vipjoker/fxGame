package mygame.editor;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mygame.editor.model.TileModel;
import mygame.util.LevelParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Admin on 11.06.2016.
 */
public class Controller implements Initializable {
    public Pane pnGrid;
    public TilePane tilePane;
    private StackPane mPane;

    private List<TileModel> modelList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = 700;
        double height = 700;

        modelList = new ArrayList<>();
        modelList.add(new TileModel(100,100,10,10,"Hello"));
        modelList.add(new TileModel(100,100,10,10,"Again"));
        modelList.add(new TileModel(100,100,10,10,"GoodBye"));




        for (int i = 50; i < width; i += 50)
            for (int j = 50; j < height; j += 50) {
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

        pnGrid.getChildren().remove(rectangle);

        ImageView imageView = (ImageView)mPane.getChildren().get(0);
        Image i = imageView.getImage();

        ImageView newImage = new ImageView(i);

        newImage.setFitHeight(rectangle.getHeight());
        newImage.setFitWidth(rectangle.getWidth());

        newImage.setLayoutX(rectangle.getX());
        newImage.setLayoutY(rectangle.getY());

        newImage.setOnMouseClicked(this::onMouseClicked);

        pnGrid.getChildren().add(newImage);


    }




    public void updateCanvas(){

    }


    public void onSave(ActionEvent event){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Level files" ,".level"));
        File file = fileChooser.showSaveDialog(App.getInstance().getWindow());
        LevelParser.saveLevel(modelList,file);

    }

    public void onLoad(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open level");

        File file = fileChooser.showOpenDialog(App.getInstance().getWindow());

        List<TileModel> tileModels = LevelParser.loadLevel(file);
        System.out.println(tileModels);


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


    public void onAddH(ActionEvent event) {

    }

    public void onRemoveH(ActionEvent event) {

    }
}
