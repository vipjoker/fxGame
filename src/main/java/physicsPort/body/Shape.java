package physicsPort.body;

import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * params type of shape
 * params width, needed when type = BOX | CIRCLE
 * params height, needed when type = BOX
 */
public class Shape {

    public static final int SHAPE_BOX = 0;
    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_POLYGON = 2;
    public static final int SHAPE_CHAIN = 3;
    public static final int SHAPE_NONE = 4;

    public int shapeType;
    public float[] position;                        // position
    public float[] scaleXY;                        // scale
    public float rotation;                            // only for editor purpose
    public List<Vertex> vertices;
    public float[] bounds;                    // AABB for selecting
    public float[] centroid;                        // centroid for shape
    public boolean isSelected;
    public boolean inEditMode;

    public float friction;
    public float restitution;
    public float density;
    public boolean isSensor;
    public int maskBits;
    public int categoryBits;
    public int groupIndex;
    public float width;
    public float height;
    public float radius;

    public Shape(int type, float width, float height) {
        this.shapeType = type;
        this.position = new float[]{0, 0};                        // position
        this.scaleXY = new float[]{1, 1};                        // scale
        this.rotation = 0;                            // only for editor purpose
        this.vertices = new ArrayList<>();
        this.bounds = new float[]{0, 0, 0, 0};                    // AABB for selecting
        this.centroid = new float[]{0, 0};                        // centroid for shape
        this.isSelected = false;
        this.inEditMode = false;

        // fixture properties
        this.friction = 1;
        this.restitution = 0.25f;
        this.density = 1;
        this.isSensor = false;
        this.maskBits = 65535;
        this.categoryBits = 1;
        this.groupIndex = 0;

        if (type == Shape.SHAPE_NONE) {
            this.shapeType = Shape.SHAPE_POLYGON;
            return;
        }

        if (type == Shape.SHAPE_CHAIN || type == Shape.SHAPE_POLYGON) {
            this.density = type == Shape.SHAPE_CHAIN ? 0 : 1;
            int size = 10;
            if ((width != 0 && height == 0) || (width == 0 && height != 0)) {
                width = 50;
                float angle = 0;
                int resolution = 10;
                for (int i = 0; i < resolution; i++) {
                    angle = 2 * (float) Math.PI * i / resolution;
                    Vertex vertex = new Vertex(width * (float) Math.cos(angle), width * (float) Math.sin(angle), size, size);
                    this.vertices.add(vertex);
                }
            } else {
                width = width == 0 ? 100 : width;
                height = height == 0 ? 100 : height;
                this.vertices.add(new Vertex(-width / 2, -height / 2, size, size));
                this.vertices.add(new Vertex(width / 2, -height / 2, size, size));
                this.vertices.add(new Vertex(width / 2, height / 2, size, size));
                this.vertices.add(new Vertex(-width / 2, height / 2, size, size));
            }
        } else if (type == Shape.SHAPE_BOX) {
            this.width = width == 0 ? 100 : width;
            this.height = height == 0 ? 100 : height;

            int size = 10;
            this.vertices.add(new Vertex(-this.width / 2, -this.height / 2, size, size));
            this.vertices.add(new Vertex(this.width / 2, -this.height / 2, size, size));
            this.vertices.add(new Vertex(this.width / 2, this.height / 2, size, size));
            this.vertices.add(new Vertex(-this.width / 2, this.height / 2, size, size));
        } else if (type == Shape.SHAPE_CIRCLE) {
            this.radius = width / 2 == 0 ? 50 : width / 2;

            float angle = 0;
            int resolution = 10;
            int size = 10;
            for (int i = 0; i < resolution; i++) {
                angle = 2 * (float) Math.PI * i / resolution;
                Vertex vertex = new Vertex(this.radius * (float) Math.cos(angle), this.radius * (float) Math.sin(angle), size, size);
                this.vertices.add(vertex);
            }
        }
    }

