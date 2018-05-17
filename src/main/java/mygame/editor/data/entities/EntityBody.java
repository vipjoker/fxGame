package mygame.editor.data.entities;


/**
 * Created by oleh on 11.05.18.
 */
public class EntityBody {
    private int id;
    private int type;
    private float positionX;
    private float positionY;
    private boolean active;
    private boolean allowSleep;
    private boolean awake;
    private boolean bullet;
    private boolean fixedRotation;
    private float angle;
    private float angularDumping;
    private float angularVelocity;
    private float gravityScale;
    private float linearVelocityX;
    private float linearVelocityY;
    private float linearDamping;
    private int nodeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAllowSleep() {
        return allowSleep;
    }

    public void setAllowSleep(boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    public boolean isAwake() {
        return awake;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }

    public boolean isBullet() {
        return bullet;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngularDumping() {
        return angularDumping;
    }

    public void setAngularDumping(float angularDumping) {
        this.angularDumping = angularDumping;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public float getLinearVelocityX() {
        return linearVelocityX;
    }

    public void setLinearVelocityX(float linearVelocityX) {
        this.linearVelocityX = linearVelocityX;
    }

    public float getLinearVelocityY() {
        return linearVelocityY;
    }

    public void setLinearVelocityY(float linearVelocityY) {
        this.linearVelocityY = linearVelocityY;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }
}
