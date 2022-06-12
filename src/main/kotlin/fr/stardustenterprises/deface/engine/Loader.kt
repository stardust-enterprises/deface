package fr.stardustenterprises.deface.engine

import fr.stardustenterprises.yanl.NativeLoader

/**
 * Internal loading mechanism for lazy-loading the native library.
 *
 * @author xtrm
 * @since 0.4.0
 */
internal object Loader {
    private val loaded by lazy {
        try {
            NativeLoader.Builder().build().loadLibrary("deface")
            true
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            false
        }
    }

    internal fun ensureLoaded() {
        if (!loaded) {
            throw IllegalStateException("libdeface could not be loaded.")
        }
    }
}
