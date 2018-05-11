package mygame.editor.repository;

import mygame.editor.data.DbConnection;
import mygame.editor.data.NodeDao;
import mygame.editor.mapper.EntityNodeMapper;
import mygame.editor.views.CcNode;

import java.sql.Connection;
import java.util.List;

public class SqlNodeRepository implements NodeRepository{
    NodeDao nodeDao;
    public SqlNodeRepository(){

        Connection con = DbConnection.getDbConnection();

        try {
            nodeDao = new NodeDao(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CcNode getNodeById(long id) {
        return null;
    }

    @Override
    public List<CcNode> getAllNodes() {
        return null;
    }

    @Override
    public void save(CcNode node) {
        try {
            nodeDao.insert(EntityNodeMapper.map(node));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(CcNode node) {

    }

    @Override
    public void update(CcNode node) {

    }

    public static void main(String[] args) {
        SqlNodeRepository repository = new SqlNodeRepository();


        CcNode node = new CcNode() ;
        node.x = 32;
        node.y = 21;
        node.setActive(false);

        repository.save(node);
    }
}
