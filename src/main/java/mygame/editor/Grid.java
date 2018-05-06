package mygame.editor;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import mygame.editor.TimerCounter;


import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Admin on 07.06.2016.
 */
public class Grid extends Application implements TimerCounter.FrameRateCallback{
    private MyPane[][] grid = new MyPane[20][20];
    private Player player ;
    TimerCounter counter;
    private byte level[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1},

            {1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
    };

    @Override
    public void update(long delta) {

    }


    @Override
    public void seconds(long seconds) {
        player.act();
    }

    private void init(Stage primaryStage) {
        Pane root = new Pane();
        counter = new TimerCounter(this);
        counter.start();
        primaryStage.setScene(new Scene(root, 500, 500));

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                Text t = new Text();
                t.setFill(Color.WHEAT);
                t.setTextAlignment(TextAlignment.CENTER);
                t.setTextOrigin(VPos.CENTER);

                MyPane p = new MyPane(t, i, j);
                p.setLayoutX(i * 25);
                p.setLayoutY(j * 25);
                p.setPrefWidth(25);
                p.setPrefHeight(25);

                t.yProperty().bind(p.heightProperty().divide(2));
                p.setOriginalColor(Color.GREEN);


                grid[i][j] = p;
                grid[i][j].setBlock(level[j][i] == 1);

                if (i > 0) {
                    int indexI = i - 1;
                    MyPane left = grid[indexI][j];
                    left.setRight(p);
                    p.setLeft(left);
                }

                if (j > 0) {
                    int indexJ = j - 1;
                    MyPane top = grid[i][indexJ];
                    top.setBottom(p);
                    p.setTop(top);

                }


                root.getChildren().add(p);

                p.setOnMouseClicked(e -> {
                    clearAllActives();
                    MyPane pane = ((MyPane) e.getSource());
                    if (!pane.isBlock) {
                        List<MyPane> myPanes = new ArrayList<>();
                        myPanes.add(pane);
                        MyPane.row(0,myPanes);
                    }
                });
            }
        }
        player = new Player(grid[0][0]);
        root.getChildren().add(player);
    }

    public void clearAllActives() {
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].setInactive();
                grid[i][j].setText("");

            }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        counter.stop();

    }

    public static void main(String... args) {
        launch(args);
    }

    static class MyPane extends Pane {
        private boolean isBlock;
        private Color mColor;
        private Text mText;
        private MyPane left;
        private MyPane right;
        private MyPane top;
        private MyPane bottom;

        private int indexI;
        private int indexJ;
        private Timeline rotationAnimation;

        public MyPane(Text text, int i, int j) {
            super();
            indexI = i;
            indexJ = j;
            this.mText = text;
            getChildren().addAll(text);

            //this.mText.setText(String.format("%d %d", i, j));
        }

        public int getIndexI() {
            return indexI;
        }

        public int getIndexJ() {
            return indexJ;
        }

        public void setBlock(boolean block) {
            if (block) {
                setColor(Color.BLACK);
            }
            isBlock = block;
        }

        public boolean isBlock() {
            return isBlock;
        }

        public void setActive() {
            if (!isBlock) {
                setColor(Color.WHEAT);
                mText.setFill(Color.BLACK);
            }

        }

        public void setInactive() {
            if (!isBlock) {
                setColor(mColor);
                mText.setFill(Color.WHEAT);
            }
        }

        public void setText(String text) {
            mText.setText(text);
        }

        boolean hasNumber() {
            return !mText.getText().isEmpty();
        }

        public void setLeft(MyPane left) {
            this.left = left;
        }

        public void setRight(MyPane right) {
            this.right = right;
        }

        public void setTop(MyPane top) {
            this.top = top;
        }

        public void setBottom(MyPane bottom) {
            this.bottom = bottom;
        }


        public MyPane getTop() {
            return top;
        }

        public MyPane getBottom() {
            return bottom;
        }

        public MyPane getLeft() {
            return left;
        }

        public MyPane getRight() {
            return right;
        }

        public void setOriginalColor(Color color) {
            this.mColor = color;
            setColor(color);
        }

        private void setColor(Color color) {
            setBackground(new Background(new BackgroundFill(color, null, new Insets(1))));
        }

        public static void row(int counter,List<MyPane> panes) {



            List<MyPane> list = new ArrayList<>();
            for (MyPane pane : panes) {

                pane.setActive();

                if (!pane.hasNumber()) {
                    pane.setText(String.valueOf(counter));
                }

                if(traverse(counter + 1, pane.getLeft()))list.add(pane.getLeft());
                if(traverse(counter + 1, pane.getTop()))list.add(pane.getTop());
                if(traverse(counter + 1, pane.getRight()))list.add(pane.getRight());
                if(traverse(counter + 1, pane.getBottom()))list.add(pane.getBottom());
            }


            if(!list.isEmpty()){
                row(counter + 1,list);
            }


        }


        public static boolean traverse(int counter, MyPane pane) {

            if (pane == null) return false;
            if (pane.isBlock()) return false;
            if(pane.hasNumber())return false;
            pane.setText(String.valueOf(counter));
            pane.setActive();
            return true;


        }

        public void startRotate() {

            if (rotationAnimation != null) return;
            final Translate rotationTransform = new Translate(0, 0);

            getTransforms().add(rotationTransform);

            // rotate a square using timeline attached to the rotation transform's angle property.
            rotationAnimation = new Timeline();
            rotationAnimation.getKeyFrames()
                    .add(
                            new KeyFrame(
                                    Duration.seconds(5),
                                    new KeyValue(
                                            rotationTransform.xProperty(),
                                            360
                                    )
                            )
                    );
            rotationAnimation.setCycleCount(Animation.INDEFINITE);
            rotationAnimation.play();
        }

        public void stopRotate() {
            if (rotationAnimation != null) {
                rotationAnimation.stop();
                rotationAnimation = null;
                getTransforms().clear();
            }
        }
    }


    static class Player extends Circle{
        MyPane pane;
        public Player(MyPane pane){
            super(10,Color.RED);
            this.pane = pane;

        }

        public void act(){



            setCenterX(pane.getLayoutX() + pane.getWidth()/2);
            setCenterY(pane.getLayoutY() + pane.getHeight()/2);

        }
    }

}

