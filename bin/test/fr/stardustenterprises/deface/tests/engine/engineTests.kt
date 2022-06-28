package fr.stardustenterprises.deface.tests.engine

import fr.stardustenterprises.deface.engine.Loader
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test

internal class EngineTests {
    @Test
    fun `should load the native`() {
        assertDoesNotThrow {
            Loader.ensureLoaded()
        }
    }
}
