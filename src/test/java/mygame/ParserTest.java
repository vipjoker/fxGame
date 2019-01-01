package mygame;

import javafx.beans.property.DoubleProperty;
import mygame.editor.model.Node;
import mygame.editor.model.Physics;
import mygame.editor.parser.LocalParser;
import mygame.editor.views.NodeView;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {


    @Test
    public void shouldSaveNodeWithName(){
        Node node = new Node();
        node.setName("OLEH");

        List<Node> list = new ArrayList<>();
        list.add(node);

        String result = LocalParser.createJsonFromNodes(list);
        System.out.println(result);
        assertTrue(result.contains("OLEH"));

        List<Node> parsedNodes = LocalParser.createNodesFromString(result);
        assertNotNull("Node is not null",parsedNodes.get(0) );
        assertEquals("Node name", "OLEH", parsedNodes.get(0).getName().getValue());
        assertNotEquals("Node name", "OLEH9", parsedNodes.get(0).getName().getValue());
    }

    @Test
    public void childrenShouldBeSerialized(){
        List<Node> repository = new ArrayList<>();
        Node parent = new Node();
        parent.setName("Parent");
        Node childOne = new Node();
        childOne.setName("Child one");
        Node childTwo = new Node();
        childTwo.setName("Child two");
        parent.addChild(childOne);
        parent.addChild(childTwo);




        repository.add(parent);


        String json = LocalParser.createJsonFromNodes(repository);
        System.out.println(json);
        assertTrue(json.contains("Child one"));
        assertTrue(json.contains("Child two"));


        List<Node> nodesFromString = LocalParser.createNodesFromString(json);
        assertEquals(nodesFromString.get(0).getChildren().size(),2);

    }

    @Test
    public void testPhysicsParsing(){
        List<Node> nodes = new ArrayList<>();
        Node node = new Node();
        node.setWidth(700);
        node.setHeight(600);
        Physics p = new Physics();
        p.setRestitution(77);
        p.setFriction(22);
        p.setDensity(3);
        node.setPhysics(p);
        nodes.add(node);
        String jsonFromNodes = LocalParser.createJsonFromNodes(nodes);
        System.out.println(jsonFromNodes);

        List<Node> nodesFromString = LocalParser.createNodesFromString(jsonFromNodes);
        Node node1 = nodesFromString.get(0);
        double width = node1.getWidth().doubleValue();
        double height = node1.getHeight().doubleValue();
        double restitution = node1.getPhysics().getRestitution().doubleValue();
        double friction = node1.getPhysics().getFriction().doubleValue();
        double density = node1.getPhysics().getDensity().doubleValue();
        String type = node1.getPhysics().getType().get();

        double physicsHeight = node1.getPhysics().getHeight().doubleValue();
        double physicsWidth = node1.getPhysics().getWidth().doubleValue();
        assertEquals("width",700,width,0.01);
        assertEquals("height",600,height,0.01);
        assertEquals("restitution",77,restitution,0.01);
        assertEquals("friction",22,friction,0.01);
        assertEquals("density",3,density,0.01);
        assertEquals("type","static",type);

        assertEquals("physics width",700,physicsWidth,0.01);
        assertEquals("physics height",600,physicsHeight,0.01);


    }
    @Test
    public void testShapeTypes(){
        List<Node> nodes = new ArrayList<>();

        Node node = new Node();
        nodes.add(node);
        node.setWidth(300);
        node.setHeight(300);
        Physics p = new Physics();
        node.setPhysics(p);
        p.setShape("circle");


        String json = LocalParser.createJsonFromNodes(nodes);
        System.out.println(json);
        List<Node> parsedNodes = LocalParser.createNodesFromString(json);
        Node parsedNode = parsedNodes.get(0);
        Physics physics = parsedNode.getPhysics();
        double radius = physics.getRadius().get();
        assertEquals("Radius" , 150,radius,0.01);


    }

    @Test
    public void testChainShape(){
        List<Node> nodes = new ArrayList<>();

        Node node = new Node();
        nodes.add(node);
        node.setWidth(300);
        node.setHeight(300);
        Physics p = new Physics();
        node.setPhysics(p);
        p.setShape("chain");


        String json = LocalParser.createJsonFromNodes(nodes);
        System.out.println(json);
        List<Node> parsedNodes = LocalParser.createNodesFromString(json);
        Node parsedNode = parsedNodes.get(0);
        Physics physics = parsedNode.getPhysics();
        int size = physics.getPoints().size();
        assertEquals("Size" , 3,size);


    }

}
