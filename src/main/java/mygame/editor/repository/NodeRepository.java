package mygame.editor.repository;

import mygame.editor.views.CcNode;

import java.util.List;

public interface NodeRepository {

    List<CcNode> getAllNodes();

    CcNode getNodeById(long id);

    void save(CcNode node);

    void delete(CcNode node);

    void update(CcNode node);
}
