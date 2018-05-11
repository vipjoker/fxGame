package mygame.editor.data.entities;

/**
 * Created by oleh on 30.04.18.
 */
public class EntityNode {
    private int id;
    private float x;
    private float y;
    private float width;
    private float height;
    private float rotation;
    private String spriteUrl;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
