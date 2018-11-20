package mygame.editor.repository;

import com.badlogic.gdx.math.Vector2;
import javafx.collections.ListChangeListener;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.model.box2d.B2Joint;
import mygame.editor.views.CcNode;

import java.util.List;

public interface NodeRepository {

    CcNode getRootNode();

    List<CcNode> getNodes();

    CcNode getNodeById(long id);

    void save(CcNode node);

    void delete(CcNode node);

    void deleteAll();

    void update(CcNode node);

    int count();

    List<B2Body> getBodies();

    List<B2Joint> getJoints();

    void deleteBody(B2Body body);

    void saveBody(B2Body body);

    void saveJoint(B2Joint joint);

    void deleteJoint(B2Joint joint);

    Vector2 getGravity();

    void saveGravity(float x, float y);

    void listenForBodies(ListChangeListener<B2Body> listener);

    void listenForJoints(ListChangeListener<B2Joint> listener);

    void listenForNodes(ListChangeListener<CcNode> listener);

}
