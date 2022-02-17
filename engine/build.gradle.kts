@file:Suppress("DEPRECATION", "GrazieInspection")

import org.gradle.internal.jvm.Jvm
import java.io.ByteArrayOutputStream

plugins {
    `java-library`
    kotlin("jvm")
    id("fr.stardustenterprises.rust.importer") version "3.1.1"
    `maven-publish`
}

sourceSets {
    val main by getting
    val test by getting

    val api by creating {
        java.srcDir("src/api/kotlin")
        resources.srcDir("src/api/resources")

        this.compileClasspath += main.compileClasspath
        this.runtimeClasspath += main.runtimeClasspath
    }

    listOf(main, test).forEach {
        it.compileClasspath += api.output
        it.runtimeClasspath += api.output
    }
}

val projectName = project.name
val desc = "Yet Another Native Library loader and extractor for the JVM."
val authors = arrayOf("xtrm")
val repo = "stardust-enterprises/$projectName"

dependencies {
    rust(project(":platform-jvm"))
//    rust(project(":platform-art"))

    implementation("org.jetbrains.kotlin", "kotlin-stdlib", Plugins.KOTLIN)
    implementation("fr.stardustenterprises", "yanl", Dependencies.YANL)

    testImplementation("org.jetbrains.kotlin", "kotlin-test", Plugins.KOTLIN)
    testImplementation("org.ow2.asm", "asm", Dependencies.ASM)
    testImplementation("org.ow2.asm", "asm-tree", Dependencies.ASM)
}

tasks.test {
    useJUnitPlatform()
}

// what the fuck
val jniHeaderDirectory = File(buildDir.parentFile, "src/main/generated/jni")

// from: https://stackoverflow.com/a/65661275
val generateJniHeaders: Task by tasks.creating {
    group = "build"
    dependsOn(tasks.getByName("compileKotlin"))

    // For caching
    inputs.dir("src/main/kotlin")
    outputs.dir("src/main/generated/jni")

    doLast {
        jniHeaderDirectory.mkdirs()

        val javaHome = Jvm.current().javaHome
        val javap = javaHome.resolve("bin").walk().firstOrNull { it.name.startsWith("javap") }?.absolutePath
            ?: error("javap not found")
        val javac = javaHome.resolve("bin").walk().firstOrNull { it.name.startsWith("javac") }?.absolutePath
            ?: error("javac not found")
        val buildDir = file("build/classes/kotlin/main")
        val tmpDir = file("build/tmp/jvmJni").apply { mkdirs() }

        val bodyExtractingRegex = """^.+\Rpublic \w* ?class ([^\s]+).*\{\R((?s:.+))\}\R$""".toRegex()
        val nativeMethodExtractingRegex = """.*\bnative\b.*""".toRegex()

        buildDir.walkTopDown()
            .filter { "META" !in it.absolutePath }
            .forEach { file ->
                if (!file.isFile) return@forEach

                val output = ByteArrayOutputStream().use {
                    project.exec {
                        commandLine(javap, "-private", "-cp", buildDir.absolutePath, file.absolutePath)
                        standardOutput = it
                    }.assertNormalExitValue()
                    it.toString()
                }

                val (qualifiedName, methodInfo) = bodyExtractingRegex.find(output)?.destructured ?: return@forEach

                val lastDot = qualifiedName.lastIndexOf('.')
                val packageName = qualifiedName.substring(0, lastDot)
                val className = qualifiedName.substring(lastDot + 1, qualifiedName.length)

                val nativeMethods =
                    nativeMethodExtractingRegex.findAll(methodInfo).map { it.groups }
                        .flatMap { it.asSequence().mapNotNull { group -> group?.value } }.toList()
                if (nativeMethods.isEmpty()) return@forEach

                val source = buildString {
                    appendln("package $packageName;")
                    appendln("public class $className {")
                    for (method in nativeMethods) {
                        if ("()" in method) appendln(method)
                        else {
                            val updatedMethod = StringBuilder(method).apply {
                                var count = 0
                                var i = 0
                                while (i < length) {
                                    if (this[i] == ',' || this[i] == ')') insert(
                                        i,
                                        " arg${count++}".also { i += it.length + 1 })
                                    else i++
                                }
                            }
                            appendln(updatedMethod)
                        }
                    }
                    appendln("}")
                }
                val outputFile =
                    tmpDir.resolve(packageName.replace(".", "/")).apply { mkdirs() }.resolve("$className.java")
                        .apply { delete() }.apply { createNewFile() }
                outputFile.writeText(source)

                project.exec {
                    commandLine(javac, "-h", jniHeaderDirectory.absolutePath, outputFile.absolutePath)
                }.assertNormalExitValue()
            }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
