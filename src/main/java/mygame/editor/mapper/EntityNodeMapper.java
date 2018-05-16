package mygame.editor.mapper;

import mygame.editor.data.entities.EntityNode;
import mygame.editor.views.CcNode;

import java.util.List;

/**
 * Created by oleh on 11.05.18.
 */
public class EntityNodeMapper {
    public static EntityNode map(CcNode node){
        EntityNode entityNode = new EntityNode();
        entityNode.setId(node.id);
        entityNode.setName(node.name);
        entityNode.setHeight((float) node.height);
        entityNode.setWidth((float) node.width);
        entityNode.setX((float) node.x);
        entityNode.setY((float) node.y);
        entityNode.setRotation((float) node.angle);
        return entityNode;
    }

    public static CcNode map(EntityNode node){
        CcNode ccNode = new CcNode();
        ccNode.angle = node.getRotation();
        ccNode.name = node.getName();
        ccNode.x = node.getX();
        ccNode.y = node.getY();
        ccNode.width = node.getWidth();
        ccNode.height = node.getHeight();
        ccNode.id = node.getId();
        return ccNode;
    }



}
