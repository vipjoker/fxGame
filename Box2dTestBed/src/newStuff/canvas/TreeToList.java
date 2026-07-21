package newStuff.canvas;


import newStuff.canvas.model.NodeEntity;
import newStuff.canvas.model.NodeTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeToList {

    // Convert one root tree to flat entities
    public static List<NodeEntity> toEntities(NodeTree root) {
        List<NodeEntity> result = new ArrayList<>();
        dfs(root, null, result);
        return result;
    }

    // Convert multiple roots (forest) to flat entities
    public static List<NodeEntity> toEntities(List<NodeTree> roots) {
        List<NodeEntity> result = new ArrayList<>();
        for (NodeTree root : roots) {
            dfs(root, null, result);
        }
        return result;
    }

    private static void dfs(NodeTree node, Long parentId, List<NodeEntity> out) {
        if (node == null) return;

        // Add current node with its parent id
        out.add(new NodeEntity(node.getId(), parentId, node.getName()));

        // Recurse children with current node id as parent id
        for (NodeTree child : node.getChildren()) {
            dfs(child, node.getId(), out);
        }
    }

    // quick demo
    public static void main(String[] args) {
        NodeTree root = new NodeTree(1L, "A");
        NodeTree b = new NodeTree(2L, "B");
        NodeTree c = new NodeTree(3L, "C");
        NodeTree d = new NodeTree(4L, "D");
        NodeTree e = new NodeTree(5L, "E");

        root.addChild(b).addChild(c);
        b.addChild(d);
        c.addChild(e);

        List<NodeEntity> entities = toEntities(root);
        entities.forEach(System.out::println);

        NodeTree tree = toTree2(entities);
        System.out.println(tree);

    }


    public static NodeTree toTree2(List<NodeEntity> entities) {
        Map<Long, NodeTree> map = new HashMap<>();
        List<NodeTree> roots = new ArrayList<>();

        for (NodeEntity entity : entities) {
            NodeTree node = new NodeTree(entity.getId(), entity.getName());
            map.put(entity.getId(), node);
        }
        for(NodeEntity entity : entities) {
            NodeTree node = map.get(entity.getId());
            if(entity.getParentId() == null){
                roots.add(node);
            }else{
                Long parentId = entity.getParentId();
                NodeTree parent = map.get(parentId);
                parent.addChild(node);
            }
        }
        return roots.get(0);


    }


    // Build forest (multiple roots supported)
    public static List<NodeTree> toTree(List<NodeEntity> entities) {
        Map<Long, NodeTree> nodeMap = new HashMap<>();
        List<NodeTree> roots = new ArrayList<>();

        // 1) create all nodes
        for (NodeEntity e : entities) {
            if (nodeMap.containsKey(e.getId())) {
                throw new IllegalArgumentException("Duplicate id found: " + e.getId());
            }
            nodeMap.put(e.getId(), new NodeTree(e.getId(), e.getName()));
        }

        // 2) connect parent-child
        for (NodeEntity e : entities) {
            NodeTree current = nodeMap.get(e.getId());
            Long parentId = e.getParentId();

            if (parentId == null) {
                roots.add(current);
            } else {
                NodeTree parent = nodeMap.get(parentId);
                if (parent == null) {
                    throw new IllegalArgumentException(
                            "Parent id " + parentId + " not found for node " + e.getId()
                    );
                }
                parent.getChildren().add(current);
            }
        }

        return roots;
    }

    // Optional helper if you expect exactly one root
    public static NodeTree toSingleRootTree(List<NodeEntity> entities) {
        List<NodeTree> roots = toTree(entities);
        if (roots.isEmpty()) {
            return null;
        }
        if (roots.size() > 1) {
            throw new IllegalStateException("Expected one root, found: " + roots.size());
        }
        return roots.get(0);
    }


}
