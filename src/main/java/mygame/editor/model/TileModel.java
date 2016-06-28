package mygame.editor.model;

/**
 * Created by Makhobey Oleh on 6/28/16.
 * email: tajcig@ya.ru
 */
public class TileModel {

    private String path;
    private int x;
    private int y;
    private int width;
    private int height;

    public TileModel(int height, int width, int y, int x, String path) {
        this.height = height;
        this.width = width;
        this.y = y;
        this.x = x;
        this.path = path;
    }

    public TileModel (){

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
