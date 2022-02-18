pluginManagement {
    repositories {
        // Fetch plugin from mavenLocal if possible
        // to aid in plugin development.
        mavenLocal()
        gradlePluginPortal()
    }
}

rootProject.name = "deface"

include("platform-jvm")
include("platform-art")
