package mygame.editor.model;


import java.util.List;


public class RootModel {
    List<BodyModel> dynamicBodies;
    List<BodyModel> kineticBodies;
    List<BodyModel> staticBodies;

    public List<BodyModel> getDynamicBodies() {
        return dynamicBodies;
    }

    public void setDynamicBodies(List<BodyModel> dynamicBodies) {
        this.dynamicBodies = dynamicBodies;
    }

    public List<BodyModel> getKineticBodies() {
        return kineticBodies;
    }

    public void setKineticBodies(List<BodyModel> kineticBodies) {
        this.kineticBodies = kineticBodies;
    }

    public List<BodyModel> getStaticBodies() {
        return staticBodies;
    }

    public void setStaticBodies(List<BodyModel> staticBodies) {
        this.staticBodies = staticBodies;
    }
}
