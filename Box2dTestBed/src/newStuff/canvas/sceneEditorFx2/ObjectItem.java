package newStuff.canvas.sceneEditorFx2;

// ===== Hierarchy tree item =====
class ObjectItem {
    final SceneObject object;
    final String label;

    ObjectItem(SceneObject object, String label) {
        this.object = object;
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
