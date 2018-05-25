package mygame.editor.mapper;

import mygame.editor.component.SpriteComponent;
import mygame.editor.data.entities.EntitySprite;

/**
 * Created by oleh on 25.05.18.
 */
public class SpriteComponentMapper {
    public static SpriteComponent map(EntitySprite entitySprite){
        SpriteComponent spriteComponent = new SpriteComponent(null);
        return spriteComponent;
    }
}
