package mygame.editor.mapper;

import javafx.beans.property.SimpleStringProperty;
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
        entityNode.setName(node.name.getName());
        entityNode.setHeight((float) node.getHeight());
        entityNode.setWidth((float) node.getWidth());
        entityNode.setX(node.getX().floatValue());
        entityNode.setY(node.getY().floatValue());
        entityNode.setRotation((float) node.getAngle());
        return entityNode;
    }

    public static CcNode map(EntityNode node){
        CcNode ccNode = new CcNode();
        ccNode.setAngle(node.getRotation());
        ccNode.name = new SimpleStringProperty(node.getName());
        ccNode.setX(node.getX());
        ccNode.setY(node.getY());
        ccNode.setWidth(node.getWidth());
        ccNode.setHeight(node.getHeight());
        ccNode.id = node.getId();
        return ccNode;
    }



}
