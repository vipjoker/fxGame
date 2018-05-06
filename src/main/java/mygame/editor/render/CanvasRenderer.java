package mygame.editor.render;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Affine;
import mygame.editor.util.Constants;
import mygame.editor.TimerCounter;
import mygame.editor.views.CcNode;
import mygame.editor.views.Global;
import mygame.editor.views.Grid;

import java.util.*;

public class CanvasRenderer {
    private double scale = 1;
    private double scrollx = 400;
    private double scrolly = 400;
    private double translatex = 400;
    private double translatey = -400;


    private GraphicsContext graphicsContext;
    private TimerCounter counter;
    private Point2D lastPoint;
    private Grid grid = new Grid();
    private List<CcNode> nodes = new ArrayList<>();



    public CanvasRenderer(Pane pane){
        Canvas canvas = new Canvas();
        Global.setHeight(800);
        Global.setWidth(800);
        pane.getChildren().add(canvas);


        AnchorPane.setTopAnchor(canvas, 0.0);
        AnchorPane.setBottomAnchor(canvas, 0.0);
        AnchorPane.setLeftAnchor(canvas, 0.0);
        AnchorPane.setRightAnchor(canvas, 0.0);

        graphicsContext = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());


        canvas.widthProperty().addListener(this::onChangeWidth);
        canvas.heightProperty().addListener(this::onChangeHeight);
        canvas.setOnScroll(this::onScroll);
        canvas.setOnMouseDragged(this::onDrag);
        canvas.setOnMouseReleased(this::onMouseReleased);
        canvas.setOnMouseClicked(this::onMouseClicked);
        draw(graphicsContext, Global.getWidth(), Global.getHeight());

    }


    private void onMouseClicked(MouseEvent event) {


        Deque<List<CcNode>> lists = new LinkedList<>();
        List<CcNode> sortedNodes = new ArrayList<>();
        int layer = 0;
        lists.push(nodes);
        boolean isPressed = false;
        while (!lists.isEmpty()) {
            layer++;
            Deque<List<CcNode>> newList = new LinkedList<>();
            while (!lists.isEmpty()) {
                List<CcNode> pop = lists.pop();
                for (CcNode n : pop) {
                    n.layer = layer;
                    sortedNodes.add(n);
                    if (!n.getChildren().isEmpty()) {
                        newList.push(n.getChildren());
                    }
                }
            }
            lists = newList;
        }
        sortedNodes.sort(Collections.reverseOrder(Comparator.comparingInt(l -> l.layer)));
        for (CcNode n : sortedNodes) {
            if (n.contains(event.getX(), event.getY()) && !isPressed) {
                n.setActive(true);
                isPressed = true;
            } else {
                n.setActive(false);
            }
        }


        draw(graphicsContext, Global.getWidth(), Global.getHeight());
    }

    private void onMouseReleased(MouseEvent event) {
        lastPoint = null;
    }


    private void onDrag(MouseEvent event) {
        if (event.getButton() != MouseButton.SECONDARY) return;
        double x = event.getX();
        double y = event.getY();
        Point2D p = new Point2D(x, y);
        if (lastPoint != null) {
            Point2D subtract = lastPoint.subtract(p);
            translatex -= subtract.getX();
            translatey -= subtract.getY();
            draw(graphicsContext, Global.getWidth(), Global.getHeight());
        }

        lastPoint = p;
    }

    private void onScroll(ScrollEvent scrollEvent) {
        if (scrollEvent.getDeltaY() < 0 && scale < 0.2) return;
        if (scrollEvent.getDeltaY() > 0 && scale > 5) return;
        scale += (scrollEvent.getDeltaY() / 1000);
        scrollx = scrollEvent.getX();
        scrolly = scrollEvent.getY();
        update();


    }


    private void onChangeWidth(ObservableValue<? extends Number> observable, Number old, Number newValue) {
        Global.setWidth((double) newValue);
        update();
    }

    private void onChangeHeight(ObservableValue<? extends Number> observable, Number old, Number newValue) {
        Global.setWidth((double) newValue);
          update();
    }

    private void draw(GraphicsContext g, double width, double height) {
        g.clearRect(0, 0, width, height);
        g.setFill(Constants.BACKGROUND);
        g.fillRect(0, 0, width, height);
        g.setFill(Constants.WHITE);
        g.save();
        g.transform(new Affine(Affine.translate(translatex, translatey)));
        g.transform(new Affine(Affine.scale(this.scale, scale, scrollx, scrolly)));


        g.translate(20, height - 20);
        for (CcNode node : nodes) {
            node.draw(g, 0);
        }
        grid.draw(g, 0);
        g.restore();
    }


    public void update(){
        draw(graphicsContext, Global.getWidth(), Global.getHeight());
    }

    public void addChild(CcNode node){
        nodes.add(node);
    }

    public List<CcNode> getNodes(){
        return nodes;
    }
}
