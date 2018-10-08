package physicsPort.body;

public class LineSegment {

    public float sX;
    public float sY;
    public float eX;
    public float eY;

    public float signX;
    public float signY;

    public LineSegment(float startX,float startY,float endX,float endY){
        this.sX = startX;
        this.sY = startY;
        this.eX = endX;
        this.eY = endY;


        // to keep track of whether sX > eX
        this.signX = 1;
        if (endX > startX){
            this.signX *= -1;
        }

        // to keep track of whether sY > eY
        this.signY = 1;
        if (endY > startY){
            this.signY *= -1;
        }
    }


    /**
     *
     * returns perpendicular distance between point and the line segment
     */
    public float distanceFromPoint (float pX, float pY){
        float distance = Math.abs((eX - sX) * (sY - pY) - (sX - pX) * (eY - sY));
        distance /= Math.pow( (eX - sX) * (eX - sX) + (eY - sY) * (eY - sY) , 0.5);
        return distance;
    }

/**
 *
 * returns true if point's x coordinate lies in line segment bounds
 */
    public boolean checkInBoundsX (float px){
        return (signX * px < signX * sX && signX * px > signX * eX);
    }

/**
 *
 * returns true if point's y coordinate lies in line segment bounds
 */
    public boolean checkInBoundsY (float py){
        return (signY * py < signY * sY && signY * py > signY * eY);
    }

/**
 *
 * params bounds => [x, y, width, height]
 * returns true if the line segment bounds contains the rectangle bounds
 */
    public boolean checkInBoundsAABB (float[] bounds){
        float[] lineBounds = {(this.eX + this.sX) / 2, (this.eY + this.sY) / 2, Math.abs(this.eX - this.sX), Math.abs(this.eY - this.sY)};

        if (lineBounds[0] + lineBounds[2] / 2 < bounds[0] - bounds[2] / 2) return false;
        if (lineBounds[0] - lineBounds[2] / 2 > bounds[0] + bounds[2] / 2) return false;
        if (lineBounds[1] + lineBounds[3] / 2 < bounds[1] - bounds[3] / 2) return false;
        if (lineBounds[1] - lineBounds[3] / 2 > bounds[1] + bounds[3] / 2) return false;

        return true;
    }

}
