package fr.stardustenterprises.deface.framework.mix.annotations

import kotlin.reflect.KClass

/**
 * Denotates a class as a Mix targetting another class
 *
 * @author xtrm-en
 */
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Mix(val value: KClass<*>)
