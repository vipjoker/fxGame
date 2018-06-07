package mygame.editor.parser.json;

import java.util.Arrays;
import java.util.Map;

public class BasicMeta<T> {
    private String ver;
    private String uuid;
    private boolean isGroup;
    private Map<String,T> subMetas;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public Map<String,T> getSubMetas() {
        return subMetas;
    }

    public void setSubMetas(Map<String ,T> subMetas) {
        this.subMetas = subMetas;
    }

    @Override
    public String toString() {
        return "BasicMeta{" +
                "ver='" + ver + '\'' +
                ", uuid='" + uuid + '\'' +
                ", isGroup=" + isGroup +
                ", subMetas=" + subMetas +
                '}';
    }
}
