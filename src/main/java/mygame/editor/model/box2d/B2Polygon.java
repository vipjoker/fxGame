package mygame.editor.model.box2d;

import java.util.List;

/**
 * Created by oleh on 17.04.18.
 */
public class B2Polygon {
    private B2Points vertices;

    public B2Points getVertices() {
        return vertices;
    }

    public void setVertices(B2Points vertices) {
        this.vertices = vertices;
    }

    public static class B2Points{
        private List<Double> x;
        private List<Double> y;

        public List<Double> getX() {
            return x;
        }

        public void setX(List<Double> x) {
            this.x = x;
        }

        public List<Double> getY() {
            return y;
        }

        public void setY(List<Double> y) {
            this.y = y;
        }
    }
}


