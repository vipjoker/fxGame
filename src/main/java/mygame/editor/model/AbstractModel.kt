package mygame.editor.model;

abstract class AbstractModel(var type:Type, var position:Point){
    var name:String? =null;get set
}

enum class Type{
    BODY,FIXTURE,JOINT,SELECTOR
}


