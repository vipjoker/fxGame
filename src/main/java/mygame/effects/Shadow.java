package mygame.effects;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Admin on 07.06.2016.
 */
public class Shadow extends Application {

        private void init(Stage primaryStage) {
            Group root = new Group();
            primaryStage.setScene(new Scene(root));
            Text sample = new Text(0,100,"Shadow");
            sample.setFont(Font.font("Arial Black",80));
            sample.setFill(Color.web("#BBBBBB"));
            final InnerShadow innerShadow = new InnerShadow();
            innerShadow.setRadius(5d);
            innerShadow.setOffsetX(2);
            innerShadow.setOffsetY(2);
            sample.setEffect(innerShadow);
            root.getChildren().add(sample);
        }

        @Override public void start(Stage primaryStage) throws Exception {
            init(primaryStage);
            primaryStage.show();
        }
        public static void main(String...args){
            launch(args);
        }
}
