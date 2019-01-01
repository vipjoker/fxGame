package mygame.editor.actions;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeModel;


public abstract class Action implements ChangeListener<String>{

    protected final CanvasRenderer mRenderer;
    protected final NodeModel mRepository;
    protected String mode;
    protected Action(CanvasRenderer renderer, NodeModel repository) {
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