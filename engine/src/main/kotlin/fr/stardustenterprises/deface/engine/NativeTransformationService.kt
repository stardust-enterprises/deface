package fr.stardustenterprises.deface.engine

import fr.stardustenterprises.deface.engine.api.IClassTransformer
import fr.stardustenterprises.deface.engine.api.ITransformationService

object NativeTransformationService : ITransformationService {

    private val transformers = mutableListOf<IClassTransformer>()

    override fun getClass(className: String): Class<Any> =
        getClass0(className)

    override fun addTransformers(vararg transformers: IClassTransformer) {
        this.transformers.addAll(transformers)
    }

    override fun getLoadedClasses(): Array<Class<Any>> =
        getLoadedClasses0()

    override fun isModifiable(javaClass: Class<Any>): Boolean =
        isModifiable0(javaClass)

    override fun retransformClasses(vararg classes: Class<Any>) =
        classes.forEach { requestRetransform0(it) }

    @JvmStatic
    private external fun getClass0(className: String): Class<Any>

    @JvmStatic
    private external fun getLoadedClasses0(): Array<Class<Any>>

    @JvmStatic
    private external fun isModifiable0(javaClass: Class<Any>): Boolean

    @JvmStatic
    private external fun requestRetransform0(javaClass: Class<Any>)

    @JvmStatic
    fun transformAll(
        className: String,
        classBuffer: Array<Byte>,
        redefinedClass: Class<Any>?
    ): Array<Byte>? {
        var buffer = classBuffer
        var modified = false
        this.transformers.forEach {
            buffer = it.transformClass(className, buffer, redefinedClass != null) ?: return@forEach
            modified = true
        }
        return if(modified) buffer else null
    }
}
