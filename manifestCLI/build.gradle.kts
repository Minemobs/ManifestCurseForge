plugins {
    kotlin("jvm") version "1.6.10"
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "fr.minemobs"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("${group}.manifestcli.ManifestCLIKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    // Publish sources and javadoc
    withSourcesJar()
    withJavadocJar()
}

tasks.processResources {
    expand("version" to project.version)
}

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven {
                url = uri("https://jitpack.io")
            }
        }
        filter {
            // Only use JitPack for the `gson-record-type-adapter-factory` library
            includeModule("com.github.Marcono1234", "gson-record-type-adapter-factory")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("commons-cli:commons-cli:1.5.0")
    implementation("com.github.Marcono1234:gson-record-type-adapter-factory:v0.1.0")
    implementation(project(":ManifestAPI"))
}