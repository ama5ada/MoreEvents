plugins {
    id("java")
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("libs/HytaleServer.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("MoreEvents")
}

val deployPlugin by tasks.registering(Copy::class) {
    dependsOn(tasks.jar)

    from(layout.buildDirectory.dir("libs")) {
        include("*.jar")
    }

    into("E:\\Hytale Dedicated\\hytale-downloader\\Server\\Server\\mods")

    doLast {
        println("Plugin copied to server plugins folder.")
    }
}

tasks.build {
    finalizedBy(deployPlugin)
}