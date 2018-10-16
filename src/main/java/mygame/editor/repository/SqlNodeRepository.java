package mygame.editor.repository;

import com.badlogic.gdx.math.Vector2;
import mygame.editor.component.Component;
import mygame.editor.component.SpriteComponent;
import mygame.editor.data.*;
import mygame.editor.data.entities.EntityBody;
import mygame.editor.data.entities.EntityNode;
import mygame.editor.data.entities.EntitySprite;
import mygame.editor.mapper.CcNodeUtil;
import mygame.editor.mapper.EntityNodeMapper;
import mygame.editor.mapper.SpriteComponentMapper;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.model.box2d.B2Joint;
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


        if(root == null){
            root = new CcNode();
            root.id = 1;
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
            SpriteComponent component = ccNode.getComponent(Component.Type.SPRITE);
            if(component != null){
                EntitySprite entitySprite = SpriteComponentMapper.map(component, ccNode.id);

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

    @Override
    public List<B2Body> getBodies() {
        return null;
    }

    @Override
    public List<B2Joint> getJoints() {
        return null;
    }

    @Override
    public void saveBody(B2Body body) {

    }

    @Override
    public void saveJoint(B2Joint joint) {

    }

    @Override
    public Vector2 getGravity() {
        return null;
    }

    @Override
    public void saveGravity(float x, float y) {

    }

    @Override
    public void deleteBody(B2Body body) {

    }

    @Override
    public void deleteJoint(B2Joint joint) {

    }



}
