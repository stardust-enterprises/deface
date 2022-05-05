package fr.stardustenterprises.deface.patcher.tests

/**
 * @author xtrm
 * @since 0.4.0
 */
class TargetTransformable {
    private val defaultValue = false

    fun execute(text: String, value: Boolean): String {
        val modifiedText = "$text $value"

        val currentTime = System.currentTimeMillis()
        println("Current time: $currentTime")

        println("Default Value Changed: $defaultValue")
        println("Transformed: ${isTransformed()}")

        return modifiedText
    }

    private fun isTransformed() = false
}
