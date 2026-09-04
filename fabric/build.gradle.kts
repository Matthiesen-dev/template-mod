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
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("parchment_minecraft")}:${property("parchment_version")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    modImplementation(libs.fabric.api)
}
