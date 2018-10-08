package physicsPort.body;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class PhysicsBody {


    public int type;
    public String userData;
    public Object texture;
    public Object textureData;
    public List<Fixture>     fixtures;
    public float[] position;
    public float rotation;
    public boolean isBullet;
    public int isFixedRotation;
    public float linearDamping;
    public float angularDamping;

    public  PhysicsBody(String type){
        this.type = parseInt(type);
        this.userData = "";
        this.fixtures = new ArrayList<>();

        this.position = new float[]{0, 0};
        this.rotation = 0;
        this.isBullet = false;
        this.isFixedRotation = 0;
        this.linearDamping = 0;
        this.angularDamping = 0;
    }
}
