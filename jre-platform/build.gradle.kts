import fr.stardustenterprises.rust.wrapper.WrapperExtension
import org.apache.commons.io.FileUtils

plugins {
    id("fr.stardustenterprises.rust.wrapper") version "2.0.0"
}

configure<WrapperExtension> {
    command = "cross"

    outputs = mapOf(
        "x86_64-pc-windows-gnu" to "patcher.dll",
        "x86_64-unknown-linux-gnu" to "libpatcher.so",
        "i686-unknown-linux-gnu" to "libpatcher.so",
        //"x86_64-apple-darwin" to "libpatcher.dylib",
        "i686-pc-windows-gnu" to "patcher.dll"
    )

    profile = "release"
}

val clean: Task = tasks.create("clean") {
    val file = File(this.project.projectDir, "target")
    FileUtils.deleteDirectory(file)
}.apply { this.group = "build" }

tasks.named("build").get().dependsOn(clean)