package fr.stardustenterprises.deface.api.patcher

/**
 * @author xtrm
 * @since 0.4.0
 */
interface IPatchResult {
    val hasSucceeded: Boolean

    val canBeReverted: Boolean

    fun revert(): Boolean
}
