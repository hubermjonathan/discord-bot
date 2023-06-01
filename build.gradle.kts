plugins {
    java
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
    implementation("net.dv8tion:JDA:5.0.0-beta.9")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
