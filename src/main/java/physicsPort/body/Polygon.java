package physicsPort.body;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polygon {

    static class Point{

        float x;
        float y;
        public Point(float x, float y){

            this.x =x;
            this.y = y;
        }

        public Point(){}

    }

    // area of triangle abc
    public static float  area(Point a,Point b,Point c) {
        return (((b.x - a.x)*(c.y - a.y))-((c.x - a.x)*(b.y - a.y)));
    }

    // point b is left to line ac
    public static boolean left(Point a, Point b, Point c) {
        return area(a, b, c) > 0;
    }

    // point b is left or on the line ac
    public static boolean leftOn(Point a, Point b, Point c) {
        return area(a, b, c) >= 0;
    }

    // point b is right to the line ac
    public static boolean right(Point a, Point b, Point c) {
        return area(a, b, c) < 0;
    }

    // point b is right or on the line ac
    public static boolean rightOn(Point a, Point b, Point c) {
        return area(a, b, c) <= 0;
    }

    // point b is  on the line ac
    public static boolean collinear(Point a,Point  b, Point c) {
        return area(a, b, c) == 0;
    }

    // squared distance between point a-b
    public static float  sqdist(Point a, Point b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return dx * dx + dy * dy;
    }

    static class  Line{
        Point first;
        Point second;
        public Line(Point p1,Point p2){
            this.first  = p1;
            this.second = p2;
        }
    }

    public static boolean eq(float a, float b) {
        return Math.abs(a - b) <= 1e-8;
    }

    // return intersecion of two line
    public static Point lineInt(Line l1, Line l2) {
        Point i = new Point();
        float a1, b1, c1, a2, b2, c2, det;
        a1 = l1.second.y - l1.first.y;
        b1 = l1.first.x - l1.second.x;
        c1 = a1 * l1.first.x + b1 * l1.first.y;
        a2 = l2.second.y - l2.first.y;
        b2 = l2.first.x - l2.second.x;
        c2 = a2 * l2.first.x + b2 * l2.first.y;
        det = a1 * b2 - a2 * b1;
        if (!eq(det, 0)) { // lines are not parallel
            i.x = (b2 * c1 - b1 * c2) / det;
            i.y = (a1 * c2 - a2 * c1) / det;
        }
        return i;
    };



    List<Point> vertices;
    public  Polygon(){
        this.vertices = new ArrayList<>();
    }

    public void draw (GraphicsContext context){
        context.setStroke(Color.WHITE);
        context.moveTo(this.at(0).x, this.at(0).y);
        for (int i = 0; i < this.vertices.size(); i++){
            context.lineTo(this.at(i).x, this.at(i).y);
        }
        context.closePath();
        context.stroke();
    };

// vertex at index - i
    public Point at (int i) {
        int s = this.vertices.size();
        return this.vertices.get(i < 0 ? i % s + s : i % s);		// if i < 0 return last vertex, and if i > size return first vertex
    }

// first vertex
    public Point first () {
        return this.vertices.get(0);
    };

// last vertex
    public  Point last () {
        return this.vertices.get(this.vertices.size()- 1);
    };

// number of vertices
    public int size () {
        return this.vertices.size();
    };

// add vertex
    public void push (Point p) {
        this.vertices.add(p);
    };

// add vertex
    public void addPoint (float x, float y){
        this.push(new Point(x, y));
    };

// reverse order of vertices
    public void reverse () {
        Collections.reverse(vertices);
    };

// returs copy of polygon
    public Polygon copy (int i, int j) {



        Polygon p = new Polygon();
        for (Point vertex : vertices) {
            p.addPoint(vertex.x, vertex.y);
        }
        return p;
    };

// makes polygon clockwise
    public void makeCCW () {
        int br = 0;

        // find bottom right point
        for (int i = 1; i < this.size(); ++i) {
            if (this.vertices.get(i).y < this.vertices.get(br).y || (this.vertices.get(i).y == this.vertices.get(br).y && this.vertices.get(i).x > this.vertices.get(br).x)) {
                br = i;
            }
        }

        // reverse poly if clockwise
        if (!left(this.at(br - 1), this.at(br), this.at(br + 1))) {
            this.reverse();
        }
    };

// returns whether vertex at index if reflex
    public boolean isReflex (int i) {
        return right(this.at(i - 1), this.at(i), this.at(i + 1));
    };

// decompose polygon (if concave) to array of convex polygons
    public List<Polygon> decompose (){
        List<Polygon >polygons = new ArrayList<>();// array to store convex polygons
        decomposePolygon(this, polygons);	// decompose polygon and store them in polygons[]
        return polygons;					// return array of polygons
    };

    // deompose polygon
    void decomposePolygon(Polygon poly, List<Polygon>polygons){
        Point upperInt = new Point();
        Point lowerInt = new Point();
        Point p = new Point();
        Point closestVert = new Point();
        float upperDist, lowerDist, d, closestDist;
        int  upperIndex = 0;
        int lowerIndex = 0;
        int closestIndex = 0;
        Polygon lowerPoly = new Polygon();
        Polygon upperPoly = new Polygon();

        if (poly.size() < 2){
            return;
        }
        for (int i = 0; i < poly.size(); ++i) {
            if (poly.isReflex(i)) {
                upperDist = lowerDist = Float.MAX_VALUE;
                for (int j = 0; j < poly.size(); ++j) {
                    if (left(poly.at(i - 1), poly.at(i), poly.at(j))
                            && rightOn(poly.at(i - 1), poly.at(i), poly.at(j - 1))) { 								// if line intersects with an edge
                        p = lineInt(new Line(poly.at(i - 1), poly.at(i)), new Line(poly.at(j), poly.at(j - 1)));	// intersection(poly.at(i - 1), poly.at(i), poly.at(j), poly.at(j - 1)); // find the point of intersection
                        if (right(poly.at(i + 1), poly.at(i), p)) { 												// make sure it'NODES inside the poly
                            d = sqdist(poly.at(i), p);
                            if (d < lowerDist) { 																	// keep only the closest intersection
                                lowerDist = d;
                                lowerInt = p;
                                lowerIndex = j;
                            }
                        }
                    }
                    if (left(poly.at(i + 1), poly.at(i), poly.at(j + 1))
                            && rightOn(poly.at(i + 1), poly.at(i), poly.at(j))) {
                        p = lineInt(new Line(poly.at(i + 1), poly.at(i)), new Line(poly.at(j), poly.at(j + 1)));	// intersection(at(poly, i + 1), at(poly, i), at(poly, j), at(poly, j + 1));
                        if (left(poly.at(i - 1), poly.at(i), p)) {
                            d = sqdist(poly.at(i), p);
                            if (d < upperDist) {
                                upperDist = d;
                                upperInt = p;
                                upperIndex = j;
                            }
                        }
                    }
                }

                // if there are no vertices to connect to, choose a point in the middle
                if (lowerIndex == (upperIndex + 1) % poly.size()) {
                    // console.log("Case 1: Vertex(" + i + "), lowerIndex(" + lowerIndex + "), upperIndex(" + upperIndex + "), poly.size(" + poly.size());
                    p.x = (lowerInt.x + upperInt.x) / 2;
                    p.y = (lowerInt.y + upperInt.y) / 2;
                    List<Point> tmp;
                    if (i < upperIndex) {
                        tmp = poly.vertices.subList(i, upperIndex + 1);
                        for (int k = 0; k < tmp.size(); k++){
                            lowerPoly.push(tmp.get(k));
                        }
                        lowerPoly.push(p);
                        upperPoly.push(p);
                        if (lowerIndex != 0) {
                            tmp = poly.vertices.subList(lowerIndex, poly.size());
                            for (int k = 0; k < tmp.size(); k++){
                                upperPoly.push(tmp.get(k));
                            }
                        }
                        tmp = poly.vertices.subList(0, i + 1);
                        for (int k = 0; k < tmp.size(); k++){
                            upperPoly.push(tmp.get(k));
                        }
                    } else {
                        if (i != 0) {
                            tmp = poly.vertices.subList(i, poly.size());
                            for (int k = 0; k < tmp.size(); k++){
                                lowerPoly.push(tmp.get(k));
                            }
                        }
                        tmp = poly.vertices.subList(0, upperIndex + 1);
                        for (int k = 0; k < tmp.size(); k++){
                            lowerPoly.push(tmp.get(k));
                        }
                        lowerPoly.push(p);
                        upperPoly.push(p);
                        tmp = poly.vertices.subList(lowerIndex, i + 1);
                        for (int k = 0; k < tmp.size(); k++){
                            upperPoly.push(tmp.get(k));
                        }
                    }
                } else {
                    // connect to the closest point within the triangle
                    // console.log("Case 2: Vertex(" + i + "), closestIndex(" + closestIndex + "), poly.size(" + poly.size());
                    if (lowerIndex > upperIndex) {
                        upperIndex += poly.size();
                    }
                    closestDist = Float.MAX_VALUE;
                    for (int j = lowerIndex; j <= upperIndex; ++j) {
                        if (leftOn(poly.at(i - 1), poly.at(i), poly.at(j))
                                && rightOn(poly.at(i + 1), poly.at(i), poly.at(j))) {
                            d = sqdist(poly.at(i), poly.at(j));
                            if (d < closestDist) {
                                closestDist = d;
                                closestVert = poly.at(j);
                                closestIndex = j % poly.size();
                            }
                        }
                    }
                    List<Point> tmp;
                    if (i < closestIndex) {
                        tmp = poly.vertices.subList(i, closestIndex + 1);
                        for (int k = 0; k < tmp.size(); k++){
                            lowerPoly.push(tmp.get(k));
                        }
                        if (closestIndex != 0) {
                            tmp = poly.vertices.subList(closestIndex, poly.size());
                            for (int k = 0; k < tmp.size(); k++){
                                upperPoly.push(tmp.get(k));
                            }
                        }
                        tmp = poly.vertices.subList(0, i + 1);
                        for (int k = 0; k < tmp.size(); k++){
                            upperPoly.push(tmp.get(k));
                        }
                    } else {
                        if (i != 0) {
                            tmp = poly.vertices.subList(i, poly.size());
                            for (int k = 0; k < tmp.size(); k++){
                                lowerPoly.push(tmp.get(k));
                            }
                        }
                        tmp = poly.vertices.subList(0, closestIndex + 1);
                        for (int  k = 0; k < tmp.size(); k++){
                            lowerPoly.push(tmp.get(k));
                        }
                        tmp = poly.vertices.subList(closestIndex, i + 1);
                        for (int k = 0; k < tmp.size(); k++){
                            upperPoly.push(tmp.get(k));
                        }
                    }
                }

                // solve smallest poly first
                if (lowerPoly.size() < upperPoly.size()) {
                    decomposePolygon(lowerPoly, polygons);
                    decomposePolygon(upperPoly, polygons);
                } else {
                    decomposePolygon(upperPoly, polygons);
                    decomposePolygon(lowerPoly, polygons);
                }
                return;
            }
        }
        polygons.add(poly);
    };
}
