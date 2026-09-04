import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

val archivesBaseName = property("archives_base_name").toString()
val descriptionText = property("description").toString()
val githubUrl = property("github_url").toString()
val javaVersion = property("java_version").toString()
val modName = property("mod_name").toString()
val modAuthor = property("mod_author").toString()
val credits = property("credits").toString()
val modId = property("mod_id").toString()
val minecraftVersion = property("minecraft_version").toString()
val minecraftVersionRange = property("minecraft_version_range").toString()
val fabricVersion = property("fabric_version").toString()
val fabricLoaderVersion = property("fabric_loader_version").toString()
val licenseName = property("license").toString()
val modrinthUrl = property("modrinth_url").toString()
val neoforgeVersion = property("neoforge_version").toString()
val neoforgeLoaderVersionRange = property("neoforge_loader_version_range").toString()
val resolvedModVersion = providers.environmentVariable("RELEASE_VERSION")
    .orElse(providers.gradleProperty("version"))
    .orNull
    ?: error("Set RELEASE_VERSION or version (e.g. in gradle.properties / -Pversion=...)")

group = property("group").toString()
version = resolvedModVersion

repositories {
    mavenCentral {
        content {
            excludeGroup("dev.matthiesen")
        }
    }
    maven("https://artefacts.cobblemon.com/releases/")
    maven("https://repo.spongepowered.org/repository/maven-public")
    maven("https://maven.matthiesen.dev/releases") {
        name = "devMatthiesenMavenReleases"
    }
    maven("https://maven.matthiesen.dev/snapshots") {
        name = "devMatthiesenMavenSnapshots"
    }
    // for development builds
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots1"
        mavenContent {
            snapshotsOnly()
            excludeGroup("dev.matthiesen")
        }
    }
    maven("https://central.sonatype.com/repository/maven-snapshots/") {
        name = "central-snapshots"
        mavenContent {
            snapshotsOnly()
            excludeGroup("dev.matthiesen")
        }
    }
    maven("https://maven.impactdev.net/repository/development/") {
        content {
            excludeGroup("dev.matthiesen")
        }
    }
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
    exclusiveContent {
        forRepositories(
            maven("https://maven.parchmentmc.org/") {
                name = "ParchmentMC"
            },
            maven("https://maven.neoforged.net/releases") {
                name = "NeoForge"
            }
        )
        filter { includeGroup("org.parchmentmc.data") }
    }
    maven("https://maven.blamejared.com") {
        name = "BlameJared"
    }
}

configure<JavaPluginExtension> {
    withSourcesJar()
//    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

base {
    archivesName.set("${archivesBaseName}-${project.name}-${minecraftVersion}")
}

listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    if (configurations.findByName(variant) != null) {
        configurations.named(variant) {
            outgoing.capability("$group:${base.archivesName.get()}:$version")
            outgoing.capability("$group:$modId-${project.name}-$minecraftVersion:$version")
            outgoing.capability("$group:$modId:$version")
        }
    }
}

//extensions.configure<PublishingExtension>("publishing") {
//    publications.register<MavenPublication>("mavenJava") {
//        artifactId = base.archivesName.get()
//        from(components["java"])
//    }
//}
//
//extensions.configure<MavenPublishBaseExtension>("mavenPublishing") {
//    publishToMavenCentral(automaticRelease = false)
//}

tasks {
    withType<JavaCompile>().configureEach {
        options.release.set(21)
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    named<Jar>("sourcesJar") {
        from(rootProject.file("LICENSE")) {
            rename { "${it}_$modName" }
        }
        extensions.extraProperties["mod_name"] = modName
        extensions.extraProperties["mod_author"] = modAuthor
        extensions.extraProperties["minecraft_version"] = minecraftVersion
    }

    named<Jar>("jar") {
        from(rootProject.file("LICENSE")) {
            rename { "${it}_$modName" }
        }
        extensions.extraProperties["mod_name"] = modName
        extensions.extraProperties["mod_author"] = modAuthor
        extensions.extraProperties["minecraft_version"] = minecraftVersion

        manifest {
            attributes(
                "Specification-Title" to modName,
                "Specification-Vendor" to modAuthor,
                "Specification-Version" to project.version,
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to modAuthor,
                "Built-On-Minecraft" to minecraftVersion
            )
        }
    }

    named<ProcessResources>("processResources") {
        var expandProps = mapOf(
            "version" to version,
            "group" to project.group, //Else we target the task's group.
            "minecraft_version" to minecraftVersion,
            "minecraft_version_range" to minecraftVersionRange,
            "fabric_version" to fabricVersion,
            "fabric_loader_version" to fabricLoaderVersion,
            "neoforge_version" to neoforgeVersion,
            "mod_name" to modName,
            "mod_author" to modAuthor,
            "mod_id" to modId,
            "license" to licenseName,
            "description" to descriptionText,
            "credits" to credits,
            "java_version" to javaVersion,
            "github_url" to githubUrl,
            "modrinth_url" to modrinthUrl,
            "neoforge_loader_version_range" to neoforgeLoaderVersionRange
        )

        filesMatching(
            listOf(
                "pack.mcmeta",
                "fabric.mod.json",
                "META-INF/mods.toml",
                "META-INF/neoforge.mods.toml",
                "*.mixins.json"
            )
        ) {
            expand(expandProps)
        }

        inputs.properties(expandProps)
    }

}