package fr.stardustenterprises.deface.engine.api

interface ITransformationService {

    fun getClass(className: String): Class<Any>

    fun getLoadedClasses(): Array<Class<Any>>

    fun addTransformers(vararg transformers: IClassTransformer)

    fun isModifiable(javaClass: Class<Any>): Boolean

    fun isModifiable(className: String): Boolean =
        isModifiable(getClass(className))

    fun retransformClasses(vararg classes: Class<Any>)
}