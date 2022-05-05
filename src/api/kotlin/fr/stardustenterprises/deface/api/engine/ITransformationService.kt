package fr.stardustenterprises.deface.api.engine

import java.util.*

/**
 * Transformation service interface.
 *
 * @author xtrm
 * @since 0.1.0
 */
@FunctionalInterface
fun interface ITransformationService {
    /**
     * Force request retransforming of a [Class].
     *
     * @param classes array/vararg of [Class]
     */
    fun retransformClasses(vararg classes: Class<*>)

    /**
     * Companion object used to get service instances.
     */
    companion object {
        /**
         * The [ServiceLoader] instance.
         */
        private val loader: ServiceLoader<ITransformationService> =
            ServiceLoader.load(ITransformationService::class.java)

        /**
         * The list of [ITransformationService] implementations.
         */
        @JvmStatic
        val SERVICES: List<ITransformationService>
            get() {
                loader.reload()
                return loader.iterator().asSequence().toList()
            }
    }
}
