package fr.stardustenterprises.deface.engine.api

@FunctionalInterface
interface IClassTransformer {

    fun transformClass(
        className: String,
        classBuffer: Array<Byte>,
        alreadyDefined: Boolean
    ): Array<Byte>?

}