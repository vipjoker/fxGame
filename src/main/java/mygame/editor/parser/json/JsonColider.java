package mygame.editor.parser.json;

import com.google.gson.annotations.SerializedName;

public class JsonColider extends Typeable {

    @SerializedName("_name")
    protected String name;

    @SerializedName("_objFlags")
    protected Integer objFlags;

    @SerializedName("node")
    protected JsonId node;

    @SerializedName("_enabled")
    protected Boolean enabled;

    @SerializedName("tag")
    protected Integer tag;

    @SerializedName("_density")
    protected Double density;

    @SerializedName("_sensor")
    protected Boolean sensor;

    @SerializedName("_friction")
    protected Double friction;

    @SerializedName("_restitution")
    protected Double restitution;

    @SerializedName("body")
    protected Object body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getObjFlags() {
        return objFlags;
    }

    public void setObjFlags(Integer objFlags) {
        this.objFlags = objFlags;
    }

    public JsonId getNode() {
        return node;
    }

    public void setNode(JsonId node) {
        this.node = node;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
    }

    public Boolean getSensor() {
        return sensor;
    }

    public void setSensor(Boolean sensor) {
        this.sensor = sensor;
    }

    public Double getFriction() {
        return friction;
    }

    public void setFriction(Double friction) {
        this.friction = friction;
    }

    public Double getRestitution() {
        return restitution;
    }

    public void setRestitution(Double restitution) {
        this.restitution = restitution;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
