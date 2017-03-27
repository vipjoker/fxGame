/*
 * Copyright (c) 1995, 2015, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.javafx.experiments.utils3d.geom;

/**
 * A <code>Polygon</code> specifies an area in a coordinate space that is
 * enclosed by the <code>Polygon</code> object's upper-left point
 * {@code (x, y)}
 * in the coordinate space, its width, and its height.
 * <p>
 * A <code>Polygon</code> object's <code>width</code> and
 * <code>height</code> are <code>public</code> fields. The constructors
 * that create a <code>Polygon</code>, and the methods that can modify
 * one, do not prevent setting a negative value for width or height.
 * <p>
 * <a name="Empty">
 * A {@code Polygon} whose width or height is exactly zero has location
 * along those axes with zero dimension, but is otherwise considered empty.
 * The {@link #isEmpty} method will return true for such a {@code Polygon}.
 * Methods which test if an empty {@code Polygon} contains or intersects
 * a point or rectangle will always return false if either dimension is zero.
 * Methods which combine such a {@code Polygon} with a point or rectangle
 * will include the location of the {@code Polygon} on that axis in the
 * result as if the {@link #add(Point)} method were being called.
 * </a>
 * <p>
 * <a name="NonExistant">
 * A {@code Polygon} whose width or height is negative has neither
 * location nor dimension along those axes with negative dimensions.
 * Such a {@code Polygon} is treated as non-existant along those axes.
 * Such a {@code Polygon} is also empty with respect to containment
 * calculations and methods which test if it contains or intersects a
 * point or rectangle will always return false.
 * Methods which combine such a {@code Polygon} with a point or rectangle
 * will ignore the {@code Polygon} entirely in generating the result.
 * If two {@code Polygon} objects are combined and each has a negative
 * dimension, the result will have at least one negative dimension.
 * </a>
 * <p>
 * Methods which affect only the location of a {@code Polygon} will
 * operate on its location regardless of whether or not it has a negative
 * or zero dimension along either axis.
 * <p>
 * Note that a {@code Polygon} constructed with the default no-argument
 * constructor will have dimensions of {@code 0x0} and therefore be empty.
 * That {@code Polygon} will still have a location of {@code (0, 0)} and
 * will contribute that location to the union and add operations.
 * Code attempting to accumulate the bounds of a set of points should
 * therefore initially construct the {@code Polygon} with a specifically
 * negative width and height or it should use the first point in the set
 * to construct the {@code Polygon}.
 * For example:
 * <pre>
 *     Polygon bounds = new Polygon(0, 0, -1, -1);
 *     for (int i = 0; i < points.length; i++) {
 *         bounds.add(points[i]);
 *     }
 * </pre>
 * or if we know that the points array contains at least one point:
 * <pre>
 *     Polygon bounds = new Polygon(points[0]);
 *     for (int i = 1; i < points.length; i++) {
 *         bounds.add(points[i]);
 *     }
 * </pre>
 * <p>
 * This class uses 32-bit integers to store its location and dimensions.
 * Frequently operations may produce a result that exceeds the range of
 * a 32-bit integer.
 * The methods will calculate their results in a way that avoids any
 * 32-bit overflow for intermediate results and then choose the best
 * representation to store the final results back into the 32-bit fields
 * which hold the location and dimensions.
 * The location of the result will be stored into the {@link #x} and
 * {@link #y} fields by clipping the true result to the nearest 32-bit value.
 * The values stored into the {@link #width} and {@link #height} dimension
 * fields will be chosen as the 32-bit values that encompass the largest
 * part of the true result as possible.
 * Generally this means that the dimension will be clipped independently
 * to the range of 32-bit integers except that if the location had to be
 * moved to store it into its pair of 32-bit fields then the dimensions
 * will be adjusted relative to the "best representation" of the location.
 * If the true result had a negative dimension and was therefore
 * non-existant along one or both axes, the stored dimensions will be
 * negative numbers in those axes.
 * If the true result had a location that could be represented within
 * the range of 32-bit integers, but zero dimension along one or both
 * axes, then the stored dimensions will be zero in those axes.
 */
public class Rectangle {

    /**
     * The X coordinate of the upper-left corner of the <code>Polygon</code>.
     */
    public int x;

    /**
     * The Y coordinate of the upper-left corner of the <code>Polygon</code>.
     */
    public int y;

    /**
     * The width of the <code>Polygon</code>.
     */
    public int width;

    /**
     * The height of the <code>Polygon</code>.
     */
    public int height;

