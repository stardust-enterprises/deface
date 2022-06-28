package fr.stardustenterprises.deface.engine

import fr.stardustenterprises.deface.api.engine.ILookupService

/**
 * [ILookupService] implementation using the JVMTI native interface.
 *
 * @author xtrm
 * @since 0.4.0
 */
object NativeLookupService : ILookupService {
    init {
        Loader.ensureLoaded()
    }

    /**
     * @inheritDoc
     */
    override fun findClass(className: String): Class<*>? =
        getLoadedClasses().firstOrNull {
            it.name.replace('.', '/') == className
        }

    /**
     * @inheritDoc
     */
    override fun getLoadedClasses(): List<Class<*>> =
        try {
            getLoadedClasses0()?.toList() ?: emptyList()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
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
            throwable.printStackTrace()
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
    private external fun getLoadedClasses0(): Array<Class<*>>?

    @JvmStatic
    private external fun getClassLoaderClasses0(
        classLoader: ClassLoader,
    ): Array<Class<*>>?

    @JvmStatic
    private external fun isModifiable0(clazz: Class<*>): Boolean
}
