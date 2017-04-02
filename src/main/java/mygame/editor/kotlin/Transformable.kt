package mygame.editor.kotlin

import mygame.editor.model.CustonAffine


interface Transformable {
    fun transform(trans:CustonAffine)
}