val slf4j = "1.7.2"
val logback = "0.9.26"
val ktor = "1.6.7"

plugins {
    kotlin("jvm") version "1.6.10"
}

group = "ru.aminocoins.amino"
version = "1.0-beta"

repositories {
    mavenCentral()
}

dependencies {
    // Logging
    implementation("org.slf4j", "slf4j-api", slf4j)
    runtimeOnly("ch.qos.logback", "logback-classic", logback)

    // Http
    implementation("io.ktor", "ktor-client-core", ktor)
    implementation("io.ktor", "ktor-client-okhttp", ktor)
    implementation("io.ktor", "ktor-client-serialization", ktor)
}