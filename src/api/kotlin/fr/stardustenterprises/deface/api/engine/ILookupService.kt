package fr.stardustenterprises.deface.api.engine

import java.util.*

/**
 * Interface for looking-up and getting [Class]es information.
 *
 * @author xtrm
 * @since 0.4.0
 */
interface ILookupService {
    /**
     * Finds a class by its name in the JVM.
     *
     * @param className the class name, must be a fully qualified class name
     *                  (e.g. "java/lang/String" or "[Ljava/lang/Object;")
     *
     * @return the class with that name, or `null` if not found.
     */
    fun findClass(className: String): Class<*>?

    /**
     * Finds all loaded classes in the JVM.
     *
     * @return all loaded classes in the JVM.
     */
    fun getLoadedClasses(): List<Class<*>>

    /**
     * Finds all classes loaded by a given class loader.
     *
     * @param classLoader the class loader to look for classes
     *
     * @return all classes loaded by the given class loader.
     */
    fun getClassLoaderClasses(classLoader: ClassLoader): List<Class<*>>

    /**
     * Finds whether a class is modifiable or not.
     *
     * @param clazz the class to check.
     *
     * @return whether a class if modifiable or not.
     */
    fun isModifiable(clazz: Class<*>): Boolean

    /**
     * Companion object used to get service instances.
     */
    companion object {
        /**
         * The [ServiceLoader] instance.
         */
        private val loader: ServiceLoader<ILookupService> =
            ServiceLoader.load(ILookupService::class.java)

        /**
         * The list of [ILookupService] implementations.
         */
        @JvmStatic
        val SERVICES: List<ILookupService>
            get() {
                loader.reload()
                return loader.iterator().asSequence().toList()
            }
    }
}
