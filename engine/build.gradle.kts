plugins {
    `java-library`
    kotlin("jvm") version "1.5.31"
}

val ASM_VERSION = "9.2"

val api = kotlin.sourceSets.create("api") {
    kotlin.srcDir("src/api/kotlin")
    resources.srcDir("src/api/resources")
}

kotlin.sourceSets["main"].dependsOn(api)

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.ow2.asm:asm:$ASM_VERSION")
    implementation("org.ow2.asm:asm-commons:$ASM_VERSION")
    implementation("org.ow2.asm:asm-tree:$ASM_VERSION")
}
