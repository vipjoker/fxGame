package mygame.editor.parser.json;

import com.google.gson.annotations.SerializedName;

public class JsonCircleColider extends JsonColider {

    @SerializedName("_offset")
    private JsonVec2 offset;

    @SerializedName("_radius")
    private Double radius;

    public JsonVec2 getOffset() {
        return offset;
    }

    public void setOffset(JsonVec2 offset) {
        this.offset = offset;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
