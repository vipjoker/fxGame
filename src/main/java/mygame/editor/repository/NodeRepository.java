package mygame.editor.repository;

import mygame.editor.views.CcNode;

import java.util.List;

public interface NodeRepository {

    public List<CcNode> getAllNodes();
    public void save(CcNode node);
    public void delete(CcNode node);
    public void update(CcNode node);
}
