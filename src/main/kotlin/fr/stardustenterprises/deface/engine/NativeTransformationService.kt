package fr.stardustenterprises.deface.engine

import fr.stardustenterprises.deface.engine.api.ITransformationService
import fr.stardustenterprises.deface.engine.api.transform.IClassTransformer
import fr.stardustenterprises.deface.engine.api.transform.ITransformationManager
import java.security.ProtectionDomain

/**
 * [ITransformationService] implementation using the JVMTI native interface.
 *
 * @author xtrm
 * @since 0.1.0
 */
object NativeTransformationService: ITransformationService, ITransformationManager {
    override val transformers: MutableList<IClassTransformer> =
        mutableListOf()

    init {
        Loader.ensureLoaded()
        registerNatives0()
    }

    override fun retransformClasses(vararg classes: Class<*>) =
        classes.forEach { retransformClass0(it) }

    override fun transform(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray? {
        var buffer = classBuffer
        var modified = false

        this.transformers.sortedBy { it.priority.get() }.forEach {
            buffer = (it.transformClass(
                redefinedClass,
                classLoader,
                className,
                protectionDomain,
                buffer
            ) ?: return@forEach)

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
