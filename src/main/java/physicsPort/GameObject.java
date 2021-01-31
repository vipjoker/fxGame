package physicsPort;

import com.badlogic.gdx.math.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class GameObject {

    private final Screen screen;
    private GraphicsContext context;
    protected Vector2 position = new Vector2();
    protected float angle;
    private Color fillColor = Color.WHITE;


    public GameObject(Screen screen){
        this.screen = screen;
    }


    public Screen getScreen() {
        return screen;
    }

    public final void draw(GraphicsContext ctx){
        this.context = ctx;
        update();
        render();
    }




    public final void drawLine(float x,float y,float x2,float y2){
        context.moveTo(x,y);
        context.lineTo(x2,y2);
    }

    public final void drawRect(float x,float y,float width,float height){
        context.fillRect(x,y,width,height);
    }

    public final void drawCircle(float x,float y,float radius){
        context.fillOval(x,y,radius,radius);
    }

    public abstract void render();

    public abstract void update();
}
