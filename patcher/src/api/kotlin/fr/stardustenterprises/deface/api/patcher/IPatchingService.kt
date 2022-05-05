package fr.stardustenterprises.deface.api.patcher

import fr.stardustenterprises.deface.api.engine.ILookupService
import fr.stardustenterprises.deface.api.engine.ITransformationService

/**
 * Interface for applying patches/"hooks" to a class.
 *
 * @author xtrm
 * @since 0.4.0
 */
interface IPatchingService {
    /**
     * The [ITransformationService] instance.
     */
    val transformationService: ITransformationService

    /**
     * The [ILookupService] instance.
     */
    val lookupService: ILookupService

    fun apply(patch: IPatch): IPatchResult
}
