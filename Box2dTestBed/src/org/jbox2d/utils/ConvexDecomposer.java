package org.jbox2d.utils;

import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Convex decomposition for Box2D (jbox2d, libGDX Box2D bindings, etc).
 *
 * Box2D polygons must be convex (and ideally <= 8 vertices for good
 * performance/stability). This splits an arbitrary simple polygon
 * (no self-intersections) into a set of convex polygons using
 * Bayazit's recursive algorithm.
 *
 * Usage:
 *   List<List<Vec2>> pieces = ConvexDecomposer.decompose(myConcavePoints);
 *   for (List<Vec2> piece : pieces) {
 *       PolygonShape shape = new PolygonShape();
 *       Vec2[] verts = piece.toArray(new Vec2[0]);
 *       shape.set(verts, verts.length);
 *       FixtureDef fd = new FixtureDef();
 *       fd.shape = shape;
 *       body.createFixture(fd);
 *   }
 *
 * Points must be a *simple* polygon in either winding order; the
 * decomposer normalizes to CCW internally.
 */
public final class ConvexDecomposer {

    /** Basic 2D vector — swap this for your engine's Vec2 if you prefer. */
//    public static final class Vec2 {
//        public final double x, y;
//
//        public Vec2(double x, double y) {
//            this.x = x;
//            this.y = y;
//        }
//
//        public Vec2 sub(Vec2 o) { return new Vec2(x - o.x, y - o.y); }
//        public Vec2 add(Vec2 o) { return new Vec2(x + o.x, y + o.y); }
//        public Vec2 scale(double s) { return new Vec2(x * s, y * s); }
//
//        public double cross(Vec2 o) { return x * o.y - y * o.x; }
//        public double dot(Vec2 o) { return x * o.x + y * o.y; }
//        public double length() { return Math.sqrt(x * x + y * y); }
//
//        public Vec2 normalized() {
//            double l = length();
//            return l < 1e-12 ? new Vec2(0, 0) : new Vec2(x / l, y / l);
//        }
//
//        @Override
//        public String toString() { return "(" + x + ", " + y + ")"; }
//    }

    /** Max vertices per output polygon (Box2D's b2_maxPolygonVertices default is 8). */
    public static final int MAX_VERTICES_PER_POLYGON = 8;

    private static final double EPS = 1e-9;

    private ConvexDecomposer() {}

    /** Entry point. Returns convex polygons (each CCW-wound) whose union equals the input. */
    public static List<List<Vec2>> decompose(List<Vec2> polygon) {
        List<Vec2> poly = cleanup(ensureCCW(polygon));
        List<List<Vec2>> result = new ArrayList<>();
        if (poly.size() < 3) return result;

        if (isConvex(poly) && poly.size() <= MAX_VERTICES_PER_POLYGON) {
            result.add(poly);
            return result;
        }

        List<List<Vec2>> raw = new ArrayList<>();
        bayazit(poly, raw);

        for (List<Vec2> p : raw) {
            result.addAll(capVertexCount(p, MAX_VERTICES_PER_POLYGON));
        }
        return result;
    }

    // ---- core recursive split -------------------------------------------

    private static void bayazit(List<Vec2> polygon, List<List<Vec2>> out) {
        if (polygon.size() < 3) return;

        int reflexIndex = findReflexVertex(polygon);
        if (reflexIndex == -1) {
            out.add(polygon);
            return;
        }

        int n = polygon.size();
        Vec2 vi = polygon.get(reflexIndex);
        int bestIndex = -1;
        double bestDist = Double.POSITIVE_INFINITY;

        for (int i = 0; i < n; i++) {
            if (i == reflexIndex) continue;
            if (!canSee(polygon, reflexIndex, i)) continue;

            Vec2 vj = polygon.get(i);
            double d = vj.sub(vi).length();
            if (d < bestDist) {
                bestDist = d;
                bestIndex = i;
            }
        }

        if (bestIndex == -1) {
            earClipFallback(polygon, out);
            return;
        }

        int low = Math.min(reflexIndex, bestIndex);
        int high = Math.max(reflexIndex, bestIndex);

        List<Vec2> poly1 = new ArrayList<>(polygon.subList(low, high + 1));
        List<Vec2> poly2 = new ArrayList<>();
        poly2.addAll(polygon.subList(high, n));
        poly2.addAll(polygon.subList(0, low + 1));

        bayazit(cleanup(poly1), out);
        bayazit(cleanup(poly2), out);
    }

    /** Whether vertex `from` can "see" vertex `to` inside the polygon. */
    private static boolean canSee(List<Vec2> polygon, int from, int to) {
        int n = polygon.size();
        Vec2 a = polygon.get(from);
        Vec2 b = polygon.get(to);

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            if (i == from || i == to || j == from || j == to) continue;
            if (segmentsIntersect(a, b, polygon.get(i), polygon.get(j))) return false;
        }

        Vec2 aPrev = polygon.get((from - 1 + n) % n);
        Vec2 aNext = polygon.get((from + 1) % n);
        if (!diagonalIsInternal(aPrev, a, aNext, b)) return false;

