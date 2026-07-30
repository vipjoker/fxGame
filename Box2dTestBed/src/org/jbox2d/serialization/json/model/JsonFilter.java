package org.jbox2d.serialization.json.model;

public class JsonFilter {
    private int categoryBits ;
    private int maskBits ;
    private int groupIndex ;

    public int getCategoryBits() {
        return categoryBits;
    }

    public void setCategoryBits(int categoryBits) {
        this.categoryBits = categoryBits;
    }

    public int getMaskBits() {
        return maskBits;
    }

    public void setMaskBits(int maskBits) {
        this.maskBits = maskBits;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }
}
