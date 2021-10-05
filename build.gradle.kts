// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("dependencies-updater")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }
}
