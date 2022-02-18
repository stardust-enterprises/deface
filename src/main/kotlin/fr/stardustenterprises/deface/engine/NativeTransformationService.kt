package fr.stardustenterprises.deface.engine

import fr.stardustenterprises.deface.engine.api.IClassTransformer
import fr.stardustenterprises.deface.engine.api.ITransformationService
import fr.stardustenterprises.yanl.NativeLoader
import java.security.ProtectionDomain

/**
 * Implementation of an [ITransformationService] using
 * external native binaries.
 *
 * @author xtrm-en
 * @since 0.1.0
 */
object NativeTransformationService : ITransformationService {

    /**
     * List of [IClassTransformer]
     */
    private val transformers = mutableListOf<IClassTransformer>()

    init {
        NativeLoader.Builder().build().loadLibrary("deface")
        registerNatives0()
    }

    /**
     * Finds a class by its name in the JVM.
     *
     * @param className the class name
     *
     * @return the class with that name
     */
    override fun findClass(className: String): Class<*> =
        getClass0(className)

    /**
     * Gets all the loaded classes in the JVM.
     *
     * @return array of loaded classes
     */
    override fun getLoadedClasses(): Array<Class<*>> =
        getLoadedClasses0()

    /**
     * Checks if a class can be modified.
     *
     * @param javaClass the [Class] instance
     *
     * @return whether or not the class is modifiable
     */
    override fun isModifiable(javaClass: Class<*>): Boolean =
        isModifiable0(javaClass)

    /**
     * Force request retransforming of a [Class].
     *
     * @param classes array/vararg of [Class]
     */
    override fun retransformClasses(vararg classes: Class<*>) =
        classes.forEach { requestRetransform0(it) }

    /**
     * Registers transformers to be added.
     *
     * @param transformers array/vararg of [IClassTransformer]
     */
    override fun addTransformers(vararg transformers: IClassTransformer) {
        this.transformers.addAll(transformers)
    }

    /**
     * Removes the provided transformers.
     *
     * @param transformers array/vararg of [IClassTransformer]
     */
    override fun removeTransformers(vararg transformers: IClassTransformer) {
        transformers.forEach{ this.transformers.remove(it) }
    }

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
