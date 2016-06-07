package mygame.level;

/**
 * Created by Makhobey Oleh on 6/7/16.
 * email: tajcig@ya.ru
 */
public class Tile {

    private int x;
    private int y;
    private int width;
    private int height;
    private int position;

    public Tile(int x, int y, int width, int height, int position) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.position = position;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPosition() {
        return position;
    }
}
