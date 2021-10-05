pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name="De Urgenta"
includeBuild("plugins/dependencies")
includeBuild("plugins/configuration")

include(":app")
