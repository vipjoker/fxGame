package mygame.editor.repository;

import com.badlogic.gdx.math.Vector2;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.model.box2d.B2Joint;
import mygame.editor.views.CcNode;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements NodeRepository{

    private ObservableList<B2Body> bodies = FXCollections.observableArrayList();

    private ObservableList<B2Joint> joints = FXCollections.observableArrayList();

    private Vector2 gravity = new Vector2();

    @Override
    public CcNode getRootNode() {
        return null;
    }

    @Override
    public CcNode getNodeById(long id) {
        return null;
    }

    @Override
    public void save(CcNode node) {

    }

    @Override
    public void delete(CcNode node) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void update(CcNode node) {

    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public List<B2Body> getBodies() {
        return bodies;
    }

    @Override
    public List<B2Joint> getJoints() {
        return joints;
    }

    @Override
    public void saveBody(B2Body body) {
        bodies.add(body);
    }



    @Override
    public void saveJoint(B2Joint joint) {
        joints.add(joint);
    }

    @Override
    public Vector2 getGravity() {
        return gravity;
    }

    @Override
    public void saveGravity(float x, float y) {
        gravity.set(x, y);
    }

    @Override
    public void deleteBody(B2Body body) {
        bodies.remove(body);
    }

    @Override
    public void deleteJoint(B2Joint joint) {
        joints.remove(joint);
    }
    @Override
    public void listenForBodies(ListChangeListener<B2Body> listener){
        bodies.addListener(listener);
    }
@Override
    public void listenForJoints(ListChangeListener<B2Joint> listener){
        joints.addListener(listener);
    }

}
