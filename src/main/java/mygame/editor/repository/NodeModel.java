package mygame.editor.repository;

import com.badlogic.gdx.math.Vector2;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import mygame.editor.model.Joint;
import mygame.editor.model.Node;
import mygame.editor.model.Point;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.model.box2d.B2Joint;
import mygame.editor.views.NodeView;

import java.util.ArrayList;
import java.util.List;

public class NodeModel {

    private final ObservableList<Node> nodes = FXCollections.observableList(new ArrayList<>(), param-> new Observable[]{param.getName()});
    private final ObservableList<Joint> joints = FXCollections.observableList(new ArrayList<>(), param-> new Observable[]{param.getType()});
    private final Point gravity = new Point(0,0);


    public List<Node> getNodes(){
        return nodes;
    }

    public Node getNodeById(long id){
        return null;
    }

    public void save(Node node){

    }

    public void delete(Node node){

    }

    public void deleteAll(){
        nodes.clear();
        joints.clear();
    }

    public void update(Node node){

    }



    public List<Joint> getJoints(){
        return joints;
    }



    void saveJoint(Joint joint){
        joints.add(joint);
    }

    void deleteJoint(Joint joint){
        joints.remove(joint);
    }

    public Point getGravity(){
        return gravity;
    }

    public void setGravity(float x, float y){
        gravity.set(x,y);
    }



    void listenForJoints(ListChangeListener<Joint> listener){
        joints.addListener(listener);
    }



    public void listenForNodes(ListChangeListener<Node> listener) {
        nodes.addListener(listener);
    }

}
