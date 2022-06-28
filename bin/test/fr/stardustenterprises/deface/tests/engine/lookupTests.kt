package fr.stardustenterprises.deface.tests.engine

import fr.stardustenterprises.deface.engine.NativeLookupService
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class LookupTests {
    @Test
    fun `can list all classes`() {
        val classes = NativeLookupService.getLoadedClasses()
        assertTrue(classes.isNotEmpty())
    }

    @Test
    fun `can get any class`() {
        with(NativeLookupService) {
            arrayOf(
                "java/lang/String",
                "[Ljava/security/ProtectionDomain;",
                "fr/stardustenterprises/deface/tests/engine/LookupTests"
            ).forEach { name ->
                assertNotNull(findClass(name), "Could not find class $name")
            }
        }
    }

    @Test
    fun `is current class modifiable`() {
        assertTrue(NativeLookupService.isModifiable(javaClass))
    }
}
