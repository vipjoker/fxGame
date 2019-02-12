package mygame.editor.parser.model;

import java.util.List;

public class Scene {
    private List<Node> children;
    private List<Joint> joints;

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public List<Joint> getJoints() {
        return joints;
    }

    public void setJoints(List<Joint> joints) {
        this.joints = joints;
    }
}
