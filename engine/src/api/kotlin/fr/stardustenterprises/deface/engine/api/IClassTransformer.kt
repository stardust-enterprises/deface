package fr.stardustenterprises.deface.engine.api

import java.security.ProtectionDomain

@FunctionalInterface
interface IClassTransformer {

    fun transformClass(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray?

}