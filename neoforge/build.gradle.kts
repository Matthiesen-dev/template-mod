plugins {
    id("matthiesen-loader")
    alias(libs.plugins.neoforged.moddev)
}

neoForge {
    version = property("neoforge_version").toString()

    val accessTransformer = project(":common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (accessTransformer.exists()) {
        accessTransformers.from(accessTransformer.absolutePath)
    }

    parchment {
        minecraftVersion = property("parchment_minecraft").toString()
        mappingsVersion = property("parchment_version").toString()
    }

    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id").toString())
            ideName = "NeoForge ${name.replaceFirstChar(Char::titlecase)} (${rootProject.path}${projectDir.toPath().let { rootDir.toPath().relativize(it).toString() }.let { if (it.isEmpty()) "" else ":${it.replace('/', ':')}" }})"
        }

        create("client") {
            client()
        }

        create("data") {
            data()
            programArguments.addAll(
                "--mod",
                property("mod_id").toString(),
                "--all",
                "--output",
                file("src/generated/resources").absolutePath,
                "--existing",
                file("src/main/resources").absolutePath
            )
        }

        create("server") {
            server()
        }
    }

    mods {
        create(property("mod_id").toString()) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}
