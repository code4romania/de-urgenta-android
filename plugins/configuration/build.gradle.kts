import ro.code4.deurgenta.dependencies.Plugins

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("dependencies")
}

group = "ro.code4.deurgenta.plugins"
version = "SNAPSHOT"

// Required since Gradle 4.10+.
repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(Plugins.androidTools)
    implementation(Plugins.kotlin)
    implementation(Plugins.versions)
    implementation(Plugins.googleServices)
    implementation(Plugins.crashlytics)
    implementation(Plugins.sonarqube)
    implementation(Plugins.navigationSafeArgs)
    implementation (Plugins.firebaseAppDistribution)
    implementation (Plugins.detekt)
    implementation (Plugins.ktlint)
    implementation("ro.code4.deurgenta.plugins:dependencies:SNAPSHOT")
}

gradlePlugin {
    plugins {
        register("configuration") {
            id = "configuration"
            implementationClass = "ro.code4.deurgenta.configuration.ConfigurationPlugin"
        }
        register("dependencies-updater") {
            id = "dependencies-updater"
            implementationClass = "ro.code4.deurgenta.dependencies.DependenciesUpdaterPlugin"
        }
    }
}
