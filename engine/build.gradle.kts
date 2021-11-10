plugins {
    `java-library`
    id("kotlin")
}

val api = kotlin.sourceSets.create("api") {
    kotlin.srcDir("src/api/kotlin")
    resources.srcDir("src/api/resources")
}

kotlin.sourceSets["main"].dependsOn(api)

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Plugins.KOTLIN}")

    api(project(":patcher"))
}
