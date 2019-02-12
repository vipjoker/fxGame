package mygame.editor.parser.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonPolygonCollider extends JsonColider {

    @SerializedName("points")
    private List<JsonVec2> points;

    public List<JsonVec2> getPoints() {
        return points;
    }

    public void setPoints(List<JsonVec2> points) {
        this.points = points;
    }
}
