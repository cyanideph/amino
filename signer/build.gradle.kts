val ktor = "1.6.7"

plugins {
    kotlin("jvm") version "1.6.10"
    id("maven-publish")
}

group = "ru.aminocoins.amino"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Api
    compileOnly(project(":api"))

    // Http
    compileOnly("io.ktor", "ktor-client-core", ktor)
    compileOnly("io.ktor", "ktor-client-okhttp", ktor)
    compileOnly("io.ktor", "ktor-client-serialization", ktor)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ru.aminocoins.amino"
            artifactId = "signer"
            version = project.version as String

            from(components["kotlin"])
        }
    }
}
