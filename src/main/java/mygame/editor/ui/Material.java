package mygame.editor.ui;

/**
 * Created by oleh on 23.04.18.
 */
public class Material {
    private float density;
    private float friction;
    private float restitution;


    public Material(float density,float friction,float restitution){
        this.density = density;
        this.friction = friction;
        this.restitution = restitution;
    }

    public static Material DEFAULT (){
        return new Material(1,0.5f,0.5f);
    }

    public float getDensity() {
        return density;
    }

    public float getFriction() {
        return friction;
    }

    public float getRestitution() {
        return restitution;
    }
}


