package mygame.editor.actions;


import javafx.geometry.Point2D;
import mygame.editor.component.Component;
import mygame.editor.component.EditComponent;
import mygame.editor.render.CanvasRenderer;
import mygame.editor.repository.NodeRepository;
import mygame.editor.views.CcNode;

public class EditAction extends Action implements CanvasRenderer.OnCanvasDragListener{

    private CcNode rootNode;
    private CcNode selected;
    private boolean isResized = false;
    private boolean isRotated = false;
    public EditAction(CanvasRenderer renderer, NodeRepository repository) {
        super(renderer,repository);

    }
    @Override
    public void init() {
        mRenderer.getNodes().clear();

        rootNode = mRepository.getRootNode();


        for (CcNode ccNode : rootNode.getChildren()) {
            ccNode.addComponent(new EditComponent());
        }
        mRenderer.setOnCanvasDragListener(this);
        mRenderer.addChild(rootNode);
        mRenderer.update();
    }


    @Override
    public void finishDrawing() {
        mRenderer.setOnCanvasDragListener(null);
        rootNode.updateAll(n->n.setActive(false));
        mRepository.deleteAll();
        mRepository.save(rootNode);
        selected = null;
    }

    @Override
    public void onStartMove(Point2D point) {

        if(selected!=null){
            Point2D convertToLocalSpace = selected.convertToLocalSpace(point);

            EditComponent editComponent = selected.getComponent(Component.Type.EDIT);

            boolean contains = editComponent.canBeResized(convertToLocalSpace);
            isRotated = editComponent.canBeRotated(convertToLocalSpace);

            editComponent.setActive(contains);


            if(isRotated){
                mRenderer.update();
                return;
            }

            if(contains){
                isResized = true;
                mRenderer.update();
                return;
            }
        }



        rootNode.updateAll(n->n.setActive(false));
        selected = rootNode.getSelected(point);

        if(selected!=null){
            selected.setActive(true);
        }
        mRenderer.update();
    }

    @Override
    public void onDrag(Point2D point) {
        if(selected != null ){
            if(isResized) {

                double width = selected.getWidth() - point.getX();
                double height = selected.getHeight() + point.getY();
                selected.setWidth(width);
                selected.setHeight(height);

            }
            if(isRotated){
                selected.appendAngle(point.getX());

            }
            mRenderer.update();


        }
    }

    @Override
    public void onStopMove(Point2D point) {

        isResized = false;
        isRotated = false;
    }
}
