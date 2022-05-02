rootProject.name = "deface"

pluginManagement.repositories {
    mavenLocal()
    gradlePluginPortal()
}

include("platform-jvm")
include("patcher")
include("mix")
