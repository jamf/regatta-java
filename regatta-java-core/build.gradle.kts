/*
 * Copyright JAMF Software, LLC
 */

val grpcVersion = "1.61.1"
val protobufVersion = "4.26.0"
val slf4jVersion = "2.0.11"
val snappyVersion = "1.1.10.5"

plugins {
    id("regatta.library-conventions")
}

group = "com.jamf.regatta"
version = "1.0.1-SNAPSHOT"

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
}
