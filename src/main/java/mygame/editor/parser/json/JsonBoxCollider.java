package mygame.editor.parser.json;

import com.google.gson.annotations.SerializedName;

public class JsonBoxCollider extends JsonColider{

    @SerializedName("_offset")
    private JsonVec2 offset;

    @SerializedName("_size")
    private JsonSize size;

    public JsonVec2 getOffset() {
        return offset;
    }

    public void setOffset(JsonVec2 offset) {
        this.offset = offset;
    }

    public JsonSize getSize() {
        return size;
    }

    public void setSize(JsonSize size) {
        this.size = size;
    }
}
