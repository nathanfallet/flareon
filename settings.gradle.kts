pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "flareon"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":core")
include(":messaging")
