package physicsPort.body;

import java.util.ArrayList;
import java.util.List;

public class Fixture {

    public List<PhysicsShape> shapes;
    public float restitution;
    public float friction;
    public float density;
    public boolean isSensor;
    public int maskBits;
    public int categoryBits;
    public int groupIndex;


    // exporting objects //
    public Fixture() {
        this.shapes = new ArrayList<>();
        this.isSensor = false;
        this.maskBits = 65535;
        this.categoryBits = 1;
        this.groupIndex = 0;
    }
}