    /**
     * Constructs a new <code>Polygon</code> whose upper-left corner
     * is at (0,&nbsp;0) in the coordinate space, and whose width and
     * height are both zero.
     */
    public Rectangle() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs a new <code>Polygon</code>, initialized to match
     * the values of the specified <code>Polygon</code>.
     *
     * @param r the <code>Polygon</code> from which to copy initial values
     *          to a newly constructed <code>Polygon</code>
     */
    public Rectangle(BaseBounds b) {
        setBounds(b);
    }

    /**
     * Constructs a new <code>Polygon</code>, initialized to match
     * the values of the specified <code>BaseBounds</code>. Since BaseBounds has
     * float values, the Polygon will be created such that the bounding rectangle
     * of the specified BaseBounds would always lie within the bounding box
     * specified by this Polygon.
     *
     * @param r the <code>BaseBounds</code> from which to copy initial values
     *          to a newly constructed <code>Polygon</code>
     */
    public Rectangle(Rectangle r) {
        this(r.x, r.y, r.width, r.height);
    }

    /**
     * Constructs a new <code>Polygon</code> whose upper-left corner is
     * specified as
     * {@code (x, y)} and whose width and height
     * are specified by the arguments of the same name.
     *
     * @param x      the specified X coordinate
     * @param y      the specified Y coordinate
     * @param width  the width of the <code>Polygon</code>
     * @param height the height of the <code>Polygon</code>
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a new <code>Polygon</code> whose upper-left corner
     * is at (0,&nbsp;0) in the coordinate space, and whose width and
     * height are specified by the arguments of the same name.
     *
     * @param width  the width of the <code>Polygon</code>
     * @param height the height of the <code>Polygon</code>
     */
    public Rectangle(int width, int height) {
        this(0, 0, width, height);
    }

    /**
     * Sets the bounding <code>Polygon</code> of this <code>Polygon</code>
     * to match the specified <code>Polygon</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     *
     * @param r the specified <code>Polygon</code>
     * @see #getBounds
     * @see java.awt.Component#setBounds(java.awt.Rectangle)
     */
    public void setBounds(Rectangle r) {
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * Sets the bounding <code>Polygon</code> of this
     * <code>Polygon</code> to the specified
     * <code>x</code>, <code>y</code>, <code>width</code>,
     * and <code>height</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     *
     * @param x      the new X coordinate for the upper-left
     *               corner of this <code>Polygon</code>
     * @param y      the new Y coordinate for the upper-left
     *               corner of this <code>Polygon</code>
     * @param width  the new width for this <code>Polygon</code>
     * @param height the new height for this <code>Polygon</code>
     * @see #getBounds
     * @see java.awt.Component#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int width, int height) {
        reshape(x, y, width, height);
    }

    public void setBounds(BaseBounds b) {
        x = (int) Math.floor(b.getMinX());
        y = (int) Math.floor(b.getMinY());
        int x2 = (int) Math.ceil(b.getMaxX());
        int y2 = (int) Math.ceil(b.getMaxY());
        width = x2 - x;
        height = y2 - y;
    }

