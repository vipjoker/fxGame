package org.view;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class ResizableCanvas extends Canvas {
    public ResizableCanvas() {
        // repaint when size changes
        parentProperty().addListener(e -> {

                    widthProperty().bind(((Pane)getParent()).widthProperty());
                    heightProperty().bind(((Pane)getParent()).heightProperty());
                }



                );
        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());
    }

    private void draw() {
        double w = getWidth();
        double h = getHeight();
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        // custom drawing code
        gc.setStroke(Color.RED);
        gc.strokeRect(0.5, 0.5, Math.max(0, w-1), Math.max(0, h-1));
    }



    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}