    public void addVertex(float x, float y) {
        Vertex v = new Vertex(x, y, 10, 10);

        // do not edit BOX and CIRCLE shape
        if (this.shapeType == Shape.SHAPE_BOX || this.shapeType == Shape.SHAPE_CIRCLE)
            return;

        if (this.vertices.size() > 3) {
            LineSegment lineSegment;
            float distance = 10000;
            int index = 0;
            for (int i = 0; i < this.vertices.size(); i++) {
                // create new line segment to calculate distance b/w vertex to be addded and each edge of the shape
                if (i == this.vertices.size() - 1) {
                    lineSegment = new LineSegment(this.vertices.get(i).x, this.vertices.get(i).y, this.vertices.get(0).x, this.vertices.get(0).y);
                } else {
                    lineSegment = new LineSegment(this.vertices.get(i).x, this.vertices.get(i).y, this.vertices.get(i + 1).x, this.vertices.get(i + 1).y);
                }

                // if distance is smaller then preceding edge, update the index
                if (distance > lineSegment.distanceFromPoint(v.x, v.y)) {
                    if (lineSegment.checkInBoundsX(v.x) || lineSegment.checkInBoundsY(v.y)) {
                        index = i;

                        // update distance
                        distance = lineSegment.distanceFromPoint(v.x, v.y);
                    }
                }
            }

            // if shapeType = CHAIN and distance is greate than threshold then just add the new vertex at the end of array
            if (distance > 25 && this.shapeType == Shape.SHAPE_CHAIN) {
                this.vertices.add(v);
                return;
            }

            // if distance of very large then don't add vertex
            if (distance > 100) return;

            // otherwise add the vertex on the edge (b/w two existing vertices)
            this.vertices.add(index + 1, v);
        }
        // if shape has less than 3 vertices then just push the new vertex at the end of the array
        else {
            this.vertices.add(v);
        }

        // update the bounds for the shape
        this.calculateBounds();
    }

    ;

