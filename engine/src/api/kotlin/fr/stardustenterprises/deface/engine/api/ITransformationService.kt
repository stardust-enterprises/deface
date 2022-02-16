package fr.stardustenterprises.deface.engine.api

interface ITransformationService {

    fun getClass(className: String): Class<*>

    fun getLoadedClasses(): Array<Class<*>>

    fun addTransformers(vararg transformers: IClassTransformer)

    fun isModifiable(javaClass: Class<*>): Boolean

    fun isModifiable(className: String): Boolean =
        isModifiable(getClass(className))

    fun retransformClasses(vararg classes: Class<*>)
}
