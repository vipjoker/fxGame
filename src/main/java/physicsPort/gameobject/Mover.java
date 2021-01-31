package physicsPort.gameobject;

import physicsPort.GameObject;
import physicsPort.Screen;

public class Mover extends GameObject {


    public Mover(Screen screen) {
        super(screen);
    }

    @Override
    public void render() {
        drawCircle(position.x,position.y,10);
    }

    @Override
    public void update() {
        position.add(1, 1);
    }
}
