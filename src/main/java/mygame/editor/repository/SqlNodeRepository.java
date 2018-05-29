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
}
