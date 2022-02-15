import fr.stardustenterprises.deface.engine.NativeTransformationService
import fr.stardustenterprises.deface.engine.api.IClassTransformer
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import java.security.ProtectionDomain

fun log(msg: String, prefix: String = ">") = println("$prefix $msg")

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        NativeTransformationService
        log("Loaded TransformationService")

        val currentClass = NativeTransformationService.getClass("Main")
        log("Has gotten current class: ${currentClass == Main::class.java}")

        val array = NativeTransformationService.getLoadedClasses()
        log("Loaded classes: ${array.size}")

        val modifiable = NativeTransformationService.isModifiable(Main::class.java)
        log("Current class modifiable: $modifiable")

        if(modifiable) {
            modifyClass()
        }
    }

    private fun modifyClass() {
        log("Has current class been modified: ${hasBeenModified()}")

        log("Transforming current class...")
        NativeTransformationService.addTransformers(MainTransformer)
        NativeTransformationService.retransformClasses(Main::class.java)

        log("Has current class been modified: ${hasBeenModified()}")
    }

    @JvmStatic
    fun hasBeenModified() = false
}

object MainTransformer : IClassTransformer {
    override fun transformClass(
        redefinedClass: Class<*>?,
        classLoader: ClassLoader?,
        className: String,
        protectionDomain: ProtectionDomain?,
        classBuffer: ByteArray,
    ): ByteArray? {
        if (!className.equals("Main", ignoreCase = true)) return null

        log("Transforming $className", prefix = ">>>")
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