package newStuff.canvas.swing;

import javax.swing.tree.DefaultMutableTreeNode;

class ObjectNode extends DefaultMutableTreeNode {
    final SceneObject object;
    final String label;

    ObjectNode(SceneObject object, String label) {
        super(label);
        this.object = object;
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
