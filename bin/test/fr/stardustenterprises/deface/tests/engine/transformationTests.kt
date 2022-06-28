package fr.stardustenterprises.deface.tests.engine

import fr.stardustenterprises.deface.api.engine.transform.IClassTransformer
import fr.stardustenterprises.deface.engine.NativeTransformationService
import org.junit.jupiter.api.assertDoesNotThrow
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import java.security.ProtectionDomain
import kotlin.test.Test
import kotlin.test.assertTrue

const val TRANSFORMER_TARGET_NAME =
    "fr/stardustenterprises/deface/tests/engine/NativeTransformTest"

internal class TransformationTests {
    @Test
    fun `should load the service`() {
        assertDoesNotThrow {
            NativeTransformationService
        }
    }

    @Test
    fun `transform current class`() {
        assertTrue(!NativeTransformTest.hasBeenModified())

        with(NativeTransformationService) {
            transformers += Transformer
            retransformClasses(NativeTransformTest::class.java)
            transformers -= Transformer
        }

        assertTrue(NativeTransformTest.hasBeenModified())
    }
}

internal object NativeTransformTest {
    fun hasBeenModified(): Boolean = false
}

internal object Transformer : IClassTransformer {
    override fun transformClass(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray? {
        if (className != TRANSFORMER_TARGET_NAME) {
            return null
        }

        val node = ClassNode(Opcodes.ASM9)
        val reader = ClassReader(classBuffer)
        reader.accept(node, ClassReader.EXPAND_FRAMES)

        node.methods.filter {
            it.name.equals("hasBeenModified") && it.desc.equals("()Z")
        }.forEach {
            it.instructions = InsnList().apply {
                add(InsnNode(Opcodes.ICONST_1))
                add(InsnNode(Opcodes.IRETURN))
            }
        }

        val writer = ClassWriter(
            ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS
        )
        node.accept(writer)

        return writer.toByteArray()
    }
}
