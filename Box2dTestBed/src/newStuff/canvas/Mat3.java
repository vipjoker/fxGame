package newStuff.canvas;

public final class Mat3 {
        // Row-major 3x3 for 2D affine transforms
        public final double m00, m01, m02;
        public final double m10, m11, m12;
        public final double m20, m21, m22;

        public Mat3(double m00, double m01, double m02,
                    double m10, double m11, double m12,
                    double m20, double m21, double m22) {
            this.m00 = m00; this.m01 = m01; this.m02 = m02;
            this.m10 = m10; this.m11 = m11; this.m12 = m12;
            this.m20 = m20; this.m21 = m21; this.m22 = m22;
        }

        public static  Mat3 identity() {
            return new  Mat3(1,0,0, 0,1,0, 0,0,1);
        }

        public static  Mat3 translation(double tx, double ty) {
            return new  Mat3(1,0,tx, 0,1,ty, 0,0,1);
        }

        public static  Mat3 rotation(double radians) {
            double c = Math.cos(radians), s = Math.sin(radians);
            return new  Mat3(c,-s,0, s,c,0, 0,0,1);
        }

        public  Mat3 mul( Mat3 b) {
            return new  Mat3(
                    m00*b.m00 + m01*b.m10 + m02*b.m20,
                    m00*b.m01 + m01*b.m11 + m02*b.m21,
                    m00*b.m02 + m01*b.m12 + m02*b.m22,

                    m10*b.m00 + m11*b.m10 + m12*b.m20,
                    m10*b.m01 + m11*b.m11 + m12*b.m21,
                    m10*b.m02 + m11*b.m12 + m12*b.m22,

                    m20*b.m00 + m21*b.m10 + m22*b.m20,
                    m20*b.m01 + m21*b.m11 + m22*b.m21,
                    m20*b.m02 + m21*b.m12 + m22*b.m22
            );
        }

        public Vec2 transform(Vec2 p) {
            double x = m00*p.x + m01*p.y + m02;
            double y = m10*p.x + m11*p.y + m12;
            return new Vec2(x, y);
        }

        public  Mat3 inverse() {
            double det = m00*m11 - m01*m10;
            if (Math.abs(det) < 1e-12) throw new IllegalStateException("Non-invertible matrix");
            double inv = 1.0 / det;
            double r00 =  m11 * inv, r01 = -m01 * inv;
            double r10 = -m10 * inv, r11 =  m00 * inv;
            double t0 = -(r00*m02 + r01*m12);
            double t1 = -(r10*m02 + r11*m12);
            return new  Mat3(r00, r01, t0, r10, r11, t1, 0, 0, 1);
        }
    }