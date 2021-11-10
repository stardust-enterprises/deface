plugins {
    kotlin("jvm") version Plugins.KOTLIN apply false
}

group = "fr.stardust"
version = "1.0-SNAPSHOT"

subprojects {
    group = "${this.rootProject.group}.deface"
    version = this.rootProject.version.toString()

    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://jitpack.io")
    }

    plugins.withType<JavaPlugin> {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}