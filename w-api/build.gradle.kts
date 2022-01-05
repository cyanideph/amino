val junitJupiter = "5.8.2"
val mockito = "3.+"
val ktor = "1.6.7"

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("maven-publish")
}

group = "ru.aminocoins.amino"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Api
    implementation(project(":api"))
    implementation(project(":signer"))

    // Http
    compileOnly("io.ktor", "ktor-client-core", ktor)
    compileOnly("io.ktor", "ktor-client-okhttp", ktor)
    compileOnly("io.ktor", "ktor-client-serialization", ktor)

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ru.aminocoins.amino"
            artifactId = "w-api"
            version = project.version as String

            from(components["kotlin"])
        }
    }
}
