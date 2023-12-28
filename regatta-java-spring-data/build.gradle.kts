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
    api("org.springframework.data:spring-data-keyvalue:3.2.0")
    api("org.springframework:spring-context-support:6.1.0")
    api(project(mapOf("path" to ":regatta-java-core")))

    implementation("com.fasterxml.jackson.core:jackson-core:2.16.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.16.0")
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
