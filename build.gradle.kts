import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.techmc"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib"))
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("KOTHPlugin.jar")
    }

    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
}

kotlin {
    jvmToolchain(17)
}
