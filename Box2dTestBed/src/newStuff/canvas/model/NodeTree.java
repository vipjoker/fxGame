package newStuff.canvas.model;

import java.util.List;

public class NodeTree {

    private final String name;
    private Long id;
    private List<NodeTree> children;

    public NodeTree(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public NodeTree addChild(NodeTree b) {
        return null;
    }

    public List<NodeTree> getChildren() {
        return children;
    }

    public void setChildren(List<NodeTree> children) {
        this.children = children;
    }

    public Long getId() {
            return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
