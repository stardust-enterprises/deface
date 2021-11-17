package fr.stardustenterprises.deface.api.patcher

interface IPatcher {

    fun getClassDef(className: String): ClassDefinition

    fun retransformClass(className: String, classBuffer: Array<Byte>)

}