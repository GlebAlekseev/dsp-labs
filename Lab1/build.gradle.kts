import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.glebalekseevjk.lab-1"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation("io.data2viz.d2v:d2v-axis:0.9.1")
                implementation("io.data2viz.d2v:d2v-chord:0.9.1")
                implementation("io.data2viz.d2v:d2v-color:0.9.1")
                implementation("io.data2viz.d2v:d2v-contour:0.9.1")
                implementation("io.data2viz.d2v:d2v-delaunay:0.9.1")
                implementation("io.data2viz.d2v:d2v-dsv:0.9.1")
                implementation("io.data2viz.d2v:d2v-ease:0.9.1")
                implementation("io.data2viz.d2v:d2v-force:0.9.1")
                implementation("io.data2viz.d2v:d2v-format:0.9.1")
                implementation("io.data2viz.d2v:d2v-geo:0.9.1")
                implementation("io.data2viz.d2v:d2v-hexbin:0.9.1")
                implementation("io.data2viz.d2v:d2v-hierarchy:0.9.1")
                implementation("io.data2viz.d2v:d2v-quadtree:0.9.1")
                implementation("io.data2viz.d2v:d2v-random:0.9.1")
                implementation("io.data2viz.d2v:d2v-scale:0.9.1")
                implementation("io.data2viz.d2v:d2v-shape:0.9.1")
                implementation("io.data2viz.d2v:d2v-tile:0.9.1")
                implementation("io.data2viz.d2v:d2v-time:0.9.1")
                implementation("io.data2viz.d2v:d2v-timer:0.9.1")
                implementation("io.data2viz.d2v:d2v-viz:0.9.1")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "Lab1"
            packageVersion = "1.0.0"
        }
    }
}
