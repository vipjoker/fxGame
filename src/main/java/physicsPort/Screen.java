package physicsPort;

import com.badlogic.gdx.math.Vector2;
import javafx.scene.canvas.GraphicsContext;
import physicsPort.gameobject.Mover;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {
    protected List<GameObject> gameObjects = new ArrayList<>();
    public float touchX;
    public float touchY;
    public boolean isTouched;

    public float width;
    public float height;

    public abstract void init();

    public void setSize(float width, float height){
        this.width = width;
        this.height = height;
    }

    protected void addChild(Mover mover) {
        gameObjects.add(mover);
    }

    public void draw(GraphicsContext context){
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(context);
        }
    }
}