        Vec2 bPrev = polygon.get((to - 1 + n) % n);
        Vec2 bNext = polygon.get((to + 1) % n);
        if (!diagonalIsInternal(bPrev, b, bNext, a)) return false;

        return true;
    }

    private static boolean diagonalIsInternal(Vec2 prev, Vec2 v, Vec2 next, Vec2 target) {
        Vec2 toTarget = target.sub(v).clone();
        toTarget.normalize();
        Vec2 toPrev = prev.sub(v).clone();
        toPrev.normalize();
        Vec2 toNext = next.sub(v).clone();
        toNext.normalize();

        if (isReflex(prev, v, next)) {
            return Vec2.cross(toTarget,toNext) >= -EPS || Vec2.cross(toTarget,toPrev) <= EPS;
        } else {
            return Vec2.cross(toTarget,toNext) >= -EPS && Vec2.cross(toTarget,toPrev) <= EPS;
        }
    }

    private static boolean segmentsIntersect(Vec2 p1, Vec2 p2, Vec2 p3, Vec2 p4) {
        double d1 = direction(p3, p4, p1);
        double d2 = direction(p3, p4, p2);
        double d3 = direction(p1, p2, p3);
        double d4 = direction(p1, p2, p4);

        return ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0))
                && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0));
    }

    private static double direction(Vec2 a, Vec2 b, Vec2 c) {
        return Vec2.cross(c.sub(a),b.sub(a));
    }

    // ---- geometry helpers --------------------------------------------------

    private static int findReflexVertex(List<Vec2> p) {
        int n = p.size();
        for (int i = 0; i < n; i++) {
            Vec2 prev = p.get((i - 1 + n) % n);
            Vec2 cur = p.get(i);
            Vec2 next = p.get((i + 1) % n);
            if (isReflex(prev, cur, next)) return i;
        }
        return -1;
    }

    private static boolean isReflex(Vec2 prev, Vec2 cur, Vec2 next) {
        return Vec2.cross(cur.sub(prev),next.sub(cur)) < -EPS;
    }

    private static boolean isConvex(List<Vec2> p) {
        return findReflexVertex(p) == -1;
    }

    /** Ensures counter-clockwise winding (Box2D convention). */
    private static List<Vec2> ensureCCW(List<Vec2> p) {
        double area = 0;
        int n = p.size();
        for (int i = 0; i < n; i++) {
            Vec2 a = p.get(i);
            Vec2 b = p.get((i + 1) % n);
            area += Vec2.cross(a,b);
        }
        if (area < 0) {
            List<Vec2> reversed = new ArrayList<>(p);
            Collections.reverse(reversed);
            return reversed;
        }
        return p;
    }

    /** Removes duplicate/collinear points that can break the reflex test. */
    private static List<Vec2> cleanup(List<Vec2> p) {
        List<Vec2> out = new ArrayList<>();
        int n = p.size();
        for (int i = 0; i < n; i++) {
            Vec2 cur = p.get(i);
            if (!out.isEmpty() && cur.sub(out.get(out.size() - 1)).length() < 1e-7) continue;
            out.add(cur);
        }
        if (out.size() > 1 && out.get(0).sub(out.get(out.size() - 1)).length() < 1e-7) {
            out.remove(out.size() - 1);
        }

        List<Vec2> result = new ArrayList<>();
        int m = out.size();
        for (int i = 0; i < m; i++) {
            Vec2 prev = out.get((i - 1 + m) % m);
            Vec2 cur = out.get(i);
            Vec2 next = out.get((i + 1) % m);
            double cross = Vec2.cross(cur.sub(prev),next.sub(cur));
            if (Math.abs(cross) > EPS) result.add(cur);
        }
        return result.size() >= 3 ? result : out;
    }

    /** Fan-triangulation fallback for near-degenerate edge cases. */
    private static void earClipFallback(List<Vec2> polygon, List<List<Vec2>> out) {
        if (polygon.size() < 3) return;
        Vec2 anchor = polygon.get(0);
        for (int i = 1; i < polygon.size() - 1; i++) {
            List<Vec2> tri = new ArrayList<>();
            tri.add(anchor);
            tri.add(polygon.get(i));
            tri.add(polygon.get(i + 1));
            out.add(tri);
        }
    }

    /** Fan-splits any polygon with more than maxVerts vertices into smaller convex pieces. */
    private static List<List<Vec2>> capVertexCount(List<Vec2> p, int maxVerts) {
        List<List<Vec2>> out = new ArrayList<>();
        if (p.size() <= maxVerts) {
            out.add(p);
            return out;
        }
        int start = 1;
        while (start < p.size() - 1) {
            int end = Math.min(start + maxVerts - 2, p.size() - 1);
            List<Vec2> chunk = new ArrayList<>();
            chunk.add(p.get(0));
            chunk.addAll(p.subList(start, end + 1));
            out.add(chunk);
            start = end;
        }
        return out;
    }
}