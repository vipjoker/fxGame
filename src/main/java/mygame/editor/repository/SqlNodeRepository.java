package mygame.editor.repository;

import mygame.editor.component.Component;
import mygame.editor.component.SpriteComponent;
import mygame.editor.data.*;
import mygame.editor.data.entities.EntityBody;
import mygame.editor.data.entities.EntityNode;
import mygame.editor.data.entities.EntitySprite;
import mygame.editor.mapper.CcNodeUtil;
import mygame.editor.mapper.EntityNodeMapper;
import mygame.editor.mapper.SpriteComponentMapper;
import mygame.editor.util.ImageUtil;
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
        try {
            EntityNode node = nodeDao.getById((int) id);
            return EntityNodeMapper.map(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CcNode getRootNode() {
        CcNode root = null;

        try {
            List<EntityNode> all = nodeDao.getAll();
            List<CcNode> ccNodes = CcNodeUtil.toCcNodesList(all);
            loadComponents(ccNodes);

            root = CcNodeUtil.buildNodeTree(all, ccNodes);
            addComponents(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    private void loadComponents(List<CcNode> nodes) {
        for (CcNode node : nodes) {
            try {
                EntitySprite entitySprite = spriteDao.getByParentId(node.id);
                if(entitySprite!= null){
                    SpriteComponent spriteComponent = SpriteComponentMapper.map(entitySprite);
                    node.addComponent(spriteComponent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addComponents(CcNode root) {
        try {

            EntitySprite spriteEntity = spriteDao.getByParentId(root.id);
            if (spriteEntity != null) {

                SpriteComponent component = new SpriteComponent(spriteEntity.getUrl());
                component.setId(spriteEntity.getId());
                component.setName(spriteEntity.getName());
                component.setUrl(spriteEntity.getUrl());

            }
            EntityBody bodyEntity = bodyDefDao.getByParentId(root.id);
            if (bodyEntity != null) {
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
        List<CcNode> nodes = new ArrayList<>();
        CcNodeUtil.flat(node,nodes).forEach(n -> {

            try {




                nodeDao.insert(n);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        for (CcNode ccNode : nodes) {
            SpriteComponent compnent = ccNode.getCompnent(Component.Type.SPRITE);
            if(compnent != null){
                EntitySprite entitySprite = SpriteComponentMapper.map(compnent, ccNode.id);

                try {
                    spriteDao.insert(entitySprite);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void delete(CcNode node) {
        try {
            nodeDao.delete(EntityNodeMapper.map(node));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
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
        grandParent.name = "grandparent";
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
