package mygame.editor.parser;

import java.util.Arrays;
import java.util.Map;

public class BasicMeta {
    private String ver;
    private String uuid;
    private boolean isGroup;
    private Map<String,String> subMetas;

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

    public Object getSubMetas() {
        return subMetas;
    }

    public void setSubMetas(Object subMetas) {
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
