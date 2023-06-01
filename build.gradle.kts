
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

group = "com.hubermjonathan"
version = "3.0.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        url = uri("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.8")
    implementation("net.dv8tion:JDA:5.0.0-beta.9")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
}

kotlin {
    jvmToolchain(11)
}

tasks {
    withType<Jar> {
        archiveBaseName.set("discord-bot")
        archiveVersion.set("")

        manifest {
            attributes(
                "Main-Class" to "com.hubermjonathan.discord.BotRunnerKt",
            )
        }
    }

    build {
        dependsOn(shadowJar)
    }

    task("stage") {
        dependsOn(build)
    }
}

configure<KtlintExtension> {
    version.set("0.48.2")
}
