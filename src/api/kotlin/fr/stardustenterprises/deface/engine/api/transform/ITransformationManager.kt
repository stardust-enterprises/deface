package fr.stardustenterprises.deface.engine.api.transform

import java.security.ProtectionDomain

/**
 * Manages transformations.
 *
 * @author xtrm
 * @since 0.4.0
 */
interface ITransformationManager {
    /**
     * List of [Transformers][IClassTransformer].
     */
    val transformers: MutableList<IClassTransformer>

    /**
     * Transforms a class.
     *
     * @param redefinedClass the existing class, if being redefined
     * @param classLoader the class loader
     * @param className the class name
     * @param protectionDomain the class's protection domain
     * @param classBuffer the classfile buffer
     *
     * @return the modified classfile buffer, or null
     *         if the class has not been modified.
     */
    fun transform(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray?
}
