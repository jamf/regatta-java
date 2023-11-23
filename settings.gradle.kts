pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "regatta-java"
include("regatta-java-grpc")
include("regatta-java-core")
include("regatta-java-spring-data")
include("regatta-java-spring-data-example")
