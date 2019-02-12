package mygame.editor.parser.json;

import com.google.gson.annotations.SerializedName;

public class JsonSpriteFrame {
    @SerializedName("__uuid__")
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
