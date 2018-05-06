package mygame.editor.views;

public class Global {
    private static double width;
    private static double height;

    public static double getWidth() {
        return width;
    }

    public static double getHeight() {
        return height;
    }

    public static void setWidth(double width) {
        Global.width = width;
    }

    public static void setHeight(double height) {
        Global.height = height;
    }
}
