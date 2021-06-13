plugins {
    kotlin("jvm") version "1.5.10"
}

group = "dev.bukgeuk.polardiscord"
version = "2.0.0"

repositories {
    mavenCentral()

    maven ("https://m2.dv8tion.net/releases")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12")
    maven("https://mvnrepository.com/artifact/net.sf.trove4j/trove4j")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("net.dv8tion:JDA:4.3.0_277") {
        exclude(module="opus-java")
    }

    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    compileOnly("org.slf4j:slf4j-log4j12:1.7.30")
    compileOnly("net.sf.trove4j:trove4j:3.0.3")
}

val shade = configurations.create("shade")
shade.extendsFrom(configurations.implementation.get())

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "16"
    }

    jar {
        exclude("META-INF/LICENSE")
        exclude("META-INF/NOTICE")
        exclude("**/module-info.class")

        from (
            shade.map {
                if (it.isDirectory)
                    it
                else
                    zipTree(it)
            }
        )
    }
}