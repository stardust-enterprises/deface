package fr.stardustenterprises.deface.api.engine

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
}
