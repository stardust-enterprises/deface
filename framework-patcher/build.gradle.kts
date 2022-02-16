plugins {
    `java-library`
    kotlin("jvm")
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

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Plugins.KOTLIN}")

    api(project(":engine"))

    api("org.ow2.asm", "asm", Dependencies.ASM)
    api("org.ow2.asm", "asm-tree", Dependencies.ASM)
//    api("org.ow2.asm", "asm-commons", Dependencies.ASM)
}
