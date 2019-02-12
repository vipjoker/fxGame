package mygame;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import mygame.editor.model.Node;
import mygame.editor.model.Physics;
import mygame.editor.model.Point;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class NodeTest {

    boolean result = false;
    @Test
    public void testPointXProperty(){
        result = false;
        Point node = new Point(0,0);
        node.addListener(new ChangeListener<Point>() {
            @Override
            public void changed(ObservableValue<? extends Point> observable, Point oldValue, Point newValue) {
                result = true;
            }
        });

        node.getX().set(10);


        assertTrue(result);
    }

    @Test
    public void testPointYProperty(){
        result = false;
        Point node = new Point(0,0);
        node.addListener(new ChangeListener<Point>() {
            @Override
            public void changed(ObservableValue<? extends Point> observable, Point oldValue, Point newValue) {
                result = true;
            }
        });
        node.getY().set(10);
        assertTrue(result);
    }

    @Test
    public void testPhysicsTypeProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.setType("Static");
        assertTrue(result);
    }

    @Test
    public void testPhysicsShapeProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.setShape("Circle");
        assertTrue(result);
    }

    @Test
    public void testPhysicsRestitutionProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.setRestitution(10);
        assertTrue(result);
    }

    @Test
    public void testPhysicsPointsProperty(){
        result = false;
        Physics physics = new Physics();
        physics.setShape(Physics.CHAIN);
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.getPoints().get(0).set(777,777);
        assertTrue(result);
    }



    @Test
    public void testPhysicsDensityProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.setDensity(10);
        assertTrue(result);
    }

    @Test
    public void testPhysicsFrictionProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.setFriction(20);
        assertTrue(result);
    }

    @Test
    public void testPhysicsWidthProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.setWidth(300);
        assertTrue(result);
    }

    @Test
    public void testPhysicsHeightProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.setHeight(200);
        assertTrue(result);
    }

    @Test
    public void testPhysicsCenterProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });

        physics.setCenter(10,10);
        assertTrue(result);
    }

    @Test
    public void testPhysicsRadiusProperty(){
        result = false;
        Physics physics = new Physics();
        physics.addListener(new ChangeListener<Physics>() {
            @Override
            public void changed(ObservableValue<? extends Physics> observable, Physics oldValue, Physics newValue) {
                result  = true;
            }
        });
        physics.setRadius(333);
        assertTrue(result);
    }


    @Test
    public void testNodeNameProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.setName("My name");
       assertTrue(result);

    }

    @Test
    public void testNodePositionProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.getPosition().set(10,9);
        assertTrue(result);
    }

    @Test
    public void testNodeScaleProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.getScale().set(10,2);
        assertTrue(result);
    }

    @Test
    public void testNodeAnchorProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.getAnchor().set(10,10);
        assertTrue(result);
    }

    @Test
    public void testNodeWidthProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.setWidth(9);
        assertTrue(result);
    }

    @Test
    public void testNodeHeightProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.setHeight(7);
        assertTrue(result);
    }

    @Test
    public void testNodeAngleProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.setAngle(6);
        assertTrue(result);
    }

    @Test
    public void testNodeChildrenProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.addChild(new Node());
        assertTrue(result);
    }

    @Test
    public void testNodePhysicsProperty(){
        result = false;
        Node node = new Node();
        node.addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                result = true;
            }
        });
        node.setPhysics(new Physics());
        assertTrue(result);
    }



}
