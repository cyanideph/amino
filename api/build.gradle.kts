val slf4j = "1.7.2"
val logback = "0.9.26"
val ktor = "1.6.7"
val junitJupiter = "5.8.2"
val mockito = "3.+"

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "ru.aminocoins.amino"
version = "1.0"

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

    // Testing
    testImplementation("org.junit.jupiter", "junit-jupiter", junitJupiter)
    testImplementation("org.mockito", "mockito-core", mockito)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitJupiter)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events(
                "passed", "failed", "skipped"
            )
        }
    }
}
