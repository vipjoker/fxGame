package org.jbox2d.serialization.json;

import com.google.gson.Gson;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.serialization.JbDeserializer;
import org.jbox2d.serialization.UnsupportedListener;
import org.jbox2d.serialization.UnsupportedObjectException;
import org.jbox2d.serialization.json.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class JsonDeserializer implements JbDeserializer {
    private ObjectListener listener = null;
    private UnsupportedListener ulistener = null;
    private Gson gson = new Gson();
    public JsonDeserializer() {}

    public JsonDeserializer(UnsupportedListener argListener) {
        ulistener = argListener;
    }

    public JsonDeserializer(ObjectListener argObjectListner) {
        listener = argObjectListner;
    }

    public JsonDeserializer(UnsupportedListener argListener, ObjectListener argObjectListner) {
        ulistener = argListener;
        listener = argObjectListner;
    }

    @Override
    public void setObjectListener(ObjectListener argListener) {
        this.listener = argListener;
    }

    @Override
    public void setUnsupportedListener(UnsupportedListener argListener) {
        this.ulistener = argListener;

    }

    private boolean isIndependentJoint(JsonJointType argType) {
        return argType != JsonJointType.GEAR && argType != JsonJointType.CONSTANT_VOLUME;
    }

    @Override
    public World deserializeWorld(InputStream input) throws IOException, UnsupportedObjectException {
        String json = streamToString(input);
        JsonWorld jsonWorld = gson.fromJson(json, JsonWorld.class);
        return deserializeWorld(jsonWorld);
    }

    private String streamToString(InputStream input) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public World deserializeWorld(JsonWorld pbWorld) {
        World world = new World(pbToVec(pbWorld.getGravity()));

        world.setAutoClearForces(pbWorld.isAutoClearForces());
        world.setContinuousPhysics(pbWorld.isContinuousPhysics());
        world.setWarmStarting(pbWorld.isWarmStarting());
        world.setSubStepping(pbWorld.isSubStepping());

        HashMap<Integer, Body> bodyMap = new HashMap<Integer, Body>();
        HashMap<Integer, Joint> jointMap = new HashMap<Integer, Joint>();

        for (JsonBody jsonBody :pbWorld.getBodies()) {
            Body body = deserializeBody(world, jsonBody);
            int position = pbWorld.getBodies().indexOf(jsonBody);
            bodyMap.put(position, body);
        }

        // first pass, indep joints
        int cnt = 0;
        for (int i = 0; i < pbWorld.getJoints().size(); i++) {
            JsonJoint pbJoint = pbWorld.getJoints().get(i);
            if (isIndependentJoint(pbJoint.getType())) {
                Joint joint = deserializeJoint(world, pbJoint, bodyMap, jointMap);
                jointMap.put(cnt, joint);
                cnt++;
            }
        }

        // second pass, dep joints
        for (int i = 0; i < pbWorld.getJoints().size(); i++) {
            JsonJoint pbJoint = pbWorld.getJoints().get(i);
            if (!isIndependentJoint(pbJoint.getType())) {
                Joint joint = deserializeJoint(world, pbJoint, bodyMap, jointMap);
                jointMap.put(cnt, joint);
                cnt++;
            }
        }

        if (listener != null && pbWorld.getTag() != 0) {
            listener.processWorld(world, pbWorld.getTag());
        }
        return world;
    }

    public Body deserializeBody(World argWorld, JsonBody argBody) {
        JsonBody b = argBody;

        BodyDef bd = new BodyDef();
        bd.position.set(pbToVec(b.getPosition()));
        bd.angle = b.getAngle();
        bd.linearDamping = b.getLinear_damping();
        bd.angularDamping = b.getAngular_damping();
        bd.gravityScale = b.getGravity_scale();
        // velocities are populated after fixture addition

        bd.bullet = b.isBullet();
        bd.allowSleep = b.isAllow_sleep();
        bd.awake = b.isAwake();
        bd.active = b.isActive();
        bd.fixedRotation = b.isFixed_rotation();

        switch (b.getType()) {
            case DYNAMIC:
                bd.type = BodyType.DYNAMIC;
                break;
            case KINEMATIC:
                bd.type = BodyType.KINEMATIC;
                break;
            case STATIC:
                bd.type = BodyType.STATIC;
                break;
            default:
                UnsupportedObjectException e =
                        new UnsupportedObjectException("Unknown body type: " + argBody.getType(), UnsupportedObjectException.Type.BODY);
                if (ulistener == null || ulistener.isUnsupported(e)) {
                    throw e;
                }
                return null;
        }

        Body body = argWorld.createBody(bd);

        for (int i = 0; i < b.getFixtures().size(); i++) {
            deserializeFixture(body, b.getFixtures().get(i));
        }

        // adding fixtures can change this`, so we put this here and set it directly in the body
        body.m_linearVelocity.set(pbToVec(b.getLinear_velocity()));
        body.m_angularVelocity = b.getAngular_velocity();

        if (listener != null && b.getTag() != null) {
            listener.processBody(body, b.getTag());
        }
        return body;
    }
    public Fixture deserializeFixture(Body argBody, JsonFixture argFixture) {
        JsonFixture f = argFixture;

        FixtureDef fd = new FixtureDef();
        fd.density = f.getDensity();
        fd.filter.categoryBits = f.getFilter().getCategoryBits();
        fd.filter.groupIndex = f.getFilter().getGroupIndex();
        fd.filter.maskBits = f.getFilter().getMaskBits();
        fd.friction = f.getFriction();
        fd.isSensor = f.isSensor();
        fd.restitution = f.getRestitution();
        fd.shape = deserializeShape(f.getShape());

        Fixture fixture = argBody.createFixture(fd);
        if (listener != null && f.getTag() != null) {
            listener.processFixture(fixture, f.getTag());
        }
        return fixture;
    }


    public Shape deserializeShape(JsonShape argShape) {
        JsonShape s = argShape;

        Shape shape = null;
        switch (s.getType()) {
            case CIRCLE:
                CircleShape c = new CircleShape();
                c.m_p.set(pbToVec(s.getCenter()));
                shape = c;
                break;
            case POLYGON:
                PolygonShape p = new PolygonShape();
                p.m_centroid.set(pbToVec(s.getCentroid()));
                p.m_count = s.getPoints().size();
                for (int i = 0; i < p.m_count; i++) {
                    p.m_vertices[i].set(pbToVec(s.getPoints().get(i)));
                    p.m_normals[i].set(pbToVec(s.getNormals().get(i)));
                }
                shape = p;
                break;
            case EDGE:
                EdgeShape edge = new EdgeShape();
                edge.m_vertex0.set(pbToVec(s.getV0()));
                edge.m_vertex1.set(pbToVec(s.getV1()));
                edge.m_vertex2.set(pbToVec(s.getV2()));
                edge.m_vertex3.set(pbToVec(s.getV3()));
                edge.m_hasVertex0 = s.isHas0();
                edge.m_hasVertex3 = s.isHas3();
                shape = edge;
                break;
            case CHAIN: {
                ChainShape chain = new ChainShape();
                chain.m_count = s.getPoints().size();
                chain.m_vertices = new Vec2[chain.m_count];
                for (int i = 0; i < chain.m_count; i++) {
                    chain.m_vertices[i] = new Vec2(pbToVec(s.getPoints().get(i)));
                }
                chain.m_hasPrevVertex = s.isHas0();
                chain.m_hasNextVertex = s.isHas3();
                chain.m_prevVertex.set(pbToVec(s.getPrev()));
                chain.m_nextVertex.set(pbToVec(s.getNext()));
                shape = chain;
                break;
            }
            default: {
                UnsupportedObjectException e =
                        new UnsupportedObjectException("Unknown shape type: " + s.getType(), UnsupportedObjectException.Type.SHAPE);
                if (ulistener == null || ulistener.isUnsupported(e)) {
                    throw e;
                }
                return null;
            }
        }
        shape.m_radius = s.getRadius();

        if (listener != null && s.getTag() != null) {
            listener.processShape(shape, s.getTag());
        }
        return shape;
    }

    public Joint deserializeJoint(World argWorld, JsonJoint joint, Map<Integer, Body> argBodyMap,
                                  Map<Integer, Joint> jointMap) {
        JointDef jd = null;

        switch (joint.getType()) {
            case PRISMATIC: {
                PrismaticJointDef def = new PrismaticJointDef();
                jd = def;
                def.enableLimit = joint.isEnableLimit();
                def.enableMotor = joint.isEnableMotor();
                def.localAnchorA.set(pbToVec(joint.getLocalAnchorA()));
                def.localAnchorB.set(pbToVec(joint.getLocalAnchorB()));
                def.localAxisA.set(pbToVec(joint.getLocalAxisA()));
                def.lowerTranslation = joint.getLowerLimit();
                def.maxMotorForce = joint.getMaxMotorForce();
                def.motorSpeed = joint.getMotorSpeed();
                def.referenceAngle = joint.getRefAngle();
                def.upperTranslation = joint.getUpperLimit();
                break;
            }
            case REVOLUTE: {
                RevoluteJointDef def = new RevoluteJointDef();
                jd = def;
                def.enableLimit = joint.isEnableLimit();
                def.enableMotor = joint.isEnableMotor();
                def.localAnchorA.set(pbToVec(joint.getLocalAnchorA()));
                def.localAnchorB.set(pbToVec(joint.getLocalAnchorB()));
                def.lowerAngle = joint.getLowerLimit();
                def.maxMotorTorque = joint.getMaxMotorTorque();
                def.motorSpeed = joint.getMotorSpeed();
                def.referenceAngle = joint.getRefAngle();
                def.upperAngle = joint.getUpperLimit();
                break;
            }
            case DISTANCE: {
                DistanceJointDef def = new DistanceJointDef();
                jd = def;
                def.localAnchorA.set(pbToVec(joint.getLocalAnchorA()));
                def.localAnchorB.set(pbToVec(joint.getLocalAnchorB()));
                def.dampingRatio = joint.getDampingRatio();
                def.frequencyHz = joint.getFrequency();
                def.length = joint.getLength();
                break;
            }
            case PULLEY: {
                PulleyJointDef def = new PulleyJointDef();
                jd = def;
                def.localAnchorA.set(pbToVec(joint.getLocalAnchorA()));
                def.localAnchorB.set(pbToVec(joint.getLocalAnchorB()));
                def.groundAnchorA.set(pbToVec(joint.getGroundAnchorA()));
                def.groundAnchorB.set(pbToVec(joint.getGroundAnchorB()));
                def.lengthA = joint.getLengthA();
                def.lengthB = joint.getLengthB();
                def.ratio = joint.getRatio();
                break;
            }
            case MOUSE: {
                MouseJointDef def = new MouseJointDef();
                jd = def;
                def.dampingRatio = joint.getDampingRatio();
                def.frequencyHz = joint.getFrequency();
                def.maxForce = joint.getMaxForce();
                def.target.set(pbToVec(joint.getTarget()));
                break;
            }
            case GEAR: {
                GearJointDef def = new GearJointDef();
                jd = def;
                if (!jointMap.containsKey(joint.getJoint1())) {
                    throw new IllegalArgumentException("Index " + joint.getJoint1()
                            + " is not present in the joint map.");
                }
                def.joint1 = jointMap.get(joint.getJoint1());
                if (!jointMap.containsKey(joint.getJoint2())) {
                    throw new IllegalArgumentException("Index " + joint.getJoint2()
                            + " is not present in the joint map.");
                }
                def.joint2 = jointMap.get(joint.getJoint2());
                def.ratio = joint.getRatio();
                break;
            }
            case WHEEL: {
                WheelJointDef def = new WheelJointDef();
                jd = def;
                def.localAnchorA.set(pbToVec(joint.getLocalAnchorA()));
                def.localAnchorB.set(pbToVec(joint.getLocalAnchorB()));
                def.localAxisA.set(pbToVec(joint.getLocalAxisA()));
                def.enableMotor = joint.isEnableMotor();
                def.maxMotorTorque = joint.getMaxMotorTorque();
                def.motorSpeed = joint.getMotorSpeed();
                def.frequencyHz = joint.getFrequency();
                def.dampingRatio = joint.getDampingRatio();
                break;
            }
            case WELD: {
                WeldJointDef def = new WeldJointDef();
                jd = def;
                def.localAnchorA.set(pbToVec(joint.getLocalAnchorA()));
                def.localAnchorB.set(pbToVec(joint.getLocalAnchorB()));
                def.referenceAngle = joint.getRefAngle();
                def.frequencyHz = joint.getFrequency();
                def.dampingRatio = joint.getDampingRatio();
                break;
            }
            case FRICTION: {
                FrictionJointDef def = new FrictionJointDef();
                jd = def;
                def.localAnchorA.set(pbToVec(joint.getLocalAnchorA()));
                def.localAnchorB.set(pbToVec(joint.getLocalAnchorB()));
                def.maxForce = joint.getMaxForce();
                def.maxTorque = joint.getMaxTorque();
                break;
            }
            case ROPE: {
                RopeJointDef def = new RopeJointDef();
                jd = def;
                def.localAnchorA.set(pbToVec(joint.getLocalAnchorA()));
                def.localAnchorB.set(pbToVec(joint.getLocalAnchorB()));
                def.maxLength = joint.getMaxLength();
                return null;
            }
            case CONSTANT_VOLUME: {
                ConstantVolumeJointDef def = new ConstantVolumeJointDef();
                jd = def;
                def.dampingRatio = joint.getDampingRatio();
                def.frequencyHz = joint.getFrequency();
                if (joint.getBodies().size() != joint.getJoints().size()) {
                    throw new IllegalArgumentException(
                            "Constant volume joint must have bodies and joints defined");
                }
                for (int i = 0; i < joint.getBodies().size(); i++) {
                    int body = joint.getBodies().get(i);
                    if (!argBodyMap.containsKey(body)) {
                        throw new IllegalArgumentException("Index " + body + " is not present in the body map");
                    }
                    int jointIndex = joint.getJoints().get(i);
                    if (!jointMap.containsKey(jointIndex)) {
                        throw new IllegalArgumentException("Index " + jointIndex
                                + " is not present in the joint map");
                    }
                    Joint djoint = jointMap.get(jointIndex);
                    if (!(djoint instanceof DistanceJoint)) {
                        throw new IllegalArgumentException(
                                "Joints for constant volume joint must be distance joints");
                    }
                    def.addBodyAndJoint(argBodyMap.get(body), (DistanceJoint) djoint);
                }
                break;
            }
            case LINE: {
                UnsupportedObjectException e =
                        new UnsupportedObjectException("Line joint no longer supported.", UnsupportedObjectException.Type.JOINT);
                if (ulistener == null || ulistener.isUnsupported(e)) {
                    throw e;
                }
                return null;
            }
            default: {
                UnsupportedObjectException e =
                        new UnsupportedObjectException("Unknown joint type: " + joint.getType(), UnsupportedObjectException.Type.JOINT);
                if (ulistener == null || ulistener.isUnsupported(e)) {
                    throw e;
                }
                return null;
            }
        }

        jd.collideConnected = joint.isCollideConnected();

        if (!argBodyMap.containsKey(joint.getBodyA())) {
            throw new IllegalArgumentException("Index " + joint.getBodyA()
                    + " is not present in the body map");
        }
        jd.bodyA = argBodyMap.get(joint.getBodyA());

        if (!argBodyMap.containsKey(joint.getBodyB())) {
            throw new IllegalArgumentException("Index " + joint.getBodyB()
                    + " is not present in the body map");
        }
        jd.bodyB = argBodyMap.get(joint.getBodyB());

        Joint realJoint = argWorld.createJoint(jd);
        if (listener != null && joint.getTag() != 0) {
            listener.processJoint(realJoint, joint.getTag());
        }

        return realJoint;
    }

    @Override
    public Body deserializeBody(World world, InputStream input) throws IOException, UnsupportedObjectException {
        String json = streamToString(input);
        JsonBody jsonBody = gson.fromJson(json, JsonBody.class);
        return deserializeBody(world, jsonBody);
    }

    @Override
    public Fixture deserializeFixture(Body body, InputStream input) throws IOException, UnsupportedObjectException {
        String json = streamToString(input);
        JsonFixture jsonFixture = gson.fromJson(json, JsonFixture.class);
        return deserializeFixture(body, jsonFixture);
    }

    @Override
    public Shape deserializeShape(InputStream input) throws IOException, UnsupportedObjectException {
        String json = streamToString(input);
        JsonShape jsonShape  = gson.fromJson(json, JsonShape.class);
        return deserializeShape(jsonShape);
    }

    @Override
    public Joint deserializeJoint(World world, InputStream input, Map<Integer, Body> bodyMap, Map<Integer, Joint> jointMap) throws IOException, UnsupportedObjectException {
        String json = streamToString(input);
        JsonJoint jsonJoint  = gson.fromJson(json, JsonJoint.class);
        return deserializeJoint(world, jsonJoint, bodyMap, jointMap);
    }

    private Vec2 pbToVec(JsonVec2 argVec) {
        return new Vec2(argVec.getX(), argVec.getY());
    }
}
