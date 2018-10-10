package physicsPort.viewport;

public class Navigator {
    // to handle canvas panning and zooming

    public float[] panning;
    public float[] origin;
    public float scale;
    public float[] scaleLimits;
    public float [] grid;
    public int range;

    public  Navigator(){
        // canvas manipulating parameters
        this.panning = new float[]{0, 0};						// canvas translation.[x, y]
        this.origin = new float[]{0, 0};						// canvas origin.[x, y]
        this.scale = 1;								// canvas scale (scaleX = scaleY)
        this.scaleLimits = new float []{0.5f, 3};					// [min scale, max scale]
        this.grid = new float []{0, 0};							// [range, cell_size]
    }

    public float [] screenPointToWorld (float x, float y){
        return 	new float[]{
                (x / this.scale - this.panning[0] + this.origin[0]),
                (y / this.scale - this.panning[1] + this.origin[1])
        };
    }


    public float [] worldPointToScreen (float x,float y) {
        return new float[]{
        (x + this.panning[0] - this.origin[0]) * this.scale,
                (y + this.panning[1] - this.origin[1]) * this.scale
    };
    }

    public boolean checkPointInAABB (float x,float y,float[] bounds){
        // world parametes to screen
        float width = bounds[2] * this.scale;
        float height = bounds[3] * this.scale;
        float[] position = this.worldPointToScreen(bounds[0], bounds[1]);
        // ------------------------- //
        return ((x >= position[0] - width / 2 && x <= position[0] + width / 2) & (y >= position[1] - height / 2 && y <= position[1] + height / 2));
    };
}
