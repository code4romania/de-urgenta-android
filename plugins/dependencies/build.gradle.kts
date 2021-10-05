plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "ro.code4.deurgenta.plugins"
version = "SNAPSHOT"

// Required since Gradle 4.10+.
repositories {
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins.register("dependencies") {
        id = "dependencies"
        implementationClass = "ro.code4.deurgenta.dependencies.DependenciesPlugin"
    }
}
