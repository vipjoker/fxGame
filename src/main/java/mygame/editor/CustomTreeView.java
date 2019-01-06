package mygame.editor;

import com.badlogic.gdx.math.Vector2;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CustomTreeView extends Application implements TimerCounter.FrameRateCallback {


    private GraphicsContext ctx;
    Item[] items = {
            new Item("One"),
            new Item("Two"),
            new Item("Three"),
            new Item ("Four") ,
            new Item("Five"),
            new Item("Six"),
            new Item("Seven"),
            new Item ("Eight")
    };

    final Vector2 offset = Vector2.Zero;
    final Vector2 mousePoint = Vector2.Zero;
    final Vector2 current = Vector2.Zero;
    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox box = new VBox();
        Canvas canvas = new Canvas();
        ctx = canvas.getGraphicsContext2D();
        canvas.widthProperty().set(500);
        canvas.heightProperty().set(500);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseDragged(this::onMousePressed);
        box.getChildren().add(canvas);
        primaryStage.setScene(new Scene(box,500,500));
        TimerCounter counter = new TimerCounter(this);
        counter.start();
        primaryStage.show();
    }

    private void onMousePressed(MouseEvent mouseEvent) {
        float x = (float)mouseEvent.getX();
        float y = (float)mouseEvent.getY();
        mousePoint.set(x,y);
    }


    private void onMouseDragged(MouseEvent mouseEvent) {
        float x = (float)mouseEvent.getX();
        float y = (float)mouseEvent.getY();
        current.set(x,y);
        mousePoint.sub(current);
        mousePoint.set(current);

    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void update(long delta) {

        ctx.clearRect(0,0,500,500);
        ctx.save();
        ctx.translate(0,offset.y);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.CENTER);
        int yPos = 0;
        float width  = 200;
        float height = 100;
        for (Item item : items) {

        ctx.strokeRect(0,yPos,width,height);
        ctx.fillText(item.text,width/2,yPos + height/2);
        yPos+=height;
        }



        ctx.fill();
        ctx.stroke();
        ctx.restore();
    }

    class Item {
        final String text;
        public Item(String text){
            this.text = text;
        }
    }

}
