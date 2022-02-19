import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20-M1"
    application
}

group = "me.tavishpegram"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

application {
    mainClass.set("MainKt")
}

dependencies {
    // Testing
    val kotestVersion = "5.0.3"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.2.1")

    // Arrow
    implementation(platform("io.arrow-kt:arrow-stack:1.0.1"))
    implementation("io.arrow-kt:arrow-core")

    // Clikt for CLI
    implementation("com.github.ajalt.clikt:clikt-jvm:3.4.0")
}