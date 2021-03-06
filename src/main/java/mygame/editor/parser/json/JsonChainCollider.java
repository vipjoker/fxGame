package mygame.editor.parser.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonChainCollider extends JsonColider {

    @SerializedName("loop")
    private Boolean loop;

    @SerializedName("points")
    private List<JsonVec2> points;

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

    public List<JsonVec2> getPoints() {
        return points;
    }

    public void setPoints(List<JsonVec2> points) {
        this.points = points;
    }
}
