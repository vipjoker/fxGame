package mygame.editor.actions;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mygame.editor.InfoController;
import mygame.editor.ui.CustonPane;
import mygame.editor.model.*;
import mygame.editor.model.Point;

import java.io.IOException;
import java.util.List;

/**
 * Created by oleh on 3/27/17.
 */
public class SelectAction extends Action {

    private VBox pane;

    public SelectAction(VBox pane, CustonPane transGroup, List<AbstractModel> models) {
        super(transGroup,models);
        this.pane = pane;
    }

    @Override
    public void init() {


        parent.getParent().setOnMouseClicked(event -> clearEffects());
        for (AbstractModel model : models) {
            model.setOnMouseClicked(event -> {
                clearEffects();
                pane.getChildren().clear();
                try {
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/info.fxml"));
                    Parent load = loader.load();
                    InfoController controller = loader.getController();
                    controller.setNameInfo(model.toString());
                    controller.setPositionInfo(new Point(model.getLayoutX(),model.getLayoutY()));
                    pane.getChildren().add(load);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                pane.getChildren().add(new Text(event.getSource().toString()));

                model.setEffect(new Shadow(0, Color.SALMON));
                event.consume();
            });
        }
    }

    private void clearEffects(){
        for (AbstractModel model : models){
            model.setEffect(null);
        }
    }

    @Override
    public void mouseMoved(Point position) {

    }

    @Override
    public void mousePressed(Point position) {

    }

    @Override
    public void mouseReleased(Point position) {

    }

    @Override
    public void finishDrawing() {

    }
}
