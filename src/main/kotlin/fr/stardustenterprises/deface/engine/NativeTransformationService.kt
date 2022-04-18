package fr.stardustenterprises.deface.engine

import fr.stardustenterprises.deface.api.engine.ITransformationService
import fr.stardustenterprises.deface.api.engine.transform.IClassTransformer
import fr.stardustenterprises.deface.api.engine.transform.ITransformationManager
import java.security.ProtectionDomain

/**
 * [ITransformationService] implementation using the JVMTI native interface.
 *
 * @author xtrm
 * @since 0.1.0
 */
object NativeTransformationService : ITransformationService, ITransformationManager {
    /**
     * @inheritDoc
     */
    override val transformers: MutableList<IClassTransformer> =
        mutableListOf()

    init {
        Loader.ensureLoaded()
        registerNatives0()
    }

    /**
     * @inheritDoc
     */
    override fun retransformClasses(vararg classes: Class<*>) =
        classes.forEach { retransformClass0(it) }

    /**
     * @inheritDoc
     */
    override fun transform(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray? {
        var buffer = classBuffer
        var modified = false

        this.transformers.forEach {
            val tmp = it.transformClass(
                redefinedClass,
                classLoader,
                className,
                protectionDomain,
                buffer
            ) ?: return@forEach

            buffer = tmp
            modified = true
        }
        return if (modified) buffer else null
    }

    @JvmStatic
    private external fun retransformClass0(javaClass: Class<*>)

    @JvmStatic
    private external fun registerNatives0()

    /** This method is known by the VM, do not change its signature. */
    @JvmStatic
    private fun transformAll(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray? = transform(
        redefinedClass,
        classLoader,
        className,
        protectionDomain,
        classBuffer
    )
}
