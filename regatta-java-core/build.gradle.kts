/*
 * Copyright JAMF Software, LLC
 */

val grpcVersion = "1.63.0"
val protobufVersion = "3.25.2"
val slf4jVersion = "2.0.13"
val snappyVersion = "1.1.10.5"
val jupiterVersion = "5.10.2"
val grpcTestingVersion = "1.63.0"
val assertjVersion = "3.25.3"

plugins {
    id("regatta.library-conventions")
}

group = "com.jamf.regatta"
version = "1.1.2-SNAPSHOT"

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(project(":regatta-java-grpc"))
    api("com.google.protobuf:protobuf-java:${protobufVersion}")
    api("io.grpc:grpc-core:${grpcVersion}")
    api("io.grpc:grpc-stub:${grpcVersion}")
    api("io.grpc:grpc-netty-shaded:${grpcVersion}")


    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("org.slf4j:slf4j-api:${slf4jVersion}")
    implementation("org.xerial.snappy:snappy-java:${snappyVersion}")

    testImplementation("org.assertj:assertj-core:${assertjVersion}")
    testImplementation("io.grpc:grpc-testing:${grpcTestingVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${jupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-migrationsupport:${jupiterVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${jupiterVersion}")
}