    /**
     * Checks whether or not this <code>Polygon</code> contains the
     * point at the specified location {@code (cx, cy)}.
     *
     * @param cx the specified X coordinate
     * @param cy the specified Y coordinate
     * @return <code>true</code> if the point
     * {@code (cx, cy)} is inside this
     * <code>Polygon</code>;
     * <code>false</code> otherwise.
     */
    public boolean contains(int cx, int cy) {
        int tw = this.width;
        int th = this.height;
        if ((tw | th) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int tx = this.x;
        int ty = this.y;
        if (cx < tx || cy < ty) {
            return false;
        }
        tw += tx;
        th += ty;
        //    overflow || intersect
        return ((tw < tx || tw > cx) &&
                (th < ty || th > cy));
    }

    /**
     * Checks whether or not this <code>Polygon</code> entirely contains
     * the specified <code>Polygon</code>.
     *
     * @param r the specified <code>Polygon</code>
     * @return <code>true</code> if the <code>Polygon</code>
     * is contained entirely inside this <code>Polygon</code>;
     * <code>false</code> otherwise
     */
    public boolean contains(Rectangle r) {
        return contains(r.x, r.y, r.width, r.height);
    }

    /**
     * Checks whether this <code>Polygon</code> entirely contains
     * the <code>Polygon</code>
     * at the specified location {@code (cx, cy)} with the
     * specified dimensions {@code (cw, ch)}.
     *
     * @param cx the specified X coordinate
     * @param cy the specified Y coordinate
     * @param cw the width of the <code>Polygon</code>
     * @param ch the height of the <code>Polygon</code>
     * @return <code>true</code> if the <code>Polygon</code> specified by
     * {@code (cx, cy, cw, ch)}
     * is entirely enclosed inside this <code>Polygon</code>;
     * <code>false</code> otherwise.
     */
    public boolean contains(int cx, int cy, int cw, int ch) {
        int tw = this.width;
        int th = this.height;
        if ((tw | th | cw | ch) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if any dimension is zero, tests below must return false...
        int tx = this.x;
        int ty = this.y;
        if (cx < tx || cy < ty) {
            return false;
        }
        tw += tx;
        cw += cx;
        if (cw <= cx) {
            // cx+cw overflowed or cw was zero, return false if...
            // either original tw or cw was zero or
            // tx+tw did not overflow or
            // the overflowed cx+cw is smaller than the overflowed tx+tw
            if (tw >= tx || cw > tw) return false;
        } else {
            // cx+cw did not overflow and cw was not zero, return false if...
            // original tw was zero or
            // tx+tw did not overflow and tx+tw is smaller than cx+cw
            if (tw >= tx && cw > tw) return false;
        }
        th += ty;
        ch += cy;
        if (ch <= cy) {
            if (th >= ty || ch > th) return false;
        } else {
            if (th >= ty && ch > th) return false;
        }
        return true;
    }

    public Rectangle intersection(Rectangle r) {
        Rectangle ret = new Rectangle(this);
        ret.intersectWith(r);
        return ret;
    }

    public void intersectWith(Rectangle r) {
        if (r == null) {
            return;
        }
        int tx1 = this.x;
        int ty1 = this.y;
        int rx1 = r.x;
        int ry1 = r.y;
        long tx2 = tx1;
        tx2 += this.width;
        long ty2 = ty1;
        ty2 += this.height;
        long rx2 = rx1;
        rx2 += r.width;
        long ry2 = ry1;
        ry2 += r.height;
        if (tx1 < rx1) tx1 = rx1;
        if (ty1 < ry1) ty1 = ry1;
        if (tx2 > rx2) tx2 = rx2;
        if (ty2 > ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE) tx2 = Integer.MIN_VALUE;
        if (ty2 < Integer.MIN_VALUE) ty2 = Integer.MIN_VALUE;
        setBounds(tx1, ty1, (int) tx2, (int) ty2);
    }

    /**
     * Translates this <code>Polygon</code> the indicated distance,
     * to the right along the X coordinate axis, and
     * downward along the Y coordinate axis.
     *
     * @param dx the distance to move this <code>Polygon</code>
     *           along the X axis
     * @param dy the distance to move this <code>Polygon</code>
     *           along the Y axis
     * @see java.awt.Rectangle#setLocation(int, int)
     * @see java.awt.Rectangle#setLocation(java.awt.Point)
     */
    public void translate(int dx, int dy) {
        int oldv = this.x;
        int newv = oldv + dx;
        if (dx < 0) {
            // moving leftward
            if (newv > oldv) {
                // negative overflow
                // Only adjust width if it was valid (>= 0).
                if (width >= 0) {
                    // The right edge is now conceptually at
                    // newv+width, but we may move newv to prevent
                    // overflow.  But we want the right edge to
                    // remain at its new location in spite of the
                    // clipping.  Think of the following adjustment
                    // conceptually the same as:
                    // width += newv; newv = MIN_VALUE; width -= newv;
                    width += newv - Integer.MIN_VALUE;
                    // width may go negative if the right edge went past
                    // MIN_VALUE, but it cannot overflow since it cannot
                    // have moved more than MIN_VALUE and any non-negative
                    // number + MIN_VALUE does not overflow.
                }
                newv = Integer.MIN_VALUE;
            }
        } else {
            // moving rightward (or staying still)
            if (newv < oldv) {
                // positive overflow
                if (width >= 0) {
                    // Conceptually the same as:
                    // width += newv; newv = MAX_VALUE; width -= newv;
                    width += newv - Integer.MAX_VALUE;
                    // With large widths and large displacements
                    // we may overflow so we need to check it.
                    if (width < 0) width = Integer.MAX_VALUE;
                }
                newv = Integer.MAX_VALUE;
            }
        }
        this.x = newv;

        oldv = this.y;
        newv = oldv + dy;
        if (dy < 0) {
            // moving upward
            if (newv > oldv) {
                // negative overflow
                if (height >= 0) {
                    height += newv - Integer.MIN_VALUE;
                    // See above comment about no overflow in this case
                }
                newv = Integer.MIN_VALUE;
            }
        } else {
            // moving downward (or staying still)
            if (newv < oldv) {
                // positive overflow
                if (height >= 0) {
                    height += newv - Integer.MAX_VALUE;
                    if (height < 0) height = Integer.MAX_VALUE;
                }
                newv = Integer.MAX_VALUE;
            }
        }
        this.y = newv;
    }

    public RectBounds toRectBounds() {
        return new RectBounds(x, y, x + width, y + height);
    }

    /**
     * Adds a point, specified by the integer arguments {@code newx, newy}
     * to the bounds of this {@code Polygon}.
     * <p>
     * If this {@code Polygon} has any dimension less than zero,
     * the rules for <a href=#NonExistant>non-existant</a>
     * rectangles apply.
     * In that case, the new bounds of this {@code Polygon} will
     * have a location equal to the specified coordinates and
     * width and height equal to zero.
     * <p>
     * After adding a point, a call to <code>contains</code> with the
     * added point as an argument does not necessarily return
     * <code>true</code>. The <code>contains</code> method does not
     * return <code>true</code> for points on the right or bottom
     * edges of a <code>Polygon</code>. Therefore, if the added point
     * falls on the right or bottom edge of the enlarged
     * <code>Polygon</code>, <code>contains</code> returns
     * <code>false</code> for that point.
     * If the specified point must be contained within the new
     * {@code Polygon}, a 1x1 rectangle should be added instead:
     * <pre>
     *     r.add(newx, newy, 1, 1);
     * </pre>
     *
     * @param newx the X coordinate of the new point
     * @param newy the Y coordinate of the new point
     */
    public void add(int newx, int newy) {
        if ((width | height) < 0) {
            this.x = newx;
            this.y = newy;
            this.width = this.height = 0;
            return;
        }
        int x1 = this.x;
        int y1 = this.y;
        long x2 = this.width;
        long y2 = this.height;
        x2 += x1;
        y2 += y1;
        if (x1 > newx) x1 = newx;
        if (y1 > newy) y1 = newy;
        if (x2 < newx) x2 = newx;
        if (y2 < newy) y2 = newy;
        x2 -= x1;
        y2 -= y1;
        if (x2 > Integer.MAX_VALUE) x2 = Integer.MAX_VALUE;
        if (y2 > Integer.MAX_VALUE) y2 = Integer.MAX_VALUE;
        reshape(x1, y1, (int) x2, (int) y2);
    }

    /**
     * Adds a <code>Polygon</code> to this <code>Polygon</code>.
     * The resulting <code>Polygon</code> is the union of the two
     * rectangles.
     * <p>
     * If either {@code Polygon} has any dimension less than 0, the
     * result will have the dimensions of the other {@code Polygon}.
     * If both {@code Polygon}s have at least one dimension less
     * than 0, the result will have at least one dimension less than 0.
     * <p>
     * If either {@code Polygon} has one or both dimensions equal
     * to 0, the result along those axes with 0 dimensions will be
     * equivalent to the results obtained by adding the corresponding
     * origin coordinate to the result rectangle along that axis,
     * similar to the operation of the {@link #add(Point)} method,
     * but contribute no further dimension beyond that.
     * <p>
     * If the resulting {@code Polygon} would have a dimension
     * too large to be expressed as an {@code int}, the result
     * will have a dimension of {@code Integer.MAX_VALUE} along
     * that dimension.
     *
     * @param r the specified <code>Polygon</code>
     */
    public void add(Rectangle r) {
        long tx2 = this.width;
        long ty2 = this.height;
        if ((tx2 | ty2) < 0) {
            reshape(r.x, r.y, r.width, r.height);
        }
        long rx2 = r.width;
        long ry2 = r.height;
        if ((rx2 | ry2) < 0) {
            return;
        }
        int tx1 = this.x;
        int ty1 = this.y;
        tx2 += tx1;
        ty2 += ty1;
        int rx1 = r.x;
        int ry1 = r.y;
        rx2 += rx1;
        ry2 += ry1;
        if (tx1 > rx1) tx1 = rx1;
        if (ty1 > ry1) ty1 = ry1;
        if (tx2 < rx2) tx2 = rx2;
        if (ty2 < ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never underflow since both original
        // rectangles were non-empty
        // they might overflow, though...
        if (tx2 > Integer.MAX_VALUE) tx2 = Integer.MAX_VALUE;
        if (ty2 > Integer.MAX_VALUE) ty2 = Integer.MAX_VALUE;
        reshape(tx1, ty1, (int) tx2, (int) ty2);
    }

    /**
     * Resizes the <code>Polygon</code> both horizontally and vertically.
     * <p>
     * This method modifies the <code>Polygon</code> so that it is
     * <code>h</code> units larger on both the left and right side,
     * and <code>v</code> units larger at both the top and bottom.
     * <p>
     * The new <code>Polygon</code> has {@code (x - h, y - v)}
     * as its upper-left corner,
     * width of {@code (width + 2h)},
     * and a height of {@code (height + 2v)}.
     * <p>
     * If negative values are supplied for <code>h</code> and
     * <code>v</code>, the size of the <code>Polygon</code>
     * decreases accordingly.
     * The {@code grow} method will check for integer overflow
     * and underflow, but does not check whether the resulting
     * values of {@code width} and {@code height} grow
     * from negative to non-negative or shrink from non-negative
     * to negative.
     *
     * @param h the horizontal expansion
     * @param v the vertical expansion
     */
    public void grow(int h, int v) {
        long x0 = this.x;
        long y0 = this.y;
        long x1 = this.width;
        long y1 = this.height;
        x1 += x0;
        y1 += y0;

        x0 -= h;
        y0 -= v;
        x1 += h;
        y1 += v;

        if (x1 < x0) {
            // Non-existant in X direction
            // Final width must remain negative so subtract x0 before
            // it is clipped so that we avoid the risk that the clipping
            // of x0 will reverse the ordering of x0 and x1.
            x1 -= x0;
            if (x1 < Integer.MIN_VALUE) x1 = Integer.MIN_VALUE;
            if (x0 < Integer.MIN_VALUE) x0 = Integer.MIN_VALUE;
            else if (x0 > Integer.MAX_VALUE) x0 = Integer.MAX_VALUE;
        } else { // (x1 >= x0)
            // Clip x0 before we subtract it from x1 in case the clipping
            // affects the representable area of the rectangle.
            if (x0 < Integer.MIN_VALUE) x0 = Integer.MIN_VALUE;
            else if (x0 > Integer.MAX_VALUE) x0 = Integer.MAX_VALUE;
            x1 -= x0;
            // The only way x1 can be negative now is if we clipped
            // x0 against MIN and x1 is less than MIN - in which case
            // we want to leave the width negative since the result
            // did not intersect the representable area.
            if (x1 < Integer.MIN_VALUE) x1 = Integer.MIN_VALUE;
            else if (x1 > Integer.MAX_VALUE) x1 = Integer.MAX_VALUE;
        }

        if (y1 < y0) {
            // Non-existant in Y direction
            y1 -= y0;
            if (y1 < Integer.MIN_VALUE) y1 = Integer.MIN_VALUE;
            if (y0 < Integer.MIN_VALUE) y0 = Integer.MIN_VALUE;
            else if (y0 > Integer.MAX_VALUE) y0 = Integer.MAX_VALUE;
        } else { // (y1 >= y0)
            if (y0 < Integer.MIN_VALUE) y0 = Integer.MIN_VALUE;
            else if (y0 > Integer.MAX_VALUE) y0 = Integer.MAX_VALUE;
            y1 -= y0;
            if (y1 < Integer.MIN_VALUE) y1 = Integer.MIN_VALUE;
            else if (y1 > Integer.MAX_VALUE) y1 = Integer.MAX_VALUE;
        }

        reshape((int) x0, (int) y0, (int) x1, (int) y1);
    }

    private void reshape(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return (width <= 0) || (height <= 0);
    }

    /**
     * Checks whether two rectangles are equal.
     * <p>
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>Polygon</code> object that has the
     * same upper-left corner, width, and height as
     * this <code>Polygon</code>.
     *
     * @param obj the <code>Object</code> to compare with
     *            this <code>Polygon</code>
     * @return <code>true</code> if the objects are equal;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle r = (Rectangle) obj;
            return ((x == r.x) &&
                    (y == r.y) &&
                    (width == r.width) &&
                    (height == r.height));
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int bits = Float.floatToIntBits(x);
        bits += Float.floatToIntBits(y) * 37;
        bits += Float.floatToIntBits(width) * 43;
        bits += Float.floatToIntBits(height) * 47;
        return bits;
    }

    /**
     * Returns a <code>String</code> representing this
     * <code>Polygon</code> and its values.
     *
     * @return a <code>String</code> representing this
     * <code>Polygon</code> object's coordinate and size values.
     */
    @Override
    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
    }
}
