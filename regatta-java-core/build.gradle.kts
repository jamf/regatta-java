val grpcVersion = "1.57.2"
val protobufVersion = "3.25.1"
val slf4jVersion = "2.0.9"

plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
}

group = "com.jamf.regatta"
version = "1.0.0-SNAPSHOT"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(project(":regatta-java-grpc"))
    api("com.google.protobuf:protobuf-java:${protobufVersion}")
    api("io.grpc:grpc-core:${grpcVersion}")
    api("io.grpc:grpc-stub:${grpcVersion}")
    api("io.grpc:grpc-netty-shaded:${grpcVersion}")


    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("org.slf4j:slf4j-api:${slf4jVersion}")
    implementation("org.slf4j:slf4j-jdk14:${slf4jVersion}")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.9.3")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
