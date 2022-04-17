package fr.stardustenterprises.deface.engine

import fr.stardustenterprises.deface.engine.api.IClassLookupService

/**
 * [IClassLookupService] implementation using the JVMTI native interface.
 *
 * @author xtrm
 * @since 0.4.0
 */
object NativeClassLookupService : IClassLookupService {
    init {
        Loader.ensureLoaded()
    }

    /**
     * @inheritDoc
     */
    override fun findClass(className: String): Class<*> =
        getLoadedClasses().firstOrNull { it.name == className }
            ?: throw ClassNotFoundException(className)

    /**
     * @inheritDoc
     */
    override fun getLoadedClasses(): List<Class<*>> =
        try {
            getLoadedClasses0()?.toList() ?: emptyList()
        } catch (throwable: Throwable) {
            emptyList()
        }

    /**
     * @inheritDoc
     */
    override fun getClassLoaderClasses(
        classLoader: ClassLoader,
    ): List<Class<*>> =
        try {
            getClassLoaderClasses0(classLoader)?.toList() ?: emptyList()
        } catch (throwable: Throwable) {
            emptyList()
        }

    /**
     * @inheritDoc
     */
    override fun isModifiable(clazz: Class<*>): Boolean =
        try {
            isModifiable0(clazz)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            false
        }

    @JvmStatic
    internal external fun getLoadedClasses0(): Array<Class<*>>?

    @JvmStatic
    internal external fun getClassLoaderClasses0(
        classLoader: ClassLoader,
    ): Array<Class<*>>?

    @JvmStatic
    internal external fun isModifiable0(clazz: Class<*>): Boolean
}
