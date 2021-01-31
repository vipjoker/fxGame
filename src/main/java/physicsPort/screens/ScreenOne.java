package physicsPort.screens;

import physicsPort.Screen;
import physicsPort.gameobject.Mover;

public class ScreenOne extends Screen {

    @Override
    public void init() {
        Mover mover = new Mover(this);
        addChild(mover);

    }


}
