package mygame.editor.mapper;

import mygame.editor.data.entities.EntityNode;
import mygame.editor.views.CcNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

/**
 * Created by oleh on 16.05.18.
 */
public class CcNodeUtil {
    public static List<EntityNode> flat(CcNode root,List<CcNode> list) {
        List<EntityNode> entityNodes = new ArrayList<>();
        traverseNodes(0,entityNodes,root,list);
        return entityNodes;


    }

    public static void traverseNodes(int parentId ,List<EntityNode> nodes,CcNode root,List<CcNode> ccNodes){

        EntityNode parent = EntityNodeMapper.map(root);
        ccNodes.add(root);
        parent.setId(root.id);
        parent.setParentId(parentId);
        nodes.add(parent);
        for (CcNode ccNode : root.getChildren())
            traverseNodes(parent.getId(),nodes,ccNode,ccNodes);

    }



    public static List<CcNode> toCcNodesList(List<EntityNode> entityNodes){
        List<CcNode> ccNodes = entityNodes.stream().map(EntityNodeMapper::map).collect(Collectors.toList());
        return ccNodes;
    }

    public static CcNode buildNodeTree(List<EntityNode> nodes,List<CcNode> ccNodes) {
        CcNode root = null;

        BiFunction<List<CcNode>,Integer,CcNode> find = (list ,id)->list.stream().filter(n->n.id == id).findFirst().get();

        for (EntityNode node : nodes) {

            List<EntityNode> collect = nodes.stream().filter(n -> n.getParentId() == node.getId()).collect(Collectors.toList());
            CcNode ccNode = find.apply(ccNodes,node.getId());


            collect.forEach(n->ccNode.addChild(find.apply(ccNodes,n.getId())));

            if(node.getParentId() == 0){
                root = ccNode;
            }
        }
        return root;
    }

}
