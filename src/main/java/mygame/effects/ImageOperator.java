package mygame.effects;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mygame.editor.data.DbConnection;
import mygame.editor.data.NodeDao;
import mygame.editor.data.entities.EntityNode;

import java.sql.Connection;
import java.util.List;

public class ImageOperator extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(200);
        primaryStage.setHeight(200);


        EntityNode node = new EntityNode();
        node.setX(23);
        node.setY(33);
        node.setWidth(100);
        node.setHeight(100);

        try(Connection c = DbConnection.getDbConnection()){
            NodeDao dao = new NodeDao(c);
            dao.insert(node);
            List<EntityNode> all = dao.getAll();
            all.forEach(et->System.out.println(et.getId()));
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

