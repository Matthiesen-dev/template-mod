import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("matthiesen-common")
    alias(libs.plugins.neoforged.moddev)
}

val libs = the<VersionCatalogsExtension>().named("libs")

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
    compileOnly(libs.findLibrary("sponge-mixin").get())
    compileOnly(libs.findLibrary("mixin-extras").get())
    annotationProcessor(libs.findLibrary("mixin-extras").get())
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
