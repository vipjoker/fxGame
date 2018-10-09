package physicsPort.body;


import javafx.scene.image.Image;

public class Sprite {
    public float width;
    public float height;
    public Image image;

    public Sprite(Image image, float w, float h) {
        this.image = image;
        this.width = w;
        this.height = h;
    }

    public Sprite(Image image) {
        this.image = image;
        this.width = ((float) image.getWidth());
        this.height = ((float) image.getHeight());
    }
}
