
package mygame.editor.parser.json;

import com.google.gson.annotations.SerializedName;

public abstract class Typeable {
    @SerializedName("__type__")
    protected String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

