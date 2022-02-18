package fr.stardustenterprises.deface.engine.api

/**
 * Abstract functions for a transformation service.
 *
 * @author xtrm-en
 * @since 0.1.0
 */
interface ITransformationService {

    /**
     * Finds a class by its name in the JVM.
     *
     * @param className the class name
     *
     * @return the class with that name
     */
    fun findClass(className: String): Class<*>

    /**
     * Gets all the loaded classes in the JVM.
     *
     * @return array of loaded classes
     */
    fun getLoadedClasses(): Array<Class<*>>

    /**
     * Checks if a class can be modified.
     *
     * @param javaClass the [Class] instance
     *
     * @return whether or not the class is modifiable
     */
    fun isModifiable(javaClass: Class<*>): Boolean

    /**
     * Checks if a class can be modified.
     *
     * @param className the class name
     *
     * @return whether or not the class is modifiable
     */
    fun isModifiable(className: String): Boolean =
        isModifiable(findClass(className))

    /**
     * Force request retransforming of a [Class].
     *
     * @param classes array/vararg of [Class]
     */
    fun retransformClasses(vararg classes: Class<*>)

    /**
     * Registers transformers to be added.
     *
     * @param transformers array/vararg of [IClassTransformer]
     */
    fun addTransformers(vararg transformers: IClassTransformer)

    /**
     * Removes the provided transformers.
     *
     * @param transformers array/vararg of [IClassTransformer]
     */
    fun removeTransformers(vararg transformers: IClassTransformer)
}
