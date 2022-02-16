plugins {
    `java-library`
    kotlin("jvm")
}

val api = kotlin.sourceSets.create("api") {
    kotlin.srcDir("src/api/kotlin")
    resources.srcDir("src/api/resources")
}

kotlin.sourceSets["main"].dependsOn(api)

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Plugins.KOTLIN}")

    api(project(":engine"))

    api("org.ow2.asm", "asm", Dependencies.ASM)
    api("org.ow2.asm", "asm-tree", Dependencies.ASM)
//    api("org.ow2.asm", "asm-commons", Dependencies.ASM)
}