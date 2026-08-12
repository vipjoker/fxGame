package newStuff.box2dEditor;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.TestList;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.javafx.DebugDrawJavaFX;
import org.jbox2d.testbed.framework.javafx.TestPanelJavaFX;
import org.jbox2d.testbed.framework.javafx.TestbedControllerJavaFX;
import org.jbox2d.testbed.framework.javafx.TestbedSidePanel;

import java.net.URL;
import java.util.ResourceBundle;

public class   MainController implements Initializable {

    public AnchorPane canvasParent;
    public AnchorPane propertiesParent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

    }
}
