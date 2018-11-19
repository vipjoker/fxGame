package mygame.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import java.util.ArrayList;
import java.util.List;

public class TreeHolder {
    private String value;
    private List<TreeHolder> items = new ArrayList<>();
    private TreeHolder parent;
    public TreeHolder(String value) {
        this.value = value;
    }

    public void addChild(TreeHolder holder) {
        holder.setParent(this);
        items.add(holder);
    }

    public void setParent(TreeHolder treeHolder){
        this.parent = treeHolder;
    }

    public boolean hasParrent() {
        return parent != null;
    }

    public List<TreeHolder> getChildren() {
        return items;
    }

    public void removeFromParent(){
        if(parent != null){
            parent.getChildren().remove(this);
        }
    }

    public String getValue() {
        return value;
    }
}
