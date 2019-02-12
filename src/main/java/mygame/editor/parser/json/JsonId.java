package mygame.editor.parser.json;

import com.google.gson.annotations.SerializedName;

public class JsonId {
    @SerializedName("__id__")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
