package mygame.editor.render;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
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

    Point2D mouseCursor = new Point2D(0,0);
    private GraphicsContext graphicsContext;
    private TimerCounter counter;
    private Point2D lastPoint;

    private Point2D beginRect;
    private Point2D endRect;

    private Grid grid = new Grid();
    private List<CcNode> nodes = new ArrayList<>();
    private OnCanvasClickListener mOnCanvasClickListener;
    private OnCanvasDragListener mOnCanvasDragListener;

    public CanvasRenderer(Pane pane) {
        Canvas canvas = new Canvas();
        Global.setHeight(800);
        Global.setWidth(800);
        pane.getChildren().add(canvas);


//        AnchorPane.setTopAnchor(canvas, 0.0);
//        AnchorPane.setBottomAnchor(canvas, 0.0);
//        AnchorPane.setLeftAnchor(canvas, 0.0);
//        AnchorPane.setRightAnchor(canvas, 0.0);

        graphicsContext = canvas.getGraphicsContext2D();
        pane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Global.setWidth(newValue.doubleValue());
                update();
            }
        });
        pane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Global.setHeight(newValue.doubleValue());
                update();
            }
        });


        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());


        canvas.widthProperty().addListener(this::onChangeWidth);
        canvas.heightProperty().addListener(this::onChangeHeight);

        canvas.setOnScroll(this::onScroll);
        canvas.setOnMouseDragged(this::onDrag);
        canvas.setOnMouseReleased(this::onMouseReleased);
        canvas.setOnMouseClicked(this::onMouseClicked);
        canvas.setCursor(Cursor.CROSSHAIR);
        canvas.setOnMouseMoved(e-> {
            mouseCursor=new Point2D(e.getX(),e.getY());
            update();
        });
        draw(graphicsContext, Global.getWidth(), Global.getHeight());

    }




    private void onMouseClicked(MouseEvent event) {
        if(mOnCanvasClickListener != null && event.getButton() == MouseButton.PRIMARY){
            Point2D point2D = grid.transformPoint(event);
            Point2D p = new Point2D(point2D.getX(),-point2D.getY());

            mOnCanvasClickListener.onClick(p);

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
        for (CcNode n : sortedNodes) {
            if (n.contains(event.getX(), event.getY()) && !isPressed) {
//                n.setActive(true);
                isPressed = true;
            } else {
//                n.setActive(false);
            }
        }


        draw(graphicsContext, Global.getWidth(), Global.getHeight());
    }

    private void onMouseReleased(MouseEvent event) {
        lastPoint = null;
        beginRect = null;
        endRect = null;
        if(mOnCanvasDragListener != null){
            mOnCanvasDragListener.onDrag(0,0,true);
        }
    }


    private void onDrag(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        Point2D p = new Point2D(x, y);
        Point2D sub = null;
        if (lastPoint != null) {
            sub = lastPoint.subtract(p);
        }
        if (event.getButton() == MouseButton.SECONDARY) {
                if(sub!= null) {
                    translatex -= sub.getX();
                    translatey -= sub.getY();
                }


        }else if(mOnCanvasDragListener != null){
            if(sub != null){
                mOnCanvasDragListener.onDrag(sub.getX(),sub.getY(),false);
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

        if ( scale < 0.2) scale = 0.2;
        if (scale > 5)scale = 5;


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

    private void drawCursor(GraphicsContext g){

        try {
            grid.transformPoint(mouseCursor.getX(),mouseCursor.getY());//check grid method grid.transform

            Point2D p = g.getTransform().inverseTransform(mouseCursor);
            double y = p.getY() ;
            double x = p.getX() ;



        double cursorLength = 10;
        g.beginPath();
        g.setStroke(Color.PINK);
        g.moveTo(x -cursorLength ,y);
        g.lineTo(x +cursorLength ,y);
        g.moveTo(x ,y -cursorLength );
        g.lineTo(x  ,y +cursorLength);
        g.fillText(String.format("%f\n%f",x,y),x + 10,y + 10);


        g.stroke();
        g.closePath();
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
    }


    public void update() {
        draw(graphicsContext, Global.getWidth(), Global.getHeight());
    }

    public void addChild(CcNode node) {
        nodes.add(node);
    }

    public List<CcNode> getNodes() {
        return nodes;
    }

    public void setOnCanvasClickListener(OnCanvasClickListener canvasClickListener){
        this.mOnCanvasClickListener = canvasClickListener;
    }

    public void setmOnCanvasDragListener(OnCanvasDragListener listener){
        this.mOnCanvasDragListener = listener;
    }



    public interface OnCanvasClickListener {
        void onClick(Point2D point2D);
    }

    public interface OnCanvasDragListener{
        void onDrag(double deltaX,double deltaY,boolean isEnd);
    }
}
