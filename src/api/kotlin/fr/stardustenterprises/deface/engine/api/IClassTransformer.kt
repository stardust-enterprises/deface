package fr.stardustenterprises.deface.engine.api

import org.jetbrains.annotations.Nullable
import java.security.ProtectionDomain

/**
 * Interface for all class transformation processors.
 *
 * @author xtrm-en
 * @since 0.1.0
 */
@FunctionalInterface
interface IClassTransformer {

    /**
     * Provides class data and buffer to be modified.
     *
     * @param redefinedClass the existing class (if being redefined)
     * @param classLoader the class loader
     * @param className the class name
     * @param protectionDomain the class's protection domain
     * @param classBuffer the classfile buffer
     *
     * @return the modified classfile buffer (or null)
     */
    @Nullable
    fun transformClass(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray?

}
