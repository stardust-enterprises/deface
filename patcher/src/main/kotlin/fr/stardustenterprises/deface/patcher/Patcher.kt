package fr.stardustenterprises.deface.patcher

import fr.stardustenterprises.deface.api.patcher.ClassDefinition
import fr.stardustenterprises.deface.api.patcher.IPatcher

class Patcher : IPatcher {

    override fun getClassDef(className: String): ClassDefinition =
        ClassDefinition(getClass0(className), getClassBuffer0(className))

    private external fun getClass0(className: String): Class<Any>

    private external fun getClassBuffer0(className: String): Array<Byte>

    override fun retransformClass(className: String, classBuffer: Array<Byte>) {
        retransformClass0(className, classBuffer)
    }

    private external fun retransformClass0(className: String, classBuffer: Array<Byte>)
}
