package physicsPort;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import javafx.application.Application;
import javafx.event.EventTarget;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mygame.editor.TimerCounter;
import mygame.editor.util.Constants;

public class PhysicsEditorPort extends Application implements TimerCounter.FrameRateCallback {

    private GraphicsContext context;
    private int width = 800;
    private int height = 800;
    private int menuWidth = 200;
    private float duration = 200;
    Vector2 pos = new Vector2(10, 10);
    Vector2 target = new Vector2(10, 10);
    AnimationTask task;
    private long lastTime;
    private long targetTime;
    double[] lastPoint = {0,0};
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        // create instance of physics editor
        Button playBtn = new Button("Play");
        Button pauseBtn = new Button("Pause");
        Button stepBtn = new Button("Step");


        VBox right = new VBox(playBtn, pauseBtn, stepBtn);
        right.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY,null)));
        right.setPrefWidth(200);
        right.layoutXProperty().bind(primaryStage.widthProperty().subtract(200));
        right.prefHeightProperty().bind(primaryStage.heightProperty().subtract(200));



        VBox left = new VBox();

        TreeView<String> treeView = new TreeView<>();
        TreeItem<String> treeRoot = new TreeItem<>("Root");
        Node[] nodes = new Node[10];

        for (int i = 0; i < 10; i++) {
            TreeItem<String> one = new TreeItem<>("Item " + i+1);
            Pane pane = new Pane();
            pane.setOnMousePressed(event -> {
                lastPoint[0] = event.getX();
                lastPoint[1]=  event.getY();

            });

            pane.setOnMouseDragged(event->{

                for (Node node : nodes) {
                    if(node.getParent().contains(event.getX(),event.getY())){

                        pane.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
                    }else{
                        pane.setBackground(new Background(new BackgroundFill(Color.PINK,null,null)));

                    }
                }


            });
            pane.setOnMouseReleased(event->{

                pane.setBackground(new Background(new BackgroundFill(Color.PINK,null,null)));
            });
            pane.setPrefWidth(75);
            pane.setPrefHeight(50);
            pane.setBackground(new Background(new BackgroundFill(Color.PINK,null,null)));

            one.setGraphic(pane);
            nodes[i] = pane;
            treeRoot.getChildren().add(one);
        }





        treeView.setRoot(treeRoot);
        left.getChildren().add(treeView);

        Canvas canvas = new Canvas();


        canvas.setLayoutX(menuWidth);
        canvas.setLayoutY(0);

        left.prefHeightProperty().bind(primaryStage.heightProperty().subtract(200));
        left.setPrefWidth(menuWidth);

        final Pane root = new Pane(canvas,right,left);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        canvas.widthProperty().bind(root.widthProperty().subtract(400));
        canvas.heightProperty().bind(root.heightProperty().subtract(200));


        context = canvas.getGraphicsContext2D();


        // cached variables


        // to avoid viewport events while editing selection properties

        scene.setOnKeyPressed(e -> {

        });

        scene.setOnKeyReleased(e -> {

        });


        canvas.setOnMousePressed(e -> {
            target.x = (float) e.getX();
            target.y = (float) e.getY();
            task = new AnimationTask(Interpolator.EASEINOUTCIRC, 2000);
        });

        TimerCounter timerCounter = new TimerCounter(this);
        timerCounter.start();

        primaryStage.show();


    }

    private void printStackTrace() {
        StringBuilder builder = new StringBuilder();
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            builder.append(stackTraceElement.getClassName())
                    .append(" : ")
                    .append(stackTraceElement.getMethodName())
                    .append("()")
                    .append(": line ")
                    .append(stackTraceElement.getLineNumber())
                    .append("\n");
            for (int j = 0; j < i; j++) {
                builder.append("\t");
            }

        }

        System.out.println(builder.toString());
    }

    @Override
    public void update(long delta) {
        context.clearRect(0, 0, width, height);
        context.setFill(Constants.BACKGROUND);
        context.fillRect(0, 0, width, height);

        context.setFill(Constants.WHITE);
        context.fillText("Delta : " + delta, 100, 100);


        if (task != null && !task.isFinished()) {
            final float animate = task.animate();
            context.fillText("Delta : " + animate, width / 2, height / 2);

            pos.x = animate * 100;
            pos.y = animate * 100;
            context.fillOval(pos.x - 25, pos.y - 25, 50, 50);
        }

        lastTime = delta;


    }


}
