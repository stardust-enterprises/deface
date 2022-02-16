
import fr.stardustenterprises.deface.engine.NativeTransformationService
import fr.stardustenterprises.deface.engine.api.IClassTransformer
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import java.security.ProtectionDomain
import java.util.*
import kotlin.test.Test

internal class NativeTests {
    @Test
    fun `should load the appropriate native`() {
        assertDoesNotThrow {
            NativeTransformationService
        }
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    @Test
    fun `can list all classes`() {
        val classes = NativeTransformationService.getLoadedClasses()
        assert(classes?.isNotEmpty())
    }

    @Test
    fun `can get any class`() {
        assertDoesNotThrow {
            NativeTransformationService.getClass(
                "java/lang/String"
            )
            NativeTransformationService.getClass(
                "java/security/ProtectionDomain"
            )
            NativeTransformationService.getClass(
                "NativeTests"
            )
        }

        assertThrows<NoClassDefFoundError> {
            NativeTransformationService.getClass(
                "llllllllllllllllllllllllllll/${UUID.randomUUID()}"
            )
        }
    }

    @Test
    fun `is current class modifiable`() {
        assert(NativeTransformationService.isModifiable(javaClass))
    }

    @Test
    fun `modify current class`() {
        assert(!NativeTransformTest.hasBeenModified())

        NativeTransformationService.addTransformers(Transformer)
        NativeTransformationService.retransformClasses(
            NativeTransformTest::class.java
        )

        assert(NativeTransformTest.hasBeenModified())
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
        if (!className.equals("NativeTransformTest", ignoreCase = true)) return null

        val node = ClassNode(Opcodes.ASM9)
        val reader = ClassReader(classBuffer)
        reader.accept(node, ClassReader.EXPAND_FRAMES)

        node.methods.filter { it.name.equals("hasBeenModified") && it.desc.equals("()Z") }.forEach {
            it.instructions = InsnList().apply {
                add(InsnNode(Opcodes.ICONST_1))
                add(InsnNode(Opcodes.IRETURN))
            }
        }

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        node.accept(writer)

        return writer.toByteArray()
    }
}
