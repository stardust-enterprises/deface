package fr.stardustenterprises.deface.patcher

import fr.stardustenterprises.deface.api.engine.ILookupService
import fr.stardustenterprises.deface.api.engine.ITransformationService
import fr.stardustenterprises.deface.api.patcher.IPatch
import fr.stardustenterprises.deface.api.patcher.IPatchResult
import fr.stardustenterprises.deface.api.patcher.IPatchingService
import fr.stardustenterprises.deface.engine.NativeLookupService
import fr.stardustenterprises.deface.engine.NativeTransformationService

/**
 * Default implementation of the [IPatchingService] interface,
 * using the native Deface services.
 *
 * @author xtrm
 * @since 0.4.0
 */
object Patcher: IPatchingService {
    /**
     * @inheritDoc
     */
    override val transformationService: ITransformationService =
        NativeTransformationService

    /**
     * @inheritDoc
     */
    override val lookupService: ILookupService =
        NativeLookupService

    override fun apply(patch: IPatch): IPatchResult =
        TODO("Not yet implemented")
}
