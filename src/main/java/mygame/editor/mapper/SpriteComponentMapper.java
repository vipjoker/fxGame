package mygame.editor.mapper;

import javafx.scene.image.Image;
import mygame.editor.component.SpriteComponent;
import mygame.editor.data.entities.EntitySprite;
import mygame.editor.util.ImageUtil;

/**
 * Created by oleh on 25.05.18.
 */
public class SpriteComponentMapper {
    public static SpriteComponent map(EntitySprite entitySprite){

        SpriteComponent spriteComponent = new SpriteComponent(entitySprite.getUrl());
        return spriteComponent;
    }

    public static EntitySprite map (SpriteComponent component,int parentId){
        EntitySprite entitySprite = new EntitySprite();
        entitySprite.setUrl(component.getUrl());
        entitySprite.setId(0);
        entitySprite.setParentId(parentId);
        return entitySprite;

    }
}
