package mygame.editor.model;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import mygame.editor.view.AbstractView;

import java.util.List;


class BodyModel(pos:Point) :AbstractModel(Type.BODY,pos){
    var bodydef:BodyDef? = BodyDef() ; get
    var fixtures:MutableList<FixtureModel> = mutableListOf() ; get

    init {
        bodydef = BodyDef()
        (bodydef as BodyDef).position.set(pos.x.toFloat(),pos.y.toFloat())

    }
}
