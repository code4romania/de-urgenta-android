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

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.google.firebase:firebase-appdistribution-gradle:2.2.0")
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }
}

