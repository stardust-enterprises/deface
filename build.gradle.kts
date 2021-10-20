group = "fr.stardust"
version = "1.0-SNAPSHOT"

allprojects {
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

        tasks {
            withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
            }
        }
    }
}