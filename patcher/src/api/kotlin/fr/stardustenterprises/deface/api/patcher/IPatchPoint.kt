package fr.stardustenterprises.deface.api.patcher

/**
 * Defines a patch point and its behavior.
 *
 * @author xtrm
 * @since 0.4.0
 */
interface IPatchPoint {
    fun pinpoint(target: IPatchLocation): Int //TODO: actual method, this is just concept idea
}
