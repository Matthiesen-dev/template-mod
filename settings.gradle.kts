rootProject.name = "template-mod"

@Suppress("UnstableApiUsage")
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven("https://maven.fabricmc.net/") {
                    name = "Fabric"
                }
            }
            filter {
                includeGroup("net.fabricmc.unpick")
                includeGroup("net.fabricmc")
                includeGroup("fabric-loom")
            }
        }
        exclusiveContent {
            forRepository {
                maven("https://repo.spongepowered.org/repository/maven-public") {
                    name = "Sponge"
                }
            }
            filter {
                includeGroupAndSubgroups("org.spongepowered")
            }
        }
        maven("https://maven.neoforged.net/releases/")
    }

    includeBuild("gradle/build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include("common")
include("fabric")
include("neoforge")