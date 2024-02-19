/*
 * Copyright JAMF Software, LLC
 */

val springDataVersion = "3.2.3"
val springContextSupportVersion = "6.1.3"
val jacksonVersion = "2.16.1"

plugins {
    id("regatta.library-conventions")
}

group = "com.jamf.regatta"
version = "1.0.1-SNAPSHOT"

dependencies {
    api("org.springframework.data:spring-data-keyvalue:$springDataVersion")
    api("org.springframework:spring-context-support:$springContextSupportVersion")
    api(project(mapOf("path" to ":regatta-java-core")))

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
}
