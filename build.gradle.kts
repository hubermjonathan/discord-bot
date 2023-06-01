plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    jar {
        manifest {
            attributes(
                "Main-Class" to "com.hubermjonathan.discord.BotRunnerKt",
            )
        }
    }
}
