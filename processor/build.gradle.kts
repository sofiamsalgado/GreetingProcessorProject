plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("kapt") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    implementation("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation(project(":annotations"))
}

kapt {
    correctErrorTypes = true
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}