import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev755"
    kotlin("plugin.serialization") version "1.7.0"

}

group = "me.christopher"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.4.0")
    implementation("org.deeplearning4j:deeplearning4j-core:1.0.0-beta6")

//    implementation("com.charleskorn.kaml:kaml:0.47.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ppc-compose"
            packageVersion = "1.0.0"
        }
    }
}