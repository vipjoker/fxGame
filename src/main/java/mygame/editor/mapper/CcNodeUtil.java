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
    private static int ID;
    public static List<EntityNode> flat(CcNode root, IntSupplier idGenerator) {
        List<EntityNode> entityNodes = new ArrayList<>();
        ID = idGenerator.getAsInt();
        traverseNodes(0,entityNodes,root);
        return entityNodes;


    }

    public static void traverseNodes(int parentId ,List<EntityNode> nodes,CcNode root){

        EntityNode parent = EntityNodeMapper.map(root);
        parent.setId(ID++);
        parent.setParentId(parentId);
        nodes.add(parent);
        for (CcNode ccNode : root.getChildren()) traverseNodes(parent.getId(),nodes,ccNode);

    }

    public static CcNode unflat(List<EntityNode> nodes) {
        CcNode root = null;

        List<CcNode> ccNodes = nodes.stream().map(EntityNodeMapper::map).collect(Collectors.toList());

        BiFunction<List<CcNode>,Integer,CcNode> func = (list ,id)->list.stream().filter(n->n.id == id).findFirst().get();

        for (EntityNode node : nodes) {

            List<EntityNode> collect = nodes.stream().filter(n -> n.getParentId() == node.getId()).collect(Collectors.toList());
            CcNode ccNode = func.apply(ccNodes,node.getId());


            collect.forEach(n->ccNode.addChild(func.apply(ccNodes,n.getId())));

            if(node.getParentId() == 0){
                root = ccNode;
            }
        }
        return root;
    }

}
