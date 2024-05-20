/*
 * Copyright JAMF Software, LLC
 */

val protocVersion = "3.25.2"
val grpcVersion = "1.64.0"

plugins {
    id("regatta.library-conventions")
    id("com.google.protobuf") version "0.9.4"
}

group = "com.jamf.regatta"
version = "1.1.1-SNAPSHOT"

dependencies {
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("io.grpc:grpc-services:${grpcVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protocVersion}"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
            }
        }
    }
}
