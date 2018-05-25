package mygame.editor.repository;

import mygame.editor.component.SpriteComponent;
import mygame.editor.data.*;
import mygame.editor.data.entities.EntityBody;
import mygame.editor.data.entities.EntityNode;
import mygame.editor.data.entities.EntitySprite;
import mygame.editor.mapper.CcNodeUtil;
import mygame.editor.mapper.EntityNodeMapper;
import mygame.editor.views.CcNode;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SqlNodeRepository implements NodeRepository {
    NodeDao nodeDao;
    BodyDefDao bodyDefDao;
    FixtureDefDao fixtureDefDao;
    SpriteDao spriteDao;

    public SqlNodeRepository() {

        Connection con = DbConnection.getDbConnection();

        try {
            nodeDao = new NodeDao(con);
            bodyDefDao = new BodyDefDao(con);
            fixtureDefDao = new FixtureDefDao(con);
            spriteDao = new SpriteDao(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CcNode getNodeById(long id) {
        return null;
    }

    @Override
    public CcNode getRootNode() {
        CcNode root = null;

        try {
            List<EntityNode> all = nodeDao.getAll();

            root = CcNodeUtil.unflat(all);
            addComponents(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    private void addComponents(CcNode root) {
        try {

            EntitySprite spriteEntity = spriteDao.getByParentId(root.id);
            if(spriteEntity != null){
                SpriteComponent component = new SpriteComponent(root);
                component.setId(spriteEntity.getId());
                component.setName(spriteEntity.getName());
                component.setUrl(spriteEntity.getUrl());

            }
            EntityBody bodyEntity = bodyDefDao.getByParentId(root.id);
            if(bodyEntity!= null) {
                fixtureDefDao.getAllByParentId(bodyEntity.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        for (CcNode ccNode : root.getChildren()) {
            addComponents(ccNode);
        }
    }

    @Override
    public void save(CcNode node) {

        CcNodeUtil.flat(node,this::count).forEach(n -> {

            try {
                nodeDao.insert(n);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void delete(CcNode node) {

    }

    @Override
    public int count(){
        return nodeDao.count();
    }


    @Override
    public void deleteAll() {
        try {
            nodeDao.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(CcNode node) {

    }

    public static void main(String[] args) {
        SqlNodeRepository repository = new SqlNodeRepository();


        CcNode grandParent = new CcNode();
        grandParent.name= "grandparent";
        grandParent.addChild(createParent("parent1"));
        grandParent.addChild(createParent("parent2"));
        grandParent.addChild(createParent("parent3"));

        repository.deleteAll();
        System.out.println(repository.count());
        repository.save(grandParent);
        System.out.println(repository.count());

        CcNode rootNode = repository.getRootNode();




    }

    private static CcNode createParent(String name) {
        CcNode parent = new CcNode();
        parent.x = 32;
        parent.y = 21;
        parent.name = name;
        parent.setActive(false);

        CcNode child1 = new CcNode();
        child1.x = 23;
        child1.y = 11;
        child1.name = "child1";
        child1.setActive(true);

        CcNode child2 = new CcNode();
        child2.x = 23;
        child2.y = 11;
        child2.name = "child2";
        child2.setActive(true);

        CcNode child3 = new CcNode();
        child3.x = 23;
        child3.y = 11;
        child3.name = "child3";
        child3.setActive(true);
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        return parent;

    }
}
