plugins {
    id("matthiesen-loader")
    alias(libs.plugins.fabric.loom)
}

@Suppress("UnstableApiUsage")
loom {
    val accessWidener = project(":common").file("src/main/resources/${property("mod_id")}.accesswidener")
    if (accessWidener.exists()) {
        accessWidenerPath.set(accessWidener)
    }

    mixin {
        defaultRefmapName.set("${property("mod_id")}.refmap.json")
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
