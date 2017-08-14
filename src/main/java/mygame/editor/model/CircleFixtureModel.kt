package mygame.editor.model

class CircleFixtutureModel(pos:Point):FixtureModel(pos){
    val center: Point = pos
    val radiusPoint: Point = pos.clone().add(10.0,0.0)

}