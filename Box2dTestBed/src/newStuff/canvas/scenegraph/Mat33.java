package newStuff.canvas.scenegraph;

// ======================================================================
// Mat33 — 3x3 affine matrix (row-major, last row implied (0,0,1)-style
// but stored fully to support a general inverse).
// ======================================================================
final class Mat33 {
    float m00, m01, m02;
    float m10, m11, m12;
    float m20, m21, m22;

    Mat33() {
        m00 = m11 = m22 = 1f;
    }

    Mat33(float m00, float m01, float m02,
          float m10, float m11, float m12,
          float m20, float m21, float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }

    static Mat33 identity() {
        return new Mat33();
    }

    static Mat33 translation(float tx, float ty) {
        return new Mat33(1f, 0f, tx,
                0f, 1f, ty,
                0f, 0f, 1f);
    }

    static Mat33 scale(float sx, float sy) {
        return new Mat33(sx, 0f, 0f,
                0f, sy, 0f,
                0f, 0f, 1f);
    }

    static Mat33 rotation(float angle) {
        float c = (float) Math.cos(angle), s = (float) Math.sin(angle);
        return new Mat33(c, -s, 0f,
                s, c, 0f,
                0f, 0f, 1f);
    }

    Mat33 mul(Mat33 o) {
        return new Mat33(
                m00 * o.m00 + m01 * o.m10 + m02 * o.m20,
                m00 * o.m01 + m01 * o.m11 + m02 * o.m21,
                m00 * o.m02 + m01 * o.m12 + m02 * o.m22,

                m10 * o.m00 + m11 * o.m10 + m12 * o.m20,
                m10 * o.m01 + m11 * o.m11 + m12 * o.m21,
                m10 * o.m02 + m11 * o.m12 + m12 * o.m22,

                m20 * o.m00 + m21 * o.m10 + m22 * o.m20,
                m20 * o.m01 + m21 * o.m11 + m22 * o.m21,
                m20 * o.m02 + m21 * o.m12 + m22 * o.m22);
    }

    Vec2 transformPoint(Vec2 p) {
        return new Vec2(m00 * p.x + m01 * p.y + m02,
                m10 * p.x + m11 * p.y + m12);
    }

    Vec2 transformVector(Vec2 v) {
        return new Vec2(m00 * v.x + m01 * v.y,
                m10 * v.x + m11 * v.y);
    }

    Mat33 invert() {
        float c00 = (m11 * m22 - m12 * m21);
        float c01 = -(m10 * m22 - m12 * m20);
        float c02 = (m10 * m21 - m11 * m20);
        float det = m00 * c00 + m01 * c01 + m02 * c02;
        if (det == 0f) throw new IllegalStateException("Matrix not invertible");
        float invDet = 1f / det;
        return new Mat33(
                c00 * invDet,
                (m02 * m21 - m01 * m22) * invDet,
                (m01 * m12 - m02 * m11) * invDet,

                c01 * invDet,
                (m00 * m22 - m02 * m20) * invDet,
                (m02 * m10 - m00 * m12) * invDet,

                c02 * invDet,
                (m01 * m20 - m00 * m21) * invDet,
                (m00 * m11 - m01 * m10) * invDet);
    }

    Vec2 getTranslation() {
        return new Vec2(m02, m12);
    }

    float getRotation() {
        return (float) Math.atan2(m10, m00);
    }

    Vec2 getScale() {
        return new Vec2((float) Math.sqrt(m00 * m00 + m10 * m10),
                (float) Math.sqrt(m01 * m01 + m11 * m11));
    }
}