    public void removeVertexGivenVertex(Vertex v) {
        // do not edit BOX and CIRCLE shape
        if (this.shapeType == Shape.SHAPE_BOX || this.shapeType == Shape.SHAPE_CIRCLE)
            return;

        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).equals(v)) {
                this.removeVertexGivenIndex(i);
                break;
            }
        }
    }

    ;

    public void removeVertexGivenIndex(int index) {
        if (this.shapeType == Shape.SHAPE_BOX || this.shapeType == Shape.SHAPE_CIRCLE)
            return;
        vertices.remove(index);

        // update the bounds for the shape
        this.calculateBounds();
    }

    ;

    public int indexOfVertex(Vertex v) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).equals(v)) {
                return i;
            }
        }
        return -1;
    }


    public void move(float dx, float dy) {
        this.position[0] += dx;
        this.position[1] += dy;

        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).move(dx, dy);
        }

        this.calculateBounds();
    }

    ;

    public void setPosition(float x, float y) {
        this.move(x - this.position[0], y - this.position[1]);
    }

    public void scale(float sx, float sy) {

        float pivotX = this.position[0];
        float pivotY = this.position[1];
        scale(sx, sy, pivotX, pivotY);

    }


    public void scale(float sx, float sy, float pivotX, float pivotY) {
        this.scaleXY[0] *= sx;
        this.scaleXY[1] *= sy;

        if (this.shapeType == Shape.SHAPE_BOX) {
            if (this.rotation == 0 || this.rotation % 180 == 0) {
                this.width *= sx;// * Math.cos(this.rotation);
                this.height *= sy;// * Math.sin(this.rotation);
            } else if (this.rotation % 90 == 0) {
                this.width *= sy;
                this.height *= sx;
            } else {
                float rotation = this.rotation;
                this.rotate(-rotation);

                this.width *= sx;
                this.height *= sy;


                // move the shape to new origin
                this.move(-pivotX, -pivotY);

                // update position
                this.position[0] *= sx;
                this.position[1] *= sy;

                // scale vertices
                for (int i = 0; i < this.vertices.size(); i++) {
                    this.vertices.get(i).x *= sx;
                    this.vertices.get(i).y *= sy;
                }

                // revert origin
                this.move(pivotX, pivotY);

                // reset rotation
                this.rotate(rotation);

                return;
            }
        } else if (this.shapeType == Shape.SHAPE_CIRCLE) {
            this.radius *= sx;
            sy = sx;
        }

        if (pivotX == 0 || pivotY == 0) {
            pivotX = this.position[0];
            pivotY = this.position[1];
        }

        // move the shape to new origin
        this.move(-pivotX, -pivotY);

        // update position
        this.position[0] *= sx;
        this.position[1] *= sy;

        // scale vertices
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.get(i).x *= sx;
            this.vertices.get(i).y *= sy;
        }

        // revert origin
        this.move(pivotX, pivotY);
    }

    ;

    public void setScale(float sx, float sy, float pivotX, float pivotY) {
        this.scale(sx / this.scaleXY[0], sy / this.scaleXY[1], pivotX, pivotY);
    }

    ;

    public void setWidth(float width) {
        if (this.shapeType == Shape.SHAPE_BOX)
            this.scale(width / this.width, 1);
    }



    public void setRadius(float radius) {
        if (this.shapeType == Shape.SHAPE_CIRCLE)
            this.scale(radius / this.radius, 1);
    }



    public void setHeight (float height){
        if (this.shapeType == Shape.SHAPE_BOX)
            this.scale(1, height / this.height);
    }


    public void rotate(float angle) {
        float pivotX = this.position[0];
        float pivotY = this.position[1];
        rotate(angle, pivotX, pivotY);
    }

    // just for visualization in editor
    public void rotate(float angle, float pivotX, float pivotY) {


        // update rotation
        this.rotation += angle;

        // rotate vertices
        for (int i = 0; i < this.vertices.size(); i++) {
            float x = this.vertices.get(i).x - pivotX;
            float y = this.vertices.get(i).y - pivotY;
            float newAngle = angle + (float) Math.atan2(y, x) * 180 / (float) Math.PI;
            float length = (float) Math.pow(x * x + y * y, 0.5);
            this.vertices.get(i).x = pivotX + length * (float) Math.cos(newAngle * (float) Math.PI / 180);
            this.vertices.get(i).y = pivotY + length * (float) Math.sin(newAngle * (float) Math.PI / 180);
        }

        // update position
        float x = this.position[0] - pivotX;
        float y = this.position[1] - pivotY;
        float newAngle = angle + (float) Math.atan2(y, x) * 180 / (float) Math.PI;
        float length = (float) Math.pow(x * x + y * y, 0.5);
        this.position[0] = pivotX + length * (float) Math.cos(newAngle * Math.PI / 180);
        this.position[1] = pivotY + length * (float) Math.sin(newAngle * Math.PI / 180);
    }

    public void setRotation(float angle, float pivotX, float pivotY) {
        this.rotate(angle - this.rotation, pivotX, pivotY);
    }

    ;

    public void calculateBounds() {
        float minX = 100000, maxX = -100000, minY = 100000, maxY = -100000;
        Vertex v;
        for (Vertex vertice1 : this.vertices) {
            v = vertice1;
            minX = Math.min(minX, v.x);
            maxX = Math.max(maxX, v.x);
            minY = Math.min(minY, v.y);
            maxY = Math.max(maxY, v.y);
        }
        this.bounds[0] = (maxX + minX) / 2;
        this.bounds[1] = (maxY + minY) / 2;
        this.bounds[2] = maxX - minX;
        this.bounds[3] = maxY - minY;

        // update centroid
        this.centroid = new float[]{0, 0};
        for (Vertex vertice : this.vertices) {
            this.centroid[0] += vertice.x;
            this.centroid[1] += vertice.y;
        }
        this.centroid[0] /= this.vertices.size();
        this.centroid[1] /= this.vertices.size();
    }

    ;

    public Shape clone () {
        //var s = clone(this);
        //TODO implement it
        return null;
    }

    ;

    public boolean isConvex() {
        float sumOfAngles = 0;                                            // sum of interior angles
             float    angleForConvexity = (this.vertices.size() - 2) * 180;        // angle => (n - 2) * 180 for convexity
        Vertex[] edges = new Vertex[this.vertices.size()];                                                  // array of edges

        // calculate edges
        for (int i = 0; i < this.vertices.size(); i++) {
            if (i == this.vertices.size() - 1) {
                edges[i] = new Vertex(this.vertices.get(0).x - this.vertices.get(i).x, this.vertices.get(0).y - this.vertices.get(i).y);
            } else {
                edges[i] = new Vertex(this.vertices.get(i + 1).x - this.vertices.get(i).x, this.vertices.get(i + 1).y - this.vertices.get(i).y);
            }
        }

        // calculate angle b/w each edge
        for (int i = 0; i < edges.length; i++) {
            if (i == edges.length - 1) {
                Vertex vec1 = edges[i];
                        Vertex vec2 = edges[0];
                        float dot = vec1.x * vec2.x + vec1.y * vec2.y;
                        float mag1 = (float) Math.pow(vec1.x * vec1.x + vec1.y * vec1.y, 0.5);
                        float mag2 = (float) Math.pow(vec2.x * vec2.x + vec2.y * vec2.y, 0.5);
                        float angle = (float) Math.acos(dot / (mag1 * mag2));

                angle = (float)Math.PI - angle;
                sumOfAngles += angle;
            } else {
                Vertex vec1 = edges[i];
                      Vertex  vec2 = edges[i + 1];
                        float dot = vec1.x * vec2.x+ vec1.y* vec2.y;
                        float mag1 = (float)Math.pow(vec1.x* vec1.x + vec1.y * vec1.y, 0.5);
                        float mag2 = (float)Math.pow(vec2.x * vec2.x + vec2.y * vec2.y, 0.5);
                        float angle = (float) Math.acos(dot / (mag1 * mag2));

                        angle = (float) Math.PI - angle;
                sumOfAngles += angle;
            }
        }
        // convert radian to degrees (in radians gives unexpected results because of approximations)
        sumOfAngles = sumOfAngles * 180 / (float) Math.PI;
        return Math.abs(sumOfAngles - angleForConvexity) < 0.1;
    }

    ;

    public List<PhysicsShape> decomposeToConvex(float x, float y) {
        List<PhysicsShape> shapes = new ArrayList<>();
        List<Polygon> polygons = decomposeToConvex(this.vertices);
        for (int i = 0; i < polygons.size(); i++) {
            PhysicsShape shape = new PhysicsShape(Shape.SHAPE_POLYGON);
            shape.position = new float[]{this.position[0] - x, this.position[1] - y};
            for (int j = 0; j < polygons.get(i).vertices.size(); j++) {
                shape.vertices.add(new Vertex(polygons.get(i).vertices.get(j).x - this.position[0], polygons.get(i).vertices.get(j).y - this.position[1]))
                ;
            }
            shapes.add(shape);
        }
        return shapes;
    }

    ;

    public List<Polygon> decomposeToConvex(List<Vertex> vertices) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            polygon.addPoint(vertex.x, vertex.y);
        }
        return polygon.decompose();
    }

    // returns PhysicsShape for exporting
