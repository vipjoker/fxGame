package mygame.editor.actions;


import mygame.editor.component.EditComponent;
import mygame.editor.model.Point;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcNode;

public class EditAction extends Action{

    public EditAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);
    }

    @Override
    public void init() {
        mRenderer.getNodes().clear();

        CcNode rootNode = mRepository.getRootNode();


        for (CcNode ccNode : rootNode.getChildren()) {
            ccNode.addComponent(new EditComponent());
        }
        mRenderer.addChild(rootNode);


        mRenderer.update();
    }

    @Override
    public void mouseMoved(Point position) {

    }

    @Override
    public void mousePressed(Point position) {

    }

    @Override
    public void mouseReleased(Point position) {

    }

    @Override
    public void finishDrawing() {

    }
}
