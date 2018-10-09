package physicsPort.body;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class PhysicsShape {


    public int type;
    public float[] position;
    public List<Vertex> vertices;
    public float width;
    public float height;
    public float radius;

    public PhysicsShape(int type) {
        this.type = type;
        this.position = new float[2];                // position relative to body
        this.vertices = new ArrayList<>();
    }
}
