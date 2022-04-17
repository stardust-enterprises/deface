package fr.stardustenterprises.deface.engine.api.transform

import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain
import java.util.concurrent.atomic.AtomicInteger

/**
 * Functional Interface for all class transformation processors.
 *
 * @author xtrm
 * @since 0.1.0
 */
@FunctionalInterface
fun interface IClassTransformer {
    /**
     * If you want to run your transformer before another,
     * this value will handle transformation priority.
     *
     * The higher the number, the earlier the transformer will be executed.
     *
     * @return this transformer's priority
     */
    val priority: AtomicInteger
        get() = AtomicInteger(0)

    /**
     * Transforms and returns the bytecode of the given class.
     *
     * @param redefinedClass the existing class, if being redefined
     * @param classLoader the class loader
     * @param className the class name
     * @param protectionDomain the class's protection domain
     * @param classBuffer the classfile buffer
     *
     * @return the modified classfile buffer, or null
     *         if the class should not be modified.
     */
    fun transformClass(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray?

    companion object Adapter {
        /**
         * Adapts a [IClassTransformer] from a [ClassFileTransformer].
         *
         * **Note**: This [ClassFileTransformer] will also
         *           be called for retransformations.
         *
         * @param transformer the instrumentation transformer.
         *
         * @return the Deface transformer
         */
        @JvmStatic
        fun from(
            transformer: ClassFileTransformer,
            priority: Int = 0,
        ): IClassTransformer = IClassTransformer {
                redefinedClass,
                classLoader,
                className,
                protectionDomain,
                classBuffer,
            ->

            transformer.transform(
                classLoader,
                className,
                redefinedClass,
                protectionDomain,
                classBuffer
            )
        }.apply { this.priority.set(priority) }
    }
}
