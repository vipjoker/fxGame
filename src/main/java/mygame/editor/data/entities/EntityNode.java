package mygame.editor.data.entities;

import java.util.List;

/**
 * Created by oleh on 30.04.18.
 */
public class EntityNode {
    private int id;
    private float x;
    private float y;
    private String name;
    private float width;
    private float height;
    private float rotation;
    private int parentId;
    private String spriteUrl;
    private EntityBody body;
    private List<EntityFixture> fixtures;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getRotation() {
        return rotation;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public EntityBody getBody() {
        return body;
    }

    public void setBody(EntityBody body) {
        this.body = body;
    }

    public List<EntityFixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<EntityFixture> fixtures) {
        this.fixtures = fixtures;
    }
}
