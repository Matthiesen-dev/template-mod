plugins {
    id("matthiesen-common")
    alias(libs.plugins.neoforged.moddev)
}

neoForge {
    neoFormVersion = property("neo_form_version").toString()

    val accessTransformer = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (accessTransformer.exists()) {
        accessTransformers.from(accessTransformer.absolutePath)
    }

    parchment {
        minecraftVersion = property("parchment_minecraft").toString()
        mappingsVersion = property("parchment_version").toString()
    }
}

dependencies {
    compileOnly(libs.bundles.common.mixin)
    annotationProcessor(libs.mixin.extras)
}

val commonJava = configurations.create("commonJava") {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources = configurations.create("commonResources") {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    add(commonJava.name, sourceSets.main.get().java.sourceDirectories.singleFile)
    add(commonResources.name, sourceSets.main.get().resources.sourceDirectories.singleFile)
}
