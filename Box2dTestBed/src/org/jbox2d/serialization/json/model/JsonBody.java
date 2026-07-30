package org.jbox2d.serialization.json.model;

import org.box2d.proto.Box2D;

import java.util.List;

public class JsonBody {
        private Long tag ;
        private JsonBodyType type ;

        // physical attributes
        private JsonVec2 position ;
        private float angle;
        private JsonVec2 linear_velocity ;
        private float angular_velocity ;

        // configuration
        private float linear_damping ;
        private float angular_damping ;
        private float gravity_scale ;
        private boolean bullet ;
        private boolean allow_sleep ;
        private boolean awake ;
        private boolean active ;
        private boolean fixed_rotation ;

        private List<JsonFixture> fixtures ;

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public JsonBodyType getType() {
        return type;
    }

    public void setType(JsonBodyType type) {
        this.type = type;
    }

    public JsonVec2 getPosition() {
        return position;
    }

    public void setPosition(JsonVec2 position) {
        this.position = position;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public JsonVec2 getLinear_velocity() {
        return linear_velocity;
    }

    public void setLinear_velocity(JsonVec2 linear_velocity) {
        this.linear_velocity = linear_velocity;
    }

    public float getAngular_velocity() {
        return angular_velocity;
    }

    public void setAngular_velocity(float angular_velocity) {
        this.angular_velocity = angular_velocity;
    }

    public float getLinear_damping() {
        return linear_damping;
    }

    public void setLinear_damping(float linear_damping) {
        this.linear_damping = linear_damping;
    }

    public float getAngular_damping() {
        return angular_damping;
    }

    public void setAngular_damping(float angular_damping) {
        this.angular_damping = angular_damping;
    }

    public float getGravity_scale() {
        return gravity_scale;
    }

    public void setGravity_scale(float gravity_scale) {
        this.gravity_scale = gravity_scale;
    }

    public boolean isBullet() {
        return bullet;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }

    public boolean isAllow_sleep() {
        return allow_sleep;
    }

    public void setAllow_sleep(boolean allow_sleep) {
        this.allow_sleep = allow_sleep;
    }

    public boolean isAwake() {
        return awake;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFixed_rotation() {
        return fixed_rotation;
    }

    public void setFixed_rotation(boolean fixed_rotation) {
        this.fixed_rotation = fixed_rotation;
    }

    public List<JsonFixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<JsonFixture> fixtures) {
        this.fixtures = fixtures;
    }


    public void addFixtures(JsonFixture fixture) {
        this.fixtures.add(fixture);
    }
}
