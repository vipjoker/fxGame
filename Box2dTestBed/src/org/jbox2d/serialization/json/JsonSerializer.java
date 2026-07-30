package org.jbox2d.serialization.json;

import com.google.gson.Gson;
import org.box2d.proto.Box2D;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.serialization.*;
import org.jbox2d.serialization.json.model.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JsonSerializer implements JbSerializer {
    private ObjectSigner signer = null;
    private Gson gson = new Gson();
    private UnsupportedListener listener = null;

    public JsonSerializer() {
    }

    public JsonSerializer(UnsupportedListener argListener) {
        listener = argListener;
    }

    public JsonSerializer(ObjectSigner argSigner) {
        signer = argSigner;
    }

    public JsonSerializer(UnsupportedListener argListener, ObjectSigner argSigner) {
        listener = argListener;
        signer = argSigner;
    }

    @Override
    public void setObjectSigner(ObjectSigner argSigner) {
        signer = argSigner;
    }

    @Override
    public void setUnsupportedListener(UnsupportedListener argListener) {
        listener = argListener;
    }


    @Override
    public SerializationResult serialize(World argWorld) throws UnsupportedObjectException {
        final JsonWorld world = serializeWorld(argWorld);
        return new SerializationResult() {
            @Override
            public void writeTo(OutputStream argOutputStream) throws IOException {
              writeObject(world, argOutputStream);
            }

            @Override
            public Object getValue() {
                return world;
            }
        };

    }

    private void writeObject(Object object, OutputStream argOutputStream) throws IOException{
        String data = gson.toJson(object);
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8); // specify charset!
        argOutputStream.write(bytes);
        argOutputStream.flush();
    }

    public  JsonWorld serializeWorld(World argWorld) {
        final JsonWorld builder = new JsonWorld();
        if (signer != null) {
            Long tag = signer.getTag(argWorld);
            if (tag != null) {
                builder.setTag(tag);
            }
        }

        builder.setGravity(vecToPb(argWorld.getGravity()));
        builder.setAutoClearForces(argWorld.getAutoClearForces());
        builder.setAllowSleep(argWorld.isAllowSleep());
        builder.setContinuousPhysics(argWorld.isContinuousPhysics());
        builder.setWarmStarting(argWorld.isWarmStarting());
        builder.setSubStepping(argWorld.isSubStepping());

        Body cbody = argWorld.getBodyList();
        int cnt = 0;
        HashMap<Body, Integer> bodies = new HashMap<Body, Integer>();

        while (cbody != null) {
            builder.addBodies(serializeBody(cbody));
            bodies.put(cbody, cnt);
            cnt++;
            cbody = cbody.m_next;
        }
        cnt = 0;
        HashMap<Joint, Integer> joints = new HashMap<Joint, Integer>();
        Joint cjoint = argWorld.getJointList();
        // first pass
        while (cjoint != null) {
            if (SerializationHelper.isIndependentJoint(cjoint.getType())) {
                builder.addJoints(serializeJoint(cjoint, bodies, joints));
                joints.put(cjoint, cnt);
                cnt++;
            }
            cjoint = cjoint.m_next;
        }

        // second pass for dependent joints
        cjoint = argWorld.getJointList();
        while (cjoint != null) {
            if (!SerializationHelper.isIndependentJoint(cjoint.getType())) {
                builder.addJoints(serializeJoint(cjoint, bodies, joints));
                joints.put(cjoint, cnt);
                cnt++;
            }
            cjoint = cjoint.m_next;
        }

        return builder;
    }




    @Override
    public SerializationResult serialize(Body argBody) {
       JsonBody builder = serializeBody(argBody);
        if (builder == null) {
            return null;
        }
        final JsonBody body = builder;
        return new SerializationResult() {
            @Override
            public void writeTo(OutputStream argOutputStream) throws IOException {
                String data = gson.toJson(body);
                byte[] bytes = data.getBytes(StandardCharsets.UTF_8); // specify charset!
                argOutputStream.write(bytes);
                argOutputStream.flush();
            }

            @Override
            public Object getValue() {
                return body;
            }
        };
    }

    public JsonBody serializeBody(Body argBody) {
        JsonBody builder = new JsonBody();
        if (signer != null) {
            Long id = signer.getTag(argBody);
            if (id != null) {
                builder.setTag(id);
            }
        }
        switch (argBody.getType()) {
            case DYNAMIC:
                builder.setType(JsonBodyType.DYNAMIC);
                break;
            case KINEMATIC:
                builder.setType(JsonBodyType.KINEMATIC);
                break;
            case STATIC:
                builder.setType(JsonBodyType.STATIC);
                break;
            default:
                UnsupportedObjectException e = new UnsupportedObjectException("Unknown body type: "
                        + argBody.getType(), UnsupportedObjectException.Type.BODY);
                if (listener == null || listener.isUnsupported(e)) {
                    throw e;
                }
                return null;
        }
        builder.setPosition(vecToPb(argBody.getPosition()));
        builder.setAngle(argBody.getAngle());
        builder.setLinear_velocity(vecToPb(argBody.getLinearVelocity()));
        builder.setAngular_velocity(argBody.getAngularVelocity());
        builder.setLinear_damping(argBody.getLinearDamping());
        builder.setAngular_damping(argBody.getAngularDamping());
        builder.setGravity_scale(argBody.getGravityScale());

        builder.setBullet(argBody.isBullet());
        builder.setAllow_sleep(argBody.isSleepingAllowed());
        builder.setAwake(argBody.isAwake());
        builder.setActive(argBody.isActive());
        builder.setFixed_rotation(argBody.isFixedRotation());

        Fixture curr = argBody.m_fixtureList;
        while (curr != null) {
            builder.addFixtures(serializeFixture(curr));
            curr = curr.m_next;
        }

        return builder;
    }



    @Override
    public SerializationResult serialize(Fixture argFixture) {
        final JsonFixture fixture = serializeFixture(argFixture);
        return new SerializationResult() {
            @Override
            public void writeTo(OutputStream argOutputStream) throws IOException {
                writeObject(fixture, argOutputStream);
            }

            @Override
            public Object getValue() {
                return fixture;
            }
        };
    }

    public JsonFixture serializeFixture(Fixture argFixture) {
        final JsonFixture builder = new JsonFixture();
        if (signer != null) {
            Long tag = signer.getTag(argFixture);
            if (tag != null) {
                builder.setTag(tag);
            }
        }

        builder.setDensity(argFixture.m_density);
        builder.setFriction(argFixture.m_friction);
        builder.setRestitution(argFixture.m_restitution);
        builder.setSensor(argFixture.m_isSensor);

        builder.setShape(serializeShape(argFixture.m_shape));

        builder.setFilter(serializeFilter(argFixture.m_filter));
        return builder;
    }



    @Override
    public SerializationResult serialize(Shape argShape) {
        JsonShape builder = serializeShape(argShape);
        if (builder == null) {
            return null;
        }
        // should we do lazy building?
        final JsonShape shape = builder;
        return new SerializationResult() {
            @Override
            public void writeTo(OutputStream argOutputStream) throws IOException {
                writeObject(shape, argOutputStream);
            }

            @Override
            public Object getValue() {
                return shape;
            }
        };
    }

    public JsonShape serializeShape(Shape argShape) {
        final JsonShape builder = new JsonShape();
        if (signer != null) {
            Long tag = signer.getTag(argShape);
            if (tag != null) {
                builder.setTag(tag);
            }
        }
        builder.setRadius(argShape.m_radius);

        switch (argShape.m_type) {
            case CIRCLE:
                CircleShape c = (CircleShape) argShape;
                builder.setType(JsonShapeType.CIRCLE);
                builder.setCenter(vecToPb(c.m_p));
                break;
            case POLYGON:
                PolygonShape p = (PolygonShape) argShape;
                builder.setType(JsonShapeType.POLYGON);
                builder.setCentroid(vecToPb(p.m_centroid));
                for (int i = 0; i < p.m_count; i++) {
                    builder.addPoints(vecToPb(p.m_vertices[i]));
                    builder.addNormals(vecToPb(p.m_normals[i]));
                }
                break;
            case EDGE:
                EdgeShape e = (EdgeShape) argShape;
                builder.setType(JsonShapeType.EDGE);
                builder.setV0(vecToPb(e.m_vertex0));
                builder.setV1(vecToPb(e.m_vertex1));
                builder.setV2(vecToPb(e.m_vertex2));
                builder.setV3(vecToPb(e.m_vertex3));
                builder.setHas0(e.m_hasVertex0);
                builder.setHas3(e.m_hasVertex3);
                break;
            case CHAIN:
                ChainShape h = (ChainShape) argShape;
                builder.setType(JsonShapeType.CHAIN);
                for (int i = 0; i < h.m_count; i++) {
                    builder.addPoints(vecToPb(h.m_vertices[i]));
                }
                builder.setPrev(vecToPb(h.m_prevVertex));
                builder.setNext(vecToPb(h.m_nextVertex));
                builder.setHas0(h.m_hasPrevVertex);
                builder.setHas3(h.m_hasNextVertex);
                break;
            default:
                UnsupportedObjectException ex = new UnsupportedObjectException(
                        "Currently only encodes circle and polygon shapes", UnsupportedObjectException.Type.SHAPE);
                if (listener == null || listener.isUnsupported(ex)) {
                    throw ex;
                }
                return null;
        }

        return builder;
    }
    @Override
    public SerializationResult serialize(Joint argJoint, Map<Body, Integer> argBodyIndexMap,
                                         Map<Joint, Integer> argJointIndexMap) {
       JsonJoint builder = serializeJoint(argJoint, argBodyIndexMap, argJointIndexMap);
        if (builder == null) {
            return null;
        }
        final JsonJoint joint = builder;
        return new SerializationResult() {
            @Override
            public void writeTo(OutputStream argOutputStream) throws IOException {
                writeObject(joint, argOutputStream);
            }


            @Override
            public Object getValue() {
                return joint;
            }
        };
    }

    public JsonJoint serializeJoint(Joint joint, Map<Body, Integer> argBodyIndexMap,
                                                Map<Joint, Integer> argJointIndexMap) {
        final JsonJoint builder = new JsonJoint();
        if (signer != null) {
            Long tag = signer.getTag(joint);
            if (tag != null) {
                builder.setTag(tag);
            } else {
                builder.clearTag();
            }
        }
        Body bA = joint.getBodyA();
        Body bB = joint.getBodyB();

        if (!argBodyIndexMap.containsKey(bA)) {
            throw new IllegalArgumentException("Body " + bA + " is not present in the index map");
        }
        builder.setBodyA(argBodyIndexMap.get(bA));

        if (!argBodyIndexMap.containsKey(bB)) {
            throw new IllegalArgumentException("Body " + bB + " is not present in the index map");
        }
        builder.setBodyB(argBodyIndexMap.get(bB));

        builder.setCollideConnected(joint.getCollideConnected());

        switch (joint.getType()) {
            case REVOLUTE: {
                RevoluteJoint j = (RevoluteJoint) joint;
                builder.setType(JsonJointType.REVOLUTE);
                builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
                builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
                builder.setRefAngle(j.getReferenceAngle());
                builder.setEnableLimit(j.isLimitEnabled());
                builder.setLowerLimit(j.getLowerLimit());
                builder.setUpperLimit(j.getUpperLimit());
                builder.setEnableMotor(j.isMotorEnabled());
                builder.setMotorSpeed(j.getMotorSpeed());
                builder.setMaxMotorTorque(j.getMaxMotorTorque());
                break;
            }
            case PRISMATIC: {
                PrismaticJoint j = (PrismaticJoint) joint;
                builder.setType(JsonJointType.PRISMATIC);
                builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
                builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
                builder.setLocalAxisA(vecToPb(j.getLocalAxisA()));
                builder.setRefAngle(j.getReferenceAngle());
                builder.setEnableLimit(j.isLimitEnabled());
                builder.setLowerLimit(j.getLowerLimit());
                builder.setUpperLimit(j.getUpperLimit());
                builder.setEnableMotor(j.isMotorEnabled());
                builder.setMaxMotorForce(j.getMaxMotorForce());
                builder.setMotorSpeed(j.getMotorSpeed());
                break;
            }
            case DISTANCE: {
                DistanceJoint j = (DistanceJoint) joint;
                builder.setType(JsonJointType.DISTANCE);
                builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
                builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
                builder.setLength(j.getLength());
                builder.setFrequency(j.getFrequency());
                builder.setDampingRatio(j.getDampingRatio());
                break;
            }
            case PULLEY: {
                PulleyJoint j = (PulleyJoint) joint;
                builder.setType(JsonJointType.PULLEY);
                builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
                builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
                builder.setGroundAnchorA(vecToPb(j.getGroundAnchorA()));
                builder.setGroundAnchorB(vecToPb(j.getGroundAnchorB()));
                builder.setLengthA(j.getLengthA());
                builder.setLengthB(j.getLengthB());
                builder.setRatio(j.getRatio());
                break;
            }
            case MOUSE: {
                MouseJoint j = (MouseJoint) joint;
                builder.setType(JsonJointType.MOUSE);
                builder.setTarget(vecToPb(j.getTarget()));
                builder.setMaxForce(j.getMaxForce());
                builder.setFrequency(j.getFrequency());
                builder.setDampingRatio(j.getDampingRatio());
                break;
            }
            case GEAR: {
                GearJoint j = (GearJoint) joint;
                builder.setType(JsonJointType.GEAR);
                builder.setRatio(j.getRatio());
                if (!argJointIndexMap.containsKey(j.getJoint1())) {
                    throw new IllegalArgumentException("Joint 1 not in map");
                }
                int j1 = argJointIndexMap.get(j.getJoint1());
                if (!argJointIndexMap.containsKey(j.getJoint2())) {
                    throw new IllegalArgumentException("Joint 2 not in map");
                }
                int j2 = argJointIndexMap.get(j.getJoint2());

                builder.setJoint1(j1);
                builder.setJoint2(j2);
                break;
            }
            case FRICTION: {
                FrictionJoint j = (FrictionJoint) joint;
                builder.setType(JsonJointType.FRICTION);
                builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
                builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
                builder.setMaxForce(j.getMaxForce());
                builder.setMaxTorque(j.getMaxTorque());
                break;
            }
            case CONSTANT_VOLUME: {
                ConstantVolumeJoint j = (ConstantVolumeJoint) joint;
                builder.setType(JsonJointType.CONSTANT_VOLUME);

                for (int i = 0; i < j.getBodies().length; i++) {
                    Body b = j.getBodies()[i];
                    DistanceJoint djoint = j.getJoints()[i];
                    if (!argBodyIndexMap.containsKey(b)) {
                        throw new IllegalArgumentException("Body " + b + " is not present in the index map");
                    }
                    builder.addBodies(argBodyIndexMap.get(b));
                    if (!argJointIndexMap.containsKey(djoint)) {
                        throw new IllegalArgumentException("Joint " + djoint
                                + " is not present in the index map");
                    }
                    builder.addJoints(argJointIndexMap.get(djoint));
                }
                break;
            }
            case WHEEL: {
                WheelJoint j = (WheelJoint) joint;
                builder.setType(JsonJointType.WHEEL);
                builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
                builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
                builder.setLocalAxisA(vecToPb(j.getLocalAxisA()));
                builder.setEnableMotor(j.isMotorEnabled());
                builder.setMaxMotorTorque(j.getMaxMotorTorque());
                builder.setMotorSpeed(j.getMotorSpeed());
                builder.setFrequency(j.getSpringFrequencyHz());
                builder.setDampingRatio(j.getSpringDampingRatio());
                break;
            }
            case ROPE: {
                RopeJoint j = (RopeJoint) joint;
                builder.setType(JsonJointType.ROPE);
                builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
                builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
                builder.setMaxLength(j.getMaxLength());
                break;
            }
            case WELD: {
                WeldJoint j = (WeldJoint) joint;
                builder.setType(JsonJointType.WELD);
                builder.setLocalAnchorA(vecToPb(j.getLocalAnchorA()));
                builder.setLocalAnchorB(vecToPb(j.getLocalAnchorB()));
                builder.setRefAngle(j.getReferenceAngle());
                builder.setFrequency(j.getFrequency());
                builder.setDampingRatio(j.getDampingRatio());
                break;
            }
            default:
                UnsupportedObjectException e = new UnsupportedObjectException("Unknown joint type: "
                        + joint.getType(), UnsupportedObjectException.Type.JOINT);
                if (listener == null || listener.isUnsupported(e)) {
                    throw e;
                }
                return null;
        }
        return builder;
    }

    public JsonFilter serializeFilter(Filter argFilter) {
        JsonFilter builder = new JsonFilter();
        builder.setCategoryBits(argFilter.categoryBits);
        builder.setGroupIndex(argFilter.groupIndex);
        builder.setMaskBits(argFilter.maskBits);

        return builder;
    }

    private JsonVec2 vecToPb(Vec2 argVec) {
        if (argVec == null) {
            return null;
        }
        return new JsonVec2(argVec.x,argVec.y);
    }
}
