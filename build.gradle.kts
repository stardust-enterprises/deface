import org.gradle.internal.jvm.Jvm
import java.io.ByteArrayOutputStream
import java.net.URL

plugins {
    `java-library`
    kotlin("jvm") version Plugins.KOTLIN
    id("fr.stardustenterprises.rust.importer") version "3.1.1"
    id("org.jetbrains.dokka") version Plugins.DOKKA
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"
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
group = "fr.stardustenterprises"
version = "0.1.0"

val desc = "Allows for runtime transformation of classes via JVM bytecode."
val authors = arrayOf("xtrm")
val repo = "stardust-enterprises/$projectName"

repositories {
    mavenCentral()
}

dependencies {
    rust(project(":platform-jvm"))
//    rust(project(":platform-art"))

    implementation("org.jetbrains.kotlin", "kotlin-stdlib", Plugins.KOTLIN)
    implementation("fr.stardustenterprises", "yanl", Dependencies.YANL)

    testImplementation("org.jetbrains.kotlin", "kotlin-test", Plugins.KOTLIN)
    testImplementation("org.ow2.asm", "asm", Dependencies.ASM)
    testImplementation("org.ow2.asm", "asm-tree", Dependencies.ASM)
}

tasks {
    test {
        useJUnitPlatform()
    }
    dokkaHtml {
        val moduleFile = File(projectDir, "MODULE.temp.MD")

        run {
            // In order to have a description on the rendered docs, we have to have
            // a file with the # Module thingy in it. That's what we're
            // automagically creating here.

            doFirst {
                moduleFile.writeText("# Module $projectName\n$desc")
            }

            doLast {
                moduleFile.delete()
            }
        }

        moduleName.set(projectName)

        dokkaSourceSets.configureEach {
            displayName.set("$projectName github")
            includes.from(moduleFile.path)

            skipDeprecated.set(false)
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
            reportUndocumented.set(true)

            sourceRoots.from(file("src/api/kotlin"))

            // Link the source to the documentation
            sourceLink {
                localDirectory.set(file("src"))
                remoteUrl.set(URL("https://github.com/$repo/tree/trunk/src"))
            }

            // yanl external documentation links
            externalDocumentationLink {
                packageListUrl.set(URL("https://stardust-enterprises.github.io/yanl/yanl/package-list"))
                url.set(URL("https://stardust-enterprises.github.io/yanl/"))
            }
        }
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    /* Artifacts */

    // The original artifact, we just have to add the API source output and the
    // LICENSE file.
    jar {
        from(sourceSets["api"].output)
        from("LICENSE")
    }

    // API artifact, only including the output of the API source and the
    // LICENSE file.
    create("apiJar", Jar::class) {
        group = "build"

        archiveClassifier.set("api")
        from(sourceSets["api"].output)

        from("LICENSE")
    }

    // Source artifact, including everything the 'main' does but not compiled.
    create("sourcesJar", Jar::class) {
        group = "build"

        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
        from(sourceSets["api"].allSource)

        from("LICENSE")
    }

    // The Javadoc artifact, containing the Dokka output and the LICENSE file.
    create("javadocJar", Jar::class) {
        group = "build"

        archiveClassifier.set("javadoc")
        dependsOn(dokkaHtml)
        from(dokkaHtml)

        from("LICENSE")
    }
}

val artifactTasks = arrayOf(
    tasks["apiJar"],
    tasks["sourcesJar"],
    tasks["javadocJar"]
)

artifacts {
    artifactTasks.forEach(::archives)
}

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
                    appendLine("package $packageName;")
                    appendLine("public class $className {")
                    for (method in nativeMethods) {
                        if ("()" in method) appendLine(method)
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
                            appendLine(updatedMethod)
                        }
                    }
                    appendLine("}")
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

publishing.publications {
    // Sets up the Maven integration.
    create<MavenPublication>("mavenJava") {
        from(components["java"])
        artifactTasks.forEach(::artifact)

        pom {
            name.set(projectName)
            description.set(desc)
            url.set("https://github.com/$repo")

            licenses {
                license {
                    name.set("ISC License")
                    url.set("https://opensource.org/licenses/ISC")
                    distribution.set("repo")
                }
            }

            developers {
                authors.forEach {
                    developer {
                        id.set(it)
                        name.set(it)
                    }
                }
            }

            scm {
                connection.set("scm:git:git://github.com/$repo.git")
                developerConnection.set("scm:git:ssh://github.com/$repo.git")
                url.set("https://github.com/$repo")
            }
        }

        // Configure the signing extension to sign this Maven artifact.
        signing.sign(this)
    }
}

// Set up the Sonatype artifact publishing.
nexusPublishing.repositories.sonatype {
    nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
    snapshotRepositoryUrl.set(
        uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    )

    // Skip this step if environment variables NEXUS_USERNAME or NEXUS_PASSWORD aren't set.
    username.set(properties["NEXUS_USERNAME"] as? String ?: return@sonatype)
    password.set(properties["NEXUS_PASSWORD"] as? String ?: return@sonatype)
}
