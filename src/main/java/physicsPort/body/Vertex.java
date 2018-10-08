package physicsPort.body;

public class Vertex {
    public float x;
    public float y;
    public float width;
    public float height;
    public boolean isSelected;

    /**
     *
     * params x coordinate of vertex
     * params y coordinate of vertex
     * params width for collision detection
     * params height for collision detection
     */


    public Vertex(float x, float y,float  w,float  h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.isSelected = false;
    }
    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move (float dx,float  dy) {
        this.x += dx;
        this.y += dy;
    }

    public Vertex clone (){
        return new Vertex(x,y,width,height);
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vertex))return  false;
        Vertex v = (Vertex)obj;
        return this.x == v.x && this.y == v.y && this.width == v.width && this.height == v.height;
    }
}
