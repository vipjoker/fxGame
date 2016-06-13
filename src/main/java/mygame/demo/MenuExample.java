package mygame.demo;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by Admin on 13.06.2016.
 */
public class MenuExample extends Application {


    final PageData[] pages = new PageData[] {
        new PageData("/yabloko.jpg",
                "The apple is the pomaceous fruit of the apple tree, species Malus "
                        + "domestica in the rose family (Rosaceae). It is one of the most "
                        + "widely cultivated tree fruits, and the most widely known of "
                        + "the many members of genus Malus that are used by humans. "
                        + "The tree originated in Western Asia, where its wild ancestor, "
                        + "the Alma, is still found today.",
                "Malus domestica"),
                new PageData("/yabloko2.jpg",
                        "The hawthorn is a large genus of shrubs and trees in the rose "
                                + "family, Rosaceae, native to temperate regions of the Northern "
                                + "Hemisphere in Europe, Asia and North America. "
                                + "The name hawthorn was "
                        + "originally applied to the species native to northern Europe, "
                        + "especially the Common Hawthorn C. monogyna, and the unmodified "
                        + "name is often so used in Britain and Ireland.",
                        "Crataegus monogyna"),
                new PageData("/yabloko3.jpg",
                        "The ivy is a flowering plant in the grape family (Vitaceae) native to"
                                + " eastern Asia in Japan, Korea, and northern and eastern China. "
                                + "It is a deciduous woody vine growing to 30 m tall or more given "
                                + "suitable support,  attaching itself by means of numerous small "
                                + "branched tendrils tipped with sticky disks.",
                        "Parthenocissus tricuspidata")

    };

    final String[] viewOptions = new String[]{
            "Title",
            "Binomial name",
            "Picture",
            "Description"
    };


    final Map.Entry<String,Effect>[] effects = new Map.Entry[] {
            new AbstractMap.SimpleEntry<String,Effect>("Sepia Tone", new SepiaTone()),
            new AbstractMap.SimpleEntry<String,Effect>("Sepia Tone", new Glow()),
            new AbstractMap.SimpleEntry<String,Effect>("Sepia Tone", new DropShadow())
    };


    final Label name = new Label();
    final Label binName = new Label();
    final Label description = new Label();
    final ImageView pic = new ImageView();
    private int  currentIndex = -1;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Menu Sample");
        Scene scene = new Scene(new VBox(),400,400);
        scene.setFill(Color.OLDLACE);

        name.setFont(new Font("Verdana Bold",22));
        binName.setFont(new Font("Arial Italic",10));
        description.setWrapText(true);
        description.setTextAlignment(TextAlignment.JUSTIFY);

        pic.setFitHeight(150);
        pic.setPreserveRatio(true);
        shuffle();

        MenuBar menuBar = new MenuBar();

        final VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0,10,0,10));
        vbox.getChildren().addAll(name,binName,pic,description);

        //menu file
        Menu menuFile = new Menu("File");
        ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream("/Add-icon.png")));
        imageView.setFitWidth(10);
        imageView.setFitHeight(10);
        MenuItem add = new MenuItem("Shuffle",imageView);
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                shuffle();
                vbox.setVisible(true);

            }
        });


        MenuItem clear = new MenuItem("Clear");
        clear.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vbox.setVisible(false);
            }
        });

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        menuFile.getItems().addAll(add,clear,new SeparatorMenuItem(),exit);



        Menu menuEdit = new Menu("Edit");



        Menu menuView = new Menu("View");

        CheckMenuItem titleView = createMenuItem("Title",name);
        CheckMenuItem binNameView = createMenuItem("Binomial name", binName);
        CheckMenuItem picView = createMenuItem("Picture",pic);
        CheckMenuItem descriptorView = createMenuItem("Description", description);
        menuView.getItems().addAll(titleView,binNameView,picView,descriptorView);




        menuBar.getMenus().addAll(menuFile,menuEdit,menuView);

        ((VBox)scene.getRoot()).getChildren().addAll(menuBar,vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private static CheckMenuItem createMenuItem(String title, final Node node){
        CheckMenuItem cmi = new CheckMenuItem(title);
        cmi.setSelected(true);
        cmi.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                node.setVisible(newValue);
            }
        });
        return cmi;
    }

    private void shuffle(){
        int i = currentIndex;
        while (i == currentIndex){
            i = (int)(Math.random() * pages.length);
        }

        pic.setImage(pages[i].image);
        binName.setText(pages[i].name);
        description.setText(pages[i].description);
        currentIndex = i;
    }



    private class PageData{
        public String name;
        public String description;
        public String binNames;
        public Image image;

        public PageData(String name, String description, String binNames) {
            this.name = name;
            this.description = description;
            this.binNames = binNames;
            this.image = new Image(getClass().getResourceAsStream(name));
        }
    }
}
