plugins {
    id("matthiesen-common")
    alias(libs.plugins.neoforged.moddev)
}

neoForge {
    neoFormVersion = libs.versions.neo.form.get()

    val accessTransformer = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (accessTransformer.exists()) {
        accessTransformers.from(accessTransformer.absolutePath)
    }

    parchment {
        minecraftVersion = libs.versions.minecraft.get()
        mappingsVersion = libs.versions.parchment.get()
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
