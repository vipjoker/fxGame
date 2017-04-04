package mygame.editor.parser;


import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.JointDef


data class RootModel(val bodies:MutableList<BodyDef> = mutableListOf(),
                     val joints :MutableList<JointDef> = mutableListOf()
)






