package mygame.editor.render;

import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import mygame.editor.App;
import mygame.editor.TimerCounter;
import mygame.editor.util.Callback;
import mygame.editor.util.Constants;
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

    Point2D mouseCursor = new Point2D(0, 0);
    private GraphicsContext graphicsContext;
    private TimerCounter counter;
    private Point2D lastPoint;

    private Point2D beginRect;
    private Point2D endRect;

    private Grid grid = new Grid();
    private final ObservableList<CcNode> nodes = FXCollections.observableList(new ArrayList<>(), param ->
            new Observable[]{param.getName(), param.getX(), param.getY()});
    private OnCanvasDragListener mOnCanvasDragListener;

    public CanvasRenderer(Pane pane) {
        Canvas canvas = new Canvas();
        Global.setHeight(800);
        Global.setWidth(800);
        pane.getChildren().add(canvas);
        counter = new TimerCounter(new TimerCounter.FrameRateCallback() {
            @Override
            public void update(long delta) {
                CanvasRenderer.this.scheduledUpdate();
            }
        });
        counter.start();
        // nodes.addListener((ListChangeListener<CcNode>) change -> update());


        graphicsContext = canvas.getGraphicsContext2D();
        pane.widthProperty().addListener((observable, oldValue, newValue) -> {
            Global.setWidth(newValue.doubleValue());
            update();
        });
        pane.heightProperty().addListener((observable, oldValue, newValue) -> {
            Global.setHeight(newValue.doubleValue());
            update();
        });


        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());


        canvas.widthProperty().addListener(this::onChangeWidth);
        canvas.heightProperty().addListener(this::onChangeHeight);

        canvas.setOnScroll(this::onScroll);
        canvas.setOnMouseDragged(this::onDrag);
        canvas.setOnMouseReleased(this::onMouseReleased);
        canvas.setOnMousePressed(this::onMouseClicked);
        canvas.setCursor(Cursor.CROSSHAIR);
        canvas.setOnMouseMoved(this::onMouseMoved);
        draw(graphicsContext, Global.getWidth(), Global.getHeight());
        App.instance.selected.addListener(this::onSelectChanged);
    }

    private void onSelectChanged(ListChangeListener.Change<? extends CcNode> change) {
        final ObservableList<? extends CcNode> list = change.getList();
        for (CcNode ccNode : this.nodes) {

            traverse(ccNode, n -> {
                final boolean contains = list.contains(n);
                n.setActive(contains);
            });


        }
    }

    private void traverse(CcNode node, Callback<CcNode> action) {
        action.call(node);
        for (CcNode ccNode : node.getChildren()) {
            traverse(ccNode, action);
        }
    }


    private void onMouseMoved(MouseEvent event) {
        mouseCursor = new Point2D(event.getX(), event.getY());
        update();
    }

    private void onMouseClicked(MouseEvent event) {
        if (mOnCanvasDragListener != null && event.getButton() == MouseButton.PRIMARY) {
            Point2D point2D = grid.transformPoint(event);
            Point2D p = new Point2D(point2D.getX(), -point2D.getY());

            mOnCanvasDragListener.onStartMove(p);

        }

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
//        for (CcNode n : sortedNodes) {
//            Point2D point2D = new Point2D(event.getX(), event.getY());
//            if (n.contains(point2D) && !isPressed) {
////                n.setActive(true);
//                isPressed = true;
//            } else {
////                n.setActive(false);
//            }
//        }


        draw(graphicsContext, Global.getWidth(), Global.getHeight());
    }

    private void onMouseReleased(MouseEvent event) {

        if (mOnCanvasDragListener != null) {
            mOnCanvasDragListener.onStopMove(lastPoint);
        }
        lastPoint = null;
        beginRect = null;
        endRect = null;
    }


    private void onDrag(MouseEvent event) {
        mouseCursor = new Point2D(event.getX(), event.getY());
        double x = event.getX();
        double y = event.getY();
        Point2D p = new Point2D(x, y);
        Point2D sub = null;
        if (lastPoint != null) {
            sub = lastPoint.subtract(p);
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            if (sub != null) {
                translatex -= sub.getX();
                translatey -= sub.getY();
            }


        } else if (mOnCanvasDragListener != null) {
            if (sub != null) {
                double scalefactor = 1 / scale;
                Point2D scaled = sub.multiply(scalefactor);
                mOnCanvasDragListener.onDrag(scaled);
            }
        } else {
            if (beginRect == null) {

                beginRect = new Point2D(event.getX(), event.getY());
            } else {
                endRect = new Point2D(event.getX(), event.getY());
            }

        }
        lastPoint = p;
        update();
    }

    private void onScroll(ScrollEvent scrollEvent) {

        scale += (scrollEvent.getDeltaY() / 1000);

        if (scale < 0.2) scale = 0.2;
        if (scale > 5) scale = 5;


        scrollx = scrollEvent.getX();
        scrolly = scrollEvent.getY();
        update();


    }


    private void onChangeWidth(ObservableValue<? extends Number> observable, Number old, Number newValue) {
        Global.setWidth((double) newValue);
        update();
    }

    private void onChangeHeight(ObservableValue<? extends Number> observable, Number old, Number newValue) {
        Global.setHeight((double) newValue);
        update();
    }

    private void draw(GraphicsContext g, double width, double height) {
        g.clearRect(0, 0, width, height);
        g.setFill(Constants.BACKGROUND);
        g.fillRect(0, 0, width, height);
        g.setFill(Constants.WHITE);
        g.save();
        g.transform(new Affine(Affine.scale(this.scale, scale, scrollx, scrolly)));
        g.transform(new Affine(Affine.translate(translatex / scale, translatey / scale)));


        g.translate(20, height - 20);
        for (CcNode node : nodes) {
            node.draw(g, 0);
        }
        grid.draw(g, 0);
        drawCursor(g);
        g.restore();
        handleSelectionRect(g);
    }

    private void handleSelectionRect(GraphicsContext g) {

        if (beginRect != null && endRect != null) {

            g.setFill(Color.ALICEBLUE.deriveColor(1, 1, 1, 0.3));
            g.setStroke(Color.ALICEBLUE.deriveColor(1, 1, 1, 0.5));

            double x = beginRect.getX() < endRect.getX() ? beginRect.getX() : endRect.getX();
            double y = beginRect.getY() < endRect.getY() ? beginRect.getY() : endRect.getY();


            double width = Math.abs(beginRect.getX() - endRect.getX());

            double height = Math.abs(beginRect.getY() - endRect.getY());


            g.fillRect(x, y, width, height);
            g.strokeRect(x, y, width, height);
            g.fill();
        }
    }

    private void drawCursor(GraphicsContext g) {

        try {
            grid.transformPoint(mouseCursor.getX(), mouseCursor.getY());//check grid method grid.transform

            Point2D p = g.getTransform().inverseTransform(mouseCursor);
            double y = p.getY();
            double x = p.getX();
            g.fillText(String.format("%.0f\n%.0f", x, y), x + 20, y - 20);
            g.fill();
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
    }


    public void update() {
//        draw(graphicsContext, Global.getWidth(), Global.getHeight());
    }

    public void scheduledUpdate() {
        draw(graphicsContext, Global.getWidth(), Global.getHeight());
    }

    public void addChild(CcNode node) {
        nodes.add(node);
    }

    public List<CcNode> getNodes() {
        return nodes;
    }

    public void setOnCanvasDragListener(OnCanvasDragListener listener) {
        this.mOnCanvasDragListener = listener;
    }


    public interface OnCanvasDragListener {
        void onStartMove(Point2D point);

        void onDrag(Point2D point);

        void onStopMove(Point2D point);
    }
}
