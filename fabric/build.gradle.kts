plugins {
    id("matthiesen-loader")
    alias(libs.plugins.fabric.loom)
}

val generatedResourcesDir: File = project(":common").file("src/generated/resources")

@Suppress("UnstableApiUsage")
loom {
    val accessWidener = project(":common").file("src/main/resources/${property("mod_id")}.accesswidener")
    if (accessWidener.exists()) {
        accessWidenerPath.set(accessWidener)
    }

    mixin {
        defaultRefmapName.set("${property("mod_id")}.refmap.json")
    }

    fabricApi {
        configureDataGeneration {
            client = true
            modId = rootProject.property("mod_id").toString()
            outputDirectory = generatedResourcesDir
        }
    }

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("runs/client")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("runs/server")
        }
        named("datagen") {
            configName = "Fabric Datagen"
            ideConfigGenerated(true)
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${generatedResourcesDir.absolutePath}")
            vmArg("-Dfabric-api.datagen.modid=${rootProject.property("mod_id")}")
            runDir("runs/datagen")
        }
    }

}

@Suppress("UnstableApiUsage")
dependencies {
    minecraft(libs.minecraft.fabric)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
    })
    modImplementation(libs.bundles.fabric.mods)
}
