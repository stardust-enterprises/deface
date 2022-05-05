package fr.stardustenterprises.deface.api.patcher

/**
 * Represents a patch target location.
 *
 * @author xtrm
 * @since 0.4.0
 */
interface IPatchLocation {
    /**
     * The target's owner name.
     */
    val ownerName: String

    /**
     * The target member's name.
     */
    val name: String

    /**
     * The target member's descriptor.
     */
    val descriptor: String

    /**
     * The target member's signature.
     *
     * Can be null.
     */
    val signature: String?
}
