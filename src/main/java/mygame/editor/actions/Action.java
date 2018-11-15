package mygame.editor.actions;


import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.util.Constants;
import mygame.editor.model.Point;

import java.util.List;


public abstract class Action implements ChangeListener<String>{

    protected final CanvasRenderer mRenderer;
    protected final NodeRepository mRepository;
    protected String mode;
    protected Action(CanvasRenderer renderer,NodeRepository repository) {
        this.mRenderer = renderer;
        this.mRepository = repository;
    }

    public abstract void init();

    public abstract void finishDrawing();

    public void setMode(String mode){
        this.mode = mode;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

    }
}