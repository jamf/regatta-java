plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    id("org.springframework.boot") version "3.1.6"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    api("org.springframework.data:spring-data-keyvalue:3.2.0")
    api(project(":regatta-java-core"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(mapOf("path" to ":regatta-java-spring-data")))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
