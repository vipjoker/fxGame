package newStuff.box2dEditor;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.TestList;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.javafx.DebugDrawJavaFX;
import org.jbox2d.testbed.framework.javafx.TestPanelJavaFX;
import org.jbox2d.testbed.framework.javafx.TestbedControllerJavaFX;
import org.jbox2d.testbed.framework.javafx.TestbedSidePanel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class   MainController implements Initializable {

    public AnchorPane canvasParent;
    public AnchorPane propertiesParent;
    public ToggleButton btnRect;
    public ToggleButton btnCircle;
    public ToggleButton btnPoly;
    public ToggleButton btnLine;
    public ToolBar toolbarContainer;
    List<ToggleButton> buttonList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonList = List.of(btnCircle,btnRect,btnPoly,btnLine);
        TestbedModel model = new TestbedModel();
        final AbstractTestbedController controller = new TestbedControllerJavaFX(model,
                AbstractTestbedController.UpdateBehavior.UPDATE_CALLED, AbstractTestbedController.MouseBehavior.NORMAL, (Exception e, String message) -> {
            new Alert(Alert.AlertType.ERROR).showAndWait();
        });
        propertiesParent.getChildren().add(new ScrollPane(new TestbedSidePanel(model, controller)));
        TestPanelJavaFX panel = new TestPanelJavaFX(model, controller);
        model.setPanel(panel);
        model.setDebugDraw(new DebugDrawJavaFX(panel, true));
        TestList.populateModel(model);
        canvasParent.getChildren().add(panel);

        Platform.runLater(() -> {
            controller.playTest(0);
            controller.start();
        });

        ToggleGroup group = new ToggleGroup();
        for (ToggleButton toggleButton : buttonList) {
            toggleButton.setToggleGroup(group);
        }

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                ToggleButton selected = (ToggleButton) newToggle;
               if(selected == btnRect){
                   model.getSettings().create_mode = TestbedSettings.CreateMode.RECT;
               }else if (selected == btnCircle){
                   model.getSettings().create_mode = TestbedSettings.CreateMode.CIRCLE;

               }else if (selected == btnLine){
                   model.getSettings().create_mode = TestbedSettings.CreateMode.LINE;

               }else if(selected == btnPoly){
                   model.getSettings().create_mode = TestbedSettings.CreateMode.POLY;

               }
            }
        });
    }


}
