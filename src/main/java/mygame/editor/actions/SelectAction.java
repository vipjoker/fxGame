package mygame.editor.actions;

import mygame.editor.App;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcNode;

import java.util.List;

public class SelectAction extends Action {

    public SelectAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer, repository);
    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();
        final List<CcNode> nodes = App.instance.repository.getNodes();
        mRenderer.getNodes().addAll(nodes);
    }

    @Override
    public void finishDrawing() {

    }
}
