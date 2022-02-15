package fr.stardustenterprises.deface.engine

import fr.stardustenterprises.deface.engine.api.IClassTransformer
import fr.stardustenterprises.deface.engine.api.ITransformationService
import fr.stardustenterprises.yanl.NativeLoader
import java.security.ProtectionDomain

object NativeTransformationService : ITransformationService {

    private val transformers = mutableListOf<IClassTransformer>()

    init {
        NativeLoader.Builder().build().loadLibrary("engine")
        registerNatives0()
    }

    override fun getClass(className: String): Class<*> =
        getClass0(className)

    override fun addTransformers(vararg transformers: IClassTransformer) {
        this.transformers.addAll(transformers)
    }

    override fun getLoadedClasses(): Array<Class<*>> =
        getLoadedClasses0()

    override fun isModifiable(javaClass: Class<*>): Boolean =
        isModifiable0(javaClass)

    override fun retransformClasses(vararg classes: Class<*>) =
        classes.forEach { requestRetransform0(it) }

    @JvmStatic
    private external fun getClass0(className: String): Class<*>

    @JvmStatic
    private external fun getLoadedClasses0(): Array<Class<*>>

    @JvmStatic
    private external fun isModifiable0(javaClass: Class<*>): Boolean

    @JvmStatic
    private external fun requestRetransform0(javaClass: Class<*>)

    @JvmStatic
    private external fun registerNatives0()

    @JvmStatic
    private fun transformAll(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray? {
        var buffer = classBuffer
        var modified = false
        this.transformers.forEach {
            buffer = (it.transformClass(redefinedClass, classLoader, className, protectionDomain, buffer) ?: return@forEach)
            modified = true
        }
        return if(modified) buffer else null
    }
}
