package mygame.editor.repository;

import mygame.editor.views.CcNode;

import java.util.List;

public interface NodeRepository {

    CcNode getRootNode();

    CcNode getNodeById(long id);

    void save(CcNode node);

    void delete(CcNode node);

    void deleteAll();

    void update(CcNode node);
}