// use (x, y) as the origin for physics shape
    public List<PhysicsShape> exportShape(float x, float y) {
        List<PhysicsShape> shapes = new ArrayList<>(); // an array of physics shape (shapes if the shape is concave)

        if (this.shapeType == Shape.SHAPE_BOX && this.rotation == 0) {
            PhysicsShape pShape = new PhysicsShape(Shape.SHAPE_BOX);
            pShape.width = this.width;
            pShape.height = this.height;
            pShape.position = new float[]{this.position[0] - x, this.position[1] - y};
            shapes.add(pShape);
            return shapes;
        } else if (this.shapeType == Shape.SHAPE_CIRCLE) {
            PhysicsShape pShape = new PhysicsShape(Shape.SHAPE_CIRCLE);
            pShape.radius = this.radius / 2;
            pShape.position = new float[]{this.position[0] - x, this.position[1] - y};
            shapes.add(pShape);
            return shapes;
        }

        PhysicsShape pShape = new PhysicsShape(this.shapeType == Shape.SHAPE_BOX ? Shape.SHAPE_POLYGON : this.shapeType);
        pShape.position =new float[] {this.position[0] - x, this.position[1] - y};
        // need to check for convexity if shape is polygon
        if (this.shapeType == Shape.SHAPE_POLYGON) {
            // is shape convex
            if (this.isConvex()) {
                // just export it
                for (int i = 0; i < this.vertices.size(); i++) {
                    pShape.vertices.add(new Vertex(this.vertices.get(i).x - this.position[0], this.vertices.get(i).y - this.position[1]));    // vertex position relative to shape
                }
                shapes.add(pShape);
                return shapes;
            }
            // decompose concave shape to convex shapes
            else {
                // decompose shape
                shapes = this.decomposeToConvex(x, y);
                return shapes;
            }
        }
        // just add the vertices if the shape is edge
        else {
            for (int i = 0; i < this.vertices.size(); i++) {
                pShape.vertices.add(new Vertex(this.vertices.get(i).x - this.position[0], this.vertices.get(i).y - this.position[1]));
                ;        // vertex position relative to shape
            }
            shapes.add(pShape);
            return shapes;
        }
    }

    ;

    Fixture toPhysics(float x,float y) {

        Fixture fixture = new Fixture();
        fixture.restitution = this.restitution;
        fixture.friction = this.friction;
        fixture.density = this.density;
        fixture.isSensor = this.isSensor;
        fixture.maskBits = this.maskBits;
        fixture.categoryBits = this.categoryBits;
        fixture.groupIndex = this.groupIndex;
        fixture.shapes = this.exportShape(x, y);
        return fixture;
    }

    Fixture toPhysics(){

            float x = this.position[0];
            float y = this.position[1];
        return toPhysics(x,y);
    }


}
