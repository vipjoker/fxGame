package mygame.editor.parser.local;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import mygame.editor.model.box2d.*;
import mygame.editor.views.CcBodyNode;

import java.util.ArrayList;
import java.util.List;

public class B2Parser {

    public static List<Body> createBodies(List<B2Body> bodies, World world) {

        bodies = test();
        List<Body> list = new ArrayList<>();
        for (B2Body body : bodies) {
            BodyDef def = new BodyDef();

            def.type = getBodyType(body);
            def.position.set(body.getPosition().getX(), body.getPosition().getY());
            def.angularVelocity = body.getAngularVelocity();
            if (body.getLinearVelocity() != null) {
                def.linearVelocity.set(body.getLinearVelocity().getX(), body.getLinearVelocity().getY());
            }
            def.angularVelocity = body.getAngularVelocity();
            def.angle = body.getAngle();
            final Body b = world.createBody(def);
            list.add(b);
            for (B2Fixture b2Fixture : body.getFixture()) {

                switch (b2Fixture.getType()) {
                    case CIRCLE:
                        createCircleFixture(b2Fixture, b);
                        break;
                    case POLYGON:
                        createPolygonFixture(b2Fixture, b);
                        break;
                    case EDGE:
                        createEdgeFixture(b2Fixture, b);
                        break;
                    case CHAIN:
                        createChainFixture(b2Fixture, b);
                        break;

                }
            }
        }

        return list;

    }

    private static void createChainFixture(B2Fixture b2Fixture, Body b) {
        final int size = b2Fixture.getPoints().size();

        float points[] = new float[size * 2];

        for (int i = 0; i < size ; i++) {
            points[i*2] = b2Fixture.getPoints().get(i).x;
            points[i*2 + 1] = b2Fixture.getPoints().get(i).y;
        }

        ChainShape shape = new ChainShape();
        shape.createChain(points);

        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.friction = b2Fixture.getFriction();
        def.density = b2Fixture.getDensity();
        def.restitution = b2Fixture.getRestitution();
        b.createFixture(def);



    }

    private static void createEdgeFixture(B2Fixture b2Fixture, Body b) {
        final Vector2 begin = b2Fixture.getPoints().get(0);
        final Vector2 end = b2Fixture.getPoints().get(1);


        EdgeShape shape = new EdgeShape();
        shape.set(begin,end);

        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.friction = b2Fixture.getFriction();
        def.density = b2Fixture.getDensity();
        def.restitution = b2Fixture.getRestitution();
        b.createFixture(def);
    }

    private static void createPolygonFixture(B2Fixture b2Fixture, Body body) {
        PolygonShape shape = new PolygonShape();

        int size = b2Fixture.getPoints().size();
        float points[] = new float[size * 2];

        for (int i = 0; i < size ; i++) {
             points[i*2] = b2Fixture.getPoints().get(i).x;
             points[i*2 + 1] = b2Fixture.getPoints().get(i).y;
        }
        shape.set(points);
        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.friction = b2Fixture.getFriction();
        def.density = b2Fixture.getDensity();
        def.restitution = b2Fixture.getRestitution();
        body.createFixture(def);
    }

    private static void createCircleFixture(B2Fixture b2Fixture, Body body) {
        CircleShape shape = new CircleShape();
        final Vector2 center = b2Fixture.getCenter();
        shape.setPosition(center);
        shape.setRadius(b2Fixture.getRadius());

        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.friction = b2Fixture.getFriction();
        def.density = b2Fixture.getDensity();
        def.restitution = b2Fixture.getRestitution();
        body.createFixture(def);
    }


    private static BodyDef.BodyType getBodyType(B2Body body) {
        switch (body.getType()) {
            case KINEMATIC:
                return BodyDef.BodyType.KinematicBody;
            case STATIC:
                return BodyDef.BodyType.StaticBody;
            default:
                return BodyDef.BodyType.DynamicBody;
        }

    }

    public static void createJoints(List<B2Joint> joints, World world) {

    }

    private static List<B2Body> test() {
        List<B2Body> bodies = new ArrayList<>();
        bodies.add(createBody());
        for (int i = 0; i < 10; i++) {
            bodies.add(createSquare(i));
        }
        bodies.add(createGround());
        return bodies;
    }


    private static B2Body createSquare(int x) {
        B2Body bodyDef = new B2Body();
        bodyDef.setPosition(new B2Point(x, 5));

        bodyDef.setType(B2Type.DYNAMIC);


        B2Fixture fixture = new B2Fixture(B2FixtureType.CIRCLE,Vector2.Zero,new Vector2(10,0));
        fixture.setDensity(1);
        fixture.setRestitution(0.5f);
        fixture.setFriction(0.5f);

        return bodyDef;

    }

    private static B2Body createBody() {

        B2Body bodyDef = new B2Body();
        bodyDef.setPosition(new B2Point(1, 10));

        bodyDef.setType(B2Type.DYNAMIC);



        B2Fixture fixture = new B2Fixture(B2FixtureType.POLYGON,new Vector2(-.5f,-.2f),
                new Vector2(.5f,-.2f),
                new Vector2(0,0.2f));

        fixture.setDensity(1);
        fixture.setRestitution(0.5f);
        fixture.setFriction(0.5f);
        bodyDef.addFixture(fixture);
        return bodyDef;
    }


    private static B2Body createGround() {
        B2Body bodyDef = new B2Body();
        bodyDef.setPosition(new B2Point(0, -1));
        bodyDef.setType(B2Type.STATIC);


        B2Fixture fixture = new B2Fixture(B2FixtureType.CHAIN,

                new Vector2(-15,7),
                new Vector2(-10,5),
                new Vector2(10,0),
                new Vector2(15,5)
        );
        bodyDef.addFixture(fixture);
        return bodyDef;
    }


}